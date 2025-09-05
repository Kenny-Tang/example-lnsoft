package com.redjujubetree.fs.queue;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * 线程优化版消息队列（无延时消息支持）
 * 
 * 核心优化：
 * - 统一读取器处理所有消息（包括广播和竞争模式）
 * - 固定线程数量，不随订阅者增加而增长
 * - 通过消息分发器智能路由消息
 */
public class ThreadOptimizedMessageQueue {

    private static final Logger log = LoggerFactory.getLogger("ThreadOptimizedMessageQueue");
    
    private final ChronicleQueue queue;
    private final ExcerptAppender appender;
    private final ExecutorService processingExecutor;
    
    // 优化后的组件
    private final MessageReader messageReader;           // 统一消息读取器
    private final MessageDispatcher messageDispatcher;   // 消息分发器
    private final SubscriptionManager subscriptionManager; // 订阅管理器
    
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final AtomicLong publishedCount = new AtomicLong(0);
    private final AtomicLong processedCount = new AtomicLong(0);
    private final AtomicLong failedCount = new AtomicLong(0);

    public ThreadOptimizedMessageQueue(String path, int processingThreads, int readerThreads) {
        this.queue = SingleChronicleQueueBuilder.binary(path).build();
        this.appender = queue.acquireAppender();
        
        // 统一的处理线程池
        this.processingExecutor = new ThreadPoolExecutor(
            processingThreads, processingThreads, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10000),
            r -> {
                Thread t = new Thread(r, "MessageProcessor");
                t.setDaemon(true);
                return t;
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        this.subscriptionManager = new SubscriptionManager();
        this.messageDispatcher = new MessageDispatcher(subscriptionManager, processingExecutor, processedCount, failedCount);
        this.messageReader = new MessageReader(queue, messageDispatcher, readerThreads);
        
        messageReader.start();
        
        log.info("ThreadOptimizedMessageQueue started: processingThreads={}, readerThreads={}", 
                processingThreads, readerThreads);
    }

    /**
     * 订阅管理器 - 管理所有订阅关系
     */
    private static class SubscriptionManager {
        // 主题 -> 消费者列表
        private final Map<String, List<ConsumerInfo>> topicSubscriptions = new ConcurrentHashMap<>();
        // 消费者组 -> 已处理位置（用于广播模式去重）
        private final Map<String, AtomicLong> groupPositions = new ConcurrentHashMap<>();
        
        public synchronized void addSubscription(String topic, ConsumerGroup group, TaskHandler handler) {
            ConsumerInfo consumer = new ConsumerInfo(topic, group, handler);
            topicSubscriptions.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(consumer);
            
            // 为广播模式的消费者组初始化位置追踪
            if (group.isEnableBroadcast()) {
                groupPositions.putIfAbsent(group.getGroupId(), new AtomicLong(0));
            }
            
            log.info("Added subscription: topic={}, group={}, broadcast={}", 
                    topic, group.getGroupId(), group.isEnableBroadcast());
        }
        
        public List<ConsumerInfo> getConsumersForTopic(String topic) {
            return topicSubscriptions.getOrDefault(topic, Collections.emptyList());
        }
        
        public boolean updateGroupPosition(String groupId, long position) {
            AtomicLong current = groupPositions.get(groupId);
            if (current != null) {
                long currentVal = current.get();
                if (position > currentVal) {
                    return current.compareAndSet(currentVal, position);
                }
            }
            return false;
        }
        
        public long getGroupPosition(String groupId) {
            AtomicLong position = groupPositions.get(groupId);
            return position != null ? position.get() : 0;
        }
        
        public int getTotalSubscriptions() {
            return topicSubscriptions.values().stream().mapToInt(List::size).sum();
        }
        
        public Set<String> getAllTopics() {
            return topicSubscriptions.keySet();
        }
    }

    /**
     * 统一消息读取器 - 使用单线程避免重复读取
     * 
     * 重要：使用单个读取线程和单个Tailer，避免消息被重复处理
     * Chronicle Queue的每个Tailer都会独立读取所有消息，
     * 多个Tailer会导致消息被重复处理多次
     */
    private static class MessageReader {
        private final ChronicleQueue queue;
        private final MessageDispatcher dispatcher;
        private final ExcerptTailer tailer;
        private final AtomicBoolean running = new AtomicBoolean(true);
        private final AtomicLong messageSequence = new AtomicLong(0);
        private final Thread readerThread;

        public MessageReader(ChronicleQueue queue, MessageDispatcher dispatcher, int readerThreadCount) {
            this.queue = queue;
            this.dispatcher = dispatcher;
            
            // 注意：忽略readerThreadCount参数，强制使用单线程
            // 因为多个Tailer会导致消息重复读取
            log.warn("注意：为避免消息重复处理，统一读取器使用单线程模式（忽略readerThreadCount={}）", readerThreadCount);
            
            // 创建单个Tailer
            this.tailer = queue.createTailer("unified-single-reader");
            
            // 创建读取线程
            this.readerThread = new Thread(this::readMessages, "UnifiedReader");
            this.readerThread.setDaemon(true);
        }

        private void readMessages() {
            log.info("启动统一读取线程（单线程模式）");
            
            int emptyReads = 0;
            long messagesRead = 0;
            
            while (running.get()) {
                try (DocumentContext dc = tailer.readingDocument()) {
                    if (!dc.isPresent()) {
                        emptyReads++;
                        // 动态等待策略：没有消息时逐渐增加等待时间
                        long waitTime = Math.min(100, 10 + emptyReads * 5);
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(waitTime));
                        continue;
                    }
                    
                    emptyReads = 0;
                    QueueMessage msg = dc.wire().read("msg").object(QueueMessage.class);
                    
                    // 为消息分配序列号（用于广播模式去重）
                    long sequence = messageSequence.incrementAndGet();
                    messagesRead++;
                    
                    // 分发消息到处理线程池
                    dispatcher.dispatch(msg, sequence);
                    
                    // 定期记录进度
                    if (messagesRead % 10000 == 0) {
                        log.debug("已读取 {} 条消息", messagesRead);
                    }
                    
                } catch (Exception e) {
                    log.error("统一读取器错误", e);
                    LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
                }
            }
            
            log.info("统一读取线程停止，共读取 {} 条消息", messagesRead);
        }

        public void start() {
            readerThread.start();
        }

        public void stop() {
            running.set(false);
            try {
                readerThread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("等待读取线程停止时被中断");
            }
        }
    }

