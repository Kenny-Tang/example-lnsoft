

# HashMap

**哈希表：** 在哈希表中进行查找、添加和删除等操作性能十分高，在不考虑哈希冲突的情况下，仅需要一次定位即可完成，时间复杂度为O(1)。

HashMap 的实现

在JDK1.7 中 HashMap 是基于数组和链表实现，在哈希冲突较多的情况下索引效率较慢；

在JDK1.8 中HashMap 其实现方式修改为数组 + 链表 + 红黑树。

JDK1.7 事项方式如下图
![JDK 1.7](https://i-blog.csdnimg.cn/blog_migrate/774fd31628bef6acf5597c71d9b23f09.png)
JDK 1.8 实现方式如下图
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/1bebb558bcca7215fc2258ba3e0278a3.png)
## HashMap.put
- 根据 key 的hash值进行与运算，计算出元素在数组中存放的位置
- 如果如果该位置为空，则放入
	1. 在JDK1.7 中插入方式使用`头插法`，
	2. 在JDK1.8 中需要先判断当前位置的结构类型
	   1）如果是一刻红黑树，则将数据封装成红黑树节点，然后将节点加入树中
	   2）如果是链表， 则封装成一个链表节点，采用 `尾插法` 将该节点插入队尾，插入后如果节点数到达阈值`8 （0.00000006）`，则将链表转换为一棵`红黑树`。
	3. 数据插入后判断是否需要进行扩容如果需要则进行扩容处理，每次扩容，数组大小升级为原来的`两倍`。