    /**
     * 消息分发器 - 根据订阅关系分发消息
     */
    private static class MessageDispatcher {
        private final SubscriptionManager subscriptionManager;
        private final ExecutorService processingExecutor;
        private final AtomicLong dispatchedCount = new AtomicLong(0);
        private final AtomicLong processedCount;
        private final AtomicLong failedCount;

        public MessageDispatcher(SubscriptionManager subscriptionManager, ExecutorService processingExecutor, 
                                AtomicLong processedCount, AtomicLong failedCount) {
            this.subscriptionManager = subscriptionManager;
            this.processingExecutor = processingExecutor;
            this.processedCount = processedCount;
            this.failedCount = failedCount;
        }

        public void dispatch(QueueMessage msg, long sequence) {
            List<ConsumerInfo> consumers = subscriptionManager.getConsumersForTopic(msg.getTopic());
            
            if (consumers.isEmpty()) {
                return; // 无订阅者
            }
            
            // 按消费者组分组
            Map<String, List<ConsumerInfo>> groupedConsumers = consumers.stream()
                .collect(Collectors.groupingBy(c -> c.getGroup().getGroupId()));
            
            // 处理每个消费者组
            for (Map.Entry<String, List<ConsumerInfo>> entry : groupedConsumers.entrySet()) {
                String groupId = entry.getKey();
                List<ConsumerInfo> groupConsumers = entry.getValue();
                
                if (groupConsumers.isEmpty()) continue;
                
                // 检查消费者组过滤
                if (StrUtil.isNotEmpty(msg.getConsumerGroup()) && 
                    !Objects.equals(msg.getConsumerGroup(), groupId)) {
                    continue;
                }
                
                ConsumerGroup group = groupConsumers.get(0).getGroup();
                
                if (group.isEnableBroadcast()) {
                    // 广播模式：每个消费者都处理（使用序列号去重）
                    long lastPosition = subscriptionManager.getGroupPosition(groupId);
                    if (sequence > lastPosition) {
                        subscriptionManager.updateGroupPosition(groupId, sequence);
                        for (ConsumerInfo consumer : groupConsumers) {
                            processingExecutor.submit(() -> processMessage(msg, consumer));
                            dispatchedCount.incrementAndGet();
                        }
                    }
                } else {
                    // 竞争模式：选择一个消费者处理
                    int selectedIndex = Math.abs(msg.getId().hashCode()) % groupConsumers.size();
                    ConsumerInfo selected = groupConsumers.get(selectedIndex);
                    processingExecutor.submit(() -> processMessage(msg, selected));
                    dispatchedCount.incrementAndGet();
                }
            }
        }

        private void processMessage(QueueMessage msg, ConsumerInfo consumer) {
            try {
                consumer.getHandler().handle(msg);
                processedCount.incrementAndGet();
                log.debug("消息处理成功: {} by {}", msg.getId(), consumer.getGroup().getGroupId());
            } catch (Exception e) {
                failedCount.incrementAndGet();
                log.error("消息处理失败: {} by {}", msg.getId(), consumer.getGroup().getGroupId(), e);
                // 这里可以添加重试逻辑
            }
        }

        public long getDispatchedCount() {
            return dispatchedCount.get();
        }
    }

    /**
     * 消费者信息
     */
    @Getter
    private static class ConsumerInfo {
        private final String topic;
        private final ConsumerGroup group;
        private final TaskHandler handler;
        private final String instanceId;

        public ConsumerInfo(String topic, ConsumerGroup group, TaskHandler handler) {
            this.topic = topic;
            this.group = group;
            this.handler = handler;
            this.instanceId = UUID.randomUUID().toString().substring(0, 8);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConsumerInfo)) return false;
            ConsumerInfo that = (ConsumerInfo) o;
            return Objects.equals(instanceId, that.instanceId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(instanceId);
        }
    }

    /**
     * 订阅主题
     * 不再为每个订阅创建新线程，所有订阅共享统一的读取器
     */
    public void subscribe(String topic, ConsumerGroup consumerGroup, TaskHandler handler) {
        subscriptionManager.addSubscription(topic, consumerGroup, handler);
        log.info("Subscription added without creating new thread: topic={}, group={}", 
                topic, consumerGroup.getGroupId());
    }

    /**
     * 发布消息
     */
    public void publish(QueueMessage msg) {
        appender.writeDocument(w -> w.write("msg").object(msg));
        publishedCount.incrementAndGet();
    }

    /**
     * 获取系统统计
     */
    public SystemStats getSystemStats() {
        return new SystemStats(
            publishedCount.get(),
            processedCount.get(),
            failedCount.get(),
            messageDispatcher.getDispatchedCount(),
            subscriptionManager.getTotalSubscriptions(),
            subscriptionManager.getAllTopics().size()
        );
    }

    @Getter
    public static class SystemStats {
        private final long published;
        private final long processed;
        private final long failed;
        private final long dispatched;
        private final int totalSubscriptions;
        private final int topics;

        public SystemStats(long published, long processed, long failed, 
                          long dispatched, int totalSubscriptions, int topics) {
            this.published = published;
            this.processed = processed;
            this.failed = failed;
            this.dispatched = dispatched;
            this.totalSubscriptions = totalSubscriptions;
            this.topics = topics;
        }

        @Override
        public String toString() {
            return String.format("Stats{pub:%d, proc:%d, fail:%d, dispatch:%d, subs:%d, topics:%d}",
                    published, processed, failed, dispatched, totalSubscriptions, topics);
        }
    }

    /**
     * 关闭队列
     */
    public void shutdown() {
        log.info("关闭线程优化版消息队列...");
        
        isRunning.set(false);
        messageReader.stop();
        processingExecutor.shutdown();
        
        try {
            if (!processingExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                processingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            processingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        queue.close();
        log.info("线程优化版消息队列已关闭");
    }
}