### HashMap.hash()
```java 
static final int hash(Object key) {
   int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
1. 首先获取对象的hashCode值，然后将hashCode的值右移16位，然后将将右移后的值与原来的hashCode做异或运算，返回结果。（其中h>>>16，在JDK1.8中，优化了高位运算的算法，使用了零扩展，无论正数还是负数，都在高位插入0）。

2. 在putVal源码中，我们通过(n-1)&hash获取该对象的键在hashmap中的位置。（其中hash的值就是（1）中获得的值）其中n表示的是hash桶数组的长度，并且该长度为2的n次方，这样(n-1)&hash就等价于hash%n。因为&运算的效率高于%运算。
   源码如下
```java
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
				   boolean evict) {
	Node<K,V>[] tab; Node<K,V> p; int n, i;
	if ((tab = table) == null || (n = tab.length) == 0)
		n = (tab = resize()).length;
	if ((p = tab[i = (n - 1) & hash]) == null)
		tab[i] = newNode(hash, key, value, null);
	else {
		Node<K,V> e; K k;
		if (p.hash == hash &&
				((k = p.key) == key || (key != null && key.equals(k))))
			e = p;
		else if (p instanceof TreeNode)
			e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
		else {
			for (int binCount = 0; ; ++binCount) {
				if ((e = p.next) == null) {
					p.next = newNode(hash, key, value, null);
					if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
						treeifyBin(tab, hash);
					break;
				}
				if (e.hash == hash &&
						((k = e.key) == key || (key != null && key.equals(k))))
					break;
				p = e;
			}
		}
		if (e != null) { // existing mapping for key
			V oldValue = e.value;
			if (!onlyIfAbsent || oldValue == null)
				e.value = value;
			afterNodeAccess(e);
			return oldValue;
		}
	}
	++modCount;
	if (++size > threshold)
		resize();
	afterNodeInsertion(evict);
	return null;
}
```
下面红色部分为通过与运算取模获取元素在数组中位值的逻辑
> if ((p = tab[`i = (n - 1) & hash`]) == null)
&nbsp;&nbsp;&nbsp;&nbsp;tab[i] = newNode(hash, key, value, null);

前面我们提高数组的长度必须是2的幂数即每次扩容为原来的两倍，原因就在这里，这样可以保证每次和`n-1` 的与运算高位都是0，低位都不会发生变化。

流程如下
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ab67e7a28efc07415563268db157e5cf.png)

# HashMap线程不安全

JDK1.7 HashMap线程不安全体现在：死循环、数据丢失

JDK1.8 HashMap线程不安全体现在：数据覆盖

`putVal` 其中第六行代码是判断是否出现hash碰撞，假设两个线程A、B都在进行put操作，并且hash函数计算出的插入下标是相同的，当线程A执行完第六行代码后由于时间片耗尽导致被挂起，而线程B得到时间片后在该下标处插入了元素，完成了正常的插入，然后线程A获得时间片，由于之前已经进行了hash碰撞的判断，所有此时不会再进行判断，而是直接进行插入，这就导致了线程B插入的数据被线程A覆盖了，从而线程不安全。

除此之前，还有就是代码的第38行处有个++size，我们这样想，还是线程A、B，这两个线程同时进行put操作时，假设当前HashMap的zise大小为10，当线程A执行到第38行代码时，从主内存中获得size的值为10后准备进行+1操作，但是由于时间片耗尽只好让出CPU，线程B快乐的拿到CPU还是从主内存中拿到size的值10进行+1操作，完成了put操作并将size=11写回主内存，然后线程A再次拿到CPU并继续执行(此时size的值仍为10)，当执行完put操作后，还是将size=11写回内存，此时，线程A、B都执行了一次put操作，但是size的值只增加了1，所有说还是由于数据覆盖又导致了线程不安全。


# ConcurrentHashMap

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    // 得到 hash 值
    int hash = spread(key.hashCode());
    // 用于记录相应链表的长度
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        // 如果数组"空"，进行数组初始化
        if (tab == null || (n = tab.length) == 0)
            // 初始化数组，后面会详细介绍
            tab = initTable();

        // 找该 hash 值对应的数组下标，得到第一个节点 f
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // 如果数组该位置为空，
            //    用一次 CAS 操作将这个新值放入其中即可，这个 put 操作差不多就结束了，可以拉到最后面了
            //          如果 CAS 失败，那就是有并发操作，进到下一个循环就好了
            if (casTabAt(tab, i, null,
                         new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        // hash 居然可以等于 MOVED，这个需要到后面才能看明白，不过从名字上也能猜到，肯定是因为在扩容
        else if ((fh = f.hash) == MOVED)
            // 帮助数据迁移，这个等到看完数据迁移部分的介绍后，再理解这个就很简单了
            tab = helpTransfer(tab, f);

        else { // 到这里就是说，f 是该位置的头节点，而且不为空

            V oldVal = null;
            // 获取数组该位置的头节点的监视器锁
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) { // 头节点的 hash 值大于 0，说明是链表
                        // 用于累加，记录链表的长度
                        binCount = 1;
                        // 遍历链表
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            // 如果发现了"相等"的 key，判断是否要进行值覆盖，然后也就可以 break 了
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            // 到了链表的最末端，将这个新值放到链表的最后面
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                    else if (f instanceof TreeBin) { // 红黑树
                        Node<K,V> p;
                        binCount = 2;
                        // 调用红黑树的插值方法插入新节点
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                       value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }

            if (binCount != 0) {
                // 判断是否要将链表转换为红黑树，临界值和 HashMap 一样，也是 8
                if (binCount >= TREEIFY_THRESHOLD)
                    // 这个方法和 HashMap 中稍微有一点点不同，那就是它不是一定会进行红黑树转换，
                    // 如果当前数组的长度小于 64，那么会选择进行数组扩容，而不是转换为红黑树
                    //    具体源码我们就不看了，扩容部分后面说
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    // 
    addCount(1L, binCount);
    return null;
}
```

在JDK1.7之前，ConcurrentHashMap是通过分段锁机制来实现的，所以其最大并发度受Segment的个数限制。

因此，在JDK1.8中，ConcurrentHashMap的实现原理摒弃了这种设计，而是选择了与HashMap类似的数组+链表+红黑树的方式实现，而加锁则采用CAS和synchronized实现。
