package com.redjujubetree.fs.service;

import com.alibaba.fastjson.JSON;
import com.redjujubetree.fs.domain.StreamContext;
import com.redjujubetree.fs.domain.dto.ChunkUploadResult;
import com.redjujubetree.fs.domain.dto.StorageSessionDto;
import com.redjujubetree.fs.enums.StorageSessionStatus;
import com.redjujubetree.fs.grpc.FileTransferUploadProto.*;
import com.redjujubetree.fs.grpc.FileTransferUploadServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * 重构后的文件传输服务实现
 * 基于数据库的会话管理，移除内存管理
 */
@Slf4j
@GrpcService
public class FileTransferUploadService extends FileTransferUploadServiceGrpc.FileTransferUploadServiceImplBase {

    @Resource
    private FileStorageService storageService;

    @Resource
    private StorageSessionService sessionService;

    // 活跃流管理 - 仅用于gRPC流的生命周期管理
    private final ConcurrentHashMap<String, StreamObserver<UploadChunkResponse>> activeStreams = new ConcurrentHashMap<>();

    // 异步处理线程池
    private ExecutorService chunkProcessorPool;

    @PostConstruct
    public void init() {
        this.chunkProcessorPool = Executors.newFixedThreadPool(50, r -> {
            Thread t = new Thread(r, "chunk-processor");
            t.setDaemon(true);
            return t;
        });
        log.info("FileTransferService initialized with storage type: {}", storageService.getStorageType());
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutting down FileTransferService...");
        shutdownExecutorService(chunkProcessorPool);
        log.info("FileTransferService shutdown completed");
    }

    @Override
    public void initiateUpload(InitiateUploadRequest request, StreamObserver<InitiateUploadResponse> responseObserver) {
        try {
            // 参数验证
            if (!validateInitiateRequest(request)) {
                sendInitiateError(responseObserver, "Invalid request parameters");
                return;
            }

            log.info("Initiating upload - File: {}, Size: {} bytes, Client: {}",
                       request.getFileName(), request.getFileSize(), request.getClientId());

            // 生成uploadId（基于文件特征的确定性ID）
            String uploadId = generateUploadId(request);

            // 检查是否已存在会话（从数据库）
            Optional<StorageSessionDto> existingSession = sessionService.getSession(uploadId);
            if (existingSession.isPresent()) {
                StorageSessionDto session = existingSession.get();

                // 检查会话状态
                if (session.getStatus() == StorageSessionStatus.COMPLETED) {
                    sendInitiateError(responseObserver, "File already uploaded successfully");
                    return;
                }

                if (session.getStatus() == StorageSessionStatus.ACTIVE) {
                    log.info("Resuming existing upload session: {}", uploadId);

                    // 更新最后访问时间
                    sessionService.updateLastAccess(uploadId);

                    // 获取已上传的分片
                    List<Integer> uploadedChunks = sessionService.getUploadedChunks(uploadId);

                    sendInitiateSuccess(responseObserver, session, uploadedChunks);
                    return;
                }

                // 如果状态是 FAILED 或 EXPIRED，清理旧会话，创建新的
                if (session.getStatus() == StorageSessionStatus.FAILED ||
                    session.getStatus() == StorageSessionStatus.EXPIRED) {
                    log.info("Cleaning up old session with status: {}", session.getStatus());
                    sessionService.deleteSession(uploadId);
                    // 继续创建新会话
                }
            }

            // 创建新的上传会话
            log.info("Creating new upload session: {}", uploadId);

            // 准备会话元数据
            Map<String, String> sessionMetadata = new HashMap<>();
            sessionMetadata.put("client_id", request.getClientId());
            sessionMetadata.put("original_name", request.getFileName());
            sessionMetadata.put("upload_time", String.valueOf(System.currentTimeMillis()));

            // 创建会话（会生成新的storageId）
            StorageSessionDto session = sessionService.createSession(
                uploadId,                    // 使用uploadId作为主键
                request.getFileName(),
                request.getFileSize(),
                request.getFileHash(),
                sessionMetadata
            );

            if (session == null) {
                sendInitiateError(responseObserver, "Failed to create upload session");
                return;
            }

            // 初始化存储层
            boolean storageInitialized = storageService.initializeStorage(
                session.getStorageId(),      // 使用session中的storageId
                request.getFileName(),
                request.getFileSize(),
                request.getFileHash(),
                sessionMetadata
            );

            if (!storageInitialized) {
                // 存储初始化失败，清理数据库会话
                sessionService.deleteSession(uploadId);
                sendInitiateError(responseObserver, "Failed to initialize storage");
                return;
            }

            // 发送成功响应
            sendInitiateSuccess(responseObserver, session, new ArrayList<>());

            log.info("Upload session created successfully - ID: {}, Storage: {}, Total chunks: {}",
                       uploadId, session.getStorageId(), session.getTotalChunks());

        } catch (Exception e) {
            log.error("Error creating upload session for file: {}", request.getFileName(), e);
            sendInitiateError(responseObserver, "Failed to create upload session: " + e.getMessage());
        }
    }

    @Override
    public StreamObserver<UploadChunkRequest> uploadChunk(StreamObserver<UploadChunkResponse> responseObserver) {
        final String streamId = java.util.UUID.randomUUID().toString();
        StreamContext context = new StreamContext(streamId);
        log.info("Created new upload stream: {}", streamId);
        ServerCallStreamObserver<UploadChunkResponse> serverObserver =
                (ServerCallStreamObserver<UploadChunkResponse>) responseObserver;

        serverObserver.setOnCancelHandler(() -> {
            log.warn("Client cancelled the upload call. Cleaning up. Stream {}", streamId);
            context.markCancelled();
            activeStreams.remove(streamId);
        });

        return new StreamObserver<UploadChunkRequest>() {
            private String uploadId;
            @Override
            public void onNext(UploadChunkRequest request) {
                if (!context.isActive()) {
                    log.warn("Received chunk for inactive stream: {}", streamId);
                    return;
                }

                if (chunkProcessorPool.isShutdown()) {
                    log.error("Chunk processor pool is shutdown. Cannot process chunk for stream: {}", streamId);
                    handleStreamError(context, serverObserver, new RejectedExecutionException("Upload processor pool is shutdown"));
                    return;
                }

                context.incrementPendingChunks();

				try {
					CompletableFuture<UploadChunkResponse> futureResponse = CompletableFuture.supplyAsync(() -> {
						log.info("Processing chunk on thread {} - Upload: {}, Stream: {}, Chunk Index: {}",
								Thread.currentThread().getName(), request.getUploadId(), streamId, request.getChunkIndex());
						try {
							return processChunk(request,streamId);
						} catch (Exception e) {
                            log.error("Error processing chunk for stream {}", streamId, e);
							throw new RuntimeException(e);
						}
					}, chunkProcessorPool);

					futureResponse.whenComplete((response, throwable) -> {
						try {
							if (throwable != null) {
								log.error("Error processing chunk - Upload: {}, Stream: {}, Chunk Index: {}",
										request.getUploadId(), streamId, request.getChunkIndex(), throwable);
								handleStreamError(context,serverObserver, throwable);
							} else {
                                sendResponse(response);
							}
						} finally {
							context.decrementPendingChunks();
						}
					});
				} catch (Exception e) {
					log.error("Error processing chunk - Upload: {}, Stream: {}, Chunk Index: {}",
                            request.getUploadId(), streamId, request.getChunkIndex(), e);
                    context.decrementPendingChunks();
                    handleStreamError(context, serverObserver, e);
				}
			}

            @Override
            public void onError(Throwable t) {
                context.decrementPendingChunks();
                log.error("Upload chunk stream error - Upload: {}, Stream: {}", uploadId, streamId, t);
                activeStreams.remove(streamId);
            }

            @Override
            public void onCompleted() {
                log.info("Client completed upload chunk stream - Upload: {}, Stream: {}", uploadId, streamId);
                activeStreams.remove(streamId);
                context.markClientCompleted();
                try {
                    chunkProcessorPool.submit(() -> {
						try {
                            boolean completed = context.waitForCompletion(60, TimeUnit.SECONDS);
                            if (context.isActive() && completed) {
                                log.info("Upload chunks stream completed successfully - Upload: {}, Stream: {}", uploadId, streamId);
								try {
									serverObserver.onCompleted();
                                    // TODO Add auto complete logic if need
								} catch (Exception e) {
									log.error("Error completing response stream - Upload: {}, Stream: {}", uploadId, streamId, e);
								}
							} else if (!completed) {
                                log.warn("Upload chunks stream did not complete in time - Upload: {}, Stream: {}", uploadId, streamId);
                                if (context.isActive()) {
                                    try {
                                        serverObserver.onError(Status.DEADLINE_EXCEEDED.withDescription("Timeout when processing upload chunks")
                                                .asRuntimeException());
                                    } catch (IllegalStateException e) {
                                        log.warn("Stream already cancelled or completed when sending timeout error - Upload: {}, Stream: {}", uploadId, streamId, e);
                                    }
                                }
                            }
						} catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            log.warn("Interrupted while waiting for upload chunks stream completion - Upload: {}, Stream: {}", uploadId, streamId, e);
                            if (context.isActive()) {
								try {
									serverObserver.onError(Status.CANCELLED.withDescription("Processing interrupted").asRuntimeException());
								} catch (Exception ex) {
									log.warn("Stream already cancelled or completed when sending interrupt error - Upload: {}, Stream: {}", uploadId, streamId);
								}
							}
						} finally {
                            context.markCancelled();
                            activeStreams.remove(streamId);
                            // 清理资源
                        }

					});
                } catch (RejectedExecutionException e) {
                    log.error("Completion task rejected - Upload: {}, Stream: {}", uploadId, streamId);
                    handleStreamError(context, serverObserver, e);
                } catch (Exception e) {
                    log.error("Error completing response stream: {}", streamId, e);
                }
            }

            private void handleStreamError(StreamContext context, ServerCallStreamObserver<UploadChunkResponse> observer, Throwable e) {
                if (context.isActive() && !context.setCompleted(true)) {
                    log.error("Stream already completed by other thread: streamId {}", context.getStreamId());
                    return;
                }
                activeStreams.remove(context.getStreamId());
                observer.onError(Status.INTERNAL.withDescription("Error processing upload chunks : " + e.getMessage()).asRuntimeException());
			}

            private UploadChunkResponse processChunk(UploadChunkRequest request, String streamId) {
                // 实现具体地分片处理逻辑
                // 这里可以包括：写入文件、验证校验和、更新进度等
                    try {
                        uploadId = request.getUploadId();
                        activeStreams.put(streamId, responseObserver);
                        // 延迟初始化会话 - 从数据库查询
                        Optional<StorageSessionDto> sessionOpt = sessionService.getSession(uploadId);
                        if (!sessionOpt.isPresent()) {
                            throw new Exception("Upload session not found or expired");
                        }

                        StorageSessionDto session = sessionOpt.get();
                        // 检查会话状态
                        if (session.getStatus() != StorageSessionStatus.ACTIVE) {
                           throw new Exception("Upload session is not active: " + session.getStatus());
                        }
                        // 验证分片参数
                        if (!validateChunkRequest(request, session)) {
                            throw new Exception("Upload session is not active: " + session.getStatus());
                        }

                        // 检查分片是否已存在
                        if (sessionService.chunkExists(uploadId, request.getChunkIndex())) {
                            log.debug("Chunk {} already exists for upload: {}", request.getChunkIndex(), uploadId);
                            UploadChunkResponse response = UploadChunkResponse.newBuilder()
                                    .setSuccess(true)
                                    .setUploadId(uploadId)
                                    .setChunkIndex(request.getChunkIndex())
                                    .setMessage("Chunk already exists")
                                    .setReceivedTimestamp(System.currentTimeMillis())
                                    .build();
                            return response;
                        }

                        // 委托给存储服务处理分片
                        FileStorageService.ChunkWriteResult result = storageService.writeChunk(
                                session.getStorageId(),
                                request.getChunkIndex(),
                                request.getChunkOffset(),
                                request.getChunkData().toByteArray(),
                                request.getChunkHash()
                        );

                        // 记录分片上传到数据库
                        if (result.isSuccess()) {
                            ChunkUploadResult chunkResult = sessionService.recordChunkUpload(
                                    uploadId,
                                    request.getChunkIndex(),
                                    (long) request.getChunkSize(),
                                    request.getChunkHash(),
                                    request.getChunkOffset()
                            );

                            if (!chunkResult.isSuccess()) {
                                log.warn("Failed to record chunk upload in database: {}", chunkResult.getMessage());
                            }
                        }

                        return UploadChunkResponse.newBuilder()
                                .setSuccess(result.isSuccess())
                                .setUploadId(request.getUploadId())
                                .setChunkIndex(request.getChunkIndex())
                                .setMessage(result.getMessage())
                                .setReceivedTimestamp(System.currentTimeMillis())
                                .build();
                    } catch (Exception e) {
                        log.error("Error processing chunk {} for upload: {}, stream: {}",
                                request.getChunkIndex(), uploadId, streamId, e);
                       throw new RuntimeException(e);
                    }
            }

            private void sendResponse(UploadChunkResponse response) {
                try {
                    if (!context.isActive()) {
                        log.warn("Skip sending response for inactive stream: {}", streamId);
                        return;
                    }
                    if (serverObserver.isCancelled()) {
                        log.warn("Client already cancelled. Stop sending.");
                        return;
                    }
                    log.info("UploadChunkResponse {}", JSON.toJSONString(response));
                    serverObserver.onNext(response);
                }catch (IllegalStateException e) {
                    log.error("Error sending response for chunk {} in stream: {}",
                            response.getChunkIndex(), streamId, e);
                    context.markCompleted();
                    activeStreams.remove(streamId);
                } catch (Exception e) {
                    log.error("Error sending response for chunk {} in stream: {}",
                            response.getChunkIndex(), streamId, e);
                    handleStreamError(context, serverObserver, e);
                }
            }
        };
    }

    @Override
    public void getUploadStatus(GetUploadStatusRequest request, StreamObserver<GetUploadStatusResponse> responseObserver) {
        try {
            String uploadId = request.getUploadId();
            
            // 从数据库获取会话信息
            Optional<StorageSessionDto> sessionOpt = sessionService.getSession(uploadId);
            if (!sessionOpt.isPresent()) {
                sendStatusError(responseObserver, "Upload session not found or expired");
                return;
            }
            
            StorageSessionDto session = sessionOpt.get();
            
            // 获取已上传的分片信息
            List<Integer> uploadedChunks = sessionService.getUploadedChunks(uploadId);
            List<Integer> missingChunks = sessionService.getMissingChunks(uploadId);
            double progress = sessionService.getUploadProgress(uploadId);
            
            GetUploadStatusResponse response = GetUploadStatusResponse.newBuilder()
                    .setSuccess(true)
                    .setUploadId(uploadId)
                    .setStatus(session.getStatus().name())
                    .addAllUploadedChunks(uploadedChunks)
                    .addAllMissingChunks(missingChunks)
                    .setTotalChunks(session.getTotalChunks())
                    .setUploadedCount(uploadedChunks.size())
                    .setProgress(progress / 100.0) // 转换为0.0-1.0范围
                    .setMessage("Status retrieved successfully")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            // 更新最后访问时间
            sessionService.updateLastAccess(uploadId);
            
            log.debug("Status retrieved for upload: {}, progress: {:.2f}%, uploaded: {}/{}",
                        uploadId, progress, uploadedChunks.size(), session.getTotalChunks());
            
        } catch (Exception e) {
            log.error("Error getting upload status for: {}", request.getUploadId(), e);
            sendStatusError(responseObserver, "Failed to get upload status: " + e.getMessage());
        }
    }
    
    @Override
    public void completeUpload(CompleteUploadRequest request, StreamObserver<CompleteUploadResponse> responseObserver) {
        try {
            String uploadId = request.getUploadId();
            
            // 从数据库获取会话信息
            Optional<StorageSessionDto> sessionOpt = sessionService.getSession(uploadId);
            if (!sessionOpt.isPresent()) {
                sendCompleteError(responseObserver, "Upload session not found or expired");
                return;
            }
            
            StorageSessionDto session = sessionOpt.get();
            
            log.info("Attempting to complete upload: {}", uploadId);
            
            // 检查会话状态
            if (session.getStatus() != StorageSessionStatus.ACTIVE) {
                sendCompleteError(responseObserver, 
                    "Upload session is not in active state: " + session.getStatus());
                return;
            }
            
            // 检查上传完整性
            if (!sessionService.isUploadComplete(uploadId)) {
                List<Integer> missingChunks = sessionService.getMissingChunks(uploadId);
                log.warn("Upload incomplete for: {}, missing {} chunks", uploadId, missingChunks.size());
                
                sendCompleteError(responseObserver, 
                    String.format("Upload incomplete. Missing %d chunks: %s", 
                                 missingChunks.size(), missingChunks));
                return;
            }
            
            // 更新会话状态为完成中
            sessionService.updateSessionStatus(uploadId, StorageSessionStatus.COMPLETING);
            
            try {
                // 委托给存储服务完成上传
                FileStorageService.FileCompleteResult result = storageService.completeUpload(session.getStorageId());
                
                if (result.isSuccess()) {
                    // 更新会话状态为已完成
                    sessionService.updateSessionStatus(uploadId, StorageSessionStatus.COMPLETED);
                    
                    CompleteUploadResponse response = CompleteUploadResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("File upload completed successfully")
                            .setFilePath(result.getFinalPath())
                            .setFileHash(result.getActualChecksum())
                            .build();
                    
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    
                    log.info("Upload completed successfully - ID: {}, Path: {}, Hash verified: {}",
                               uploadId, result.getFinalPath(), 
                               result.getActualChecksum().equals(session.getExpectedChecksum()));
                } else {
                    // 完成失败，恢复为活跃状态
                    sessionService.updateSessionStatus(uploadId, StorageSessionStatus.ACTIVE);
                    sendCompleteError(responseObserver, result.getMessage());
                }
                
            } catch (Exception e) {
                // 异常时恢复会话状态
                sessionService.updateSessionStatus(uploadId, StorageSessionStatus.ACTIVE);
                throw e;
            }
            
        } catch (Exception e) {
            log.error("Error completing upload: {}", request.getUploadId(), e);
            sendCompleteError(responseObserver, "Failed to complete upload: " + e.getMessage());
        }
    }
    
    @Override
    public void cancelUpload(CancelUploadRequest request, StreamObserver<CancelUploadResponse> responseObserver) {
        try {
            String uploadId = request.getUploadId();
            
            log.info("Cancelling upload: {}", uploadId);
            
            // 从数据库获取会话信息
            Optional<StorageSessionDto> sessionOpt = sessionService.getSession(uploadId);
            
            boolean success = false;
            String message;
            
            if (sessionOpt.isPresent()) {
                StorageSessionDto session = sessionOpt.get();
                
                // 更新会话状态
                sessionService.updateSessionStatus(uploadId, StorageSessionStatus.CANCELLED);
                
                // 委托给存储服务取消上传
                success = storageService.cancelUpload(session.getStorageId());
                
                if (success) {
                    // 删除会话记录
                    sessionService.deleteSession(uploadId);
                    message = "Upload cancelled successfully";
                } else {
                    message = "Failed to cancel upload in storage, but session marked as cancelled";
                }
            } else {
                message = "Upload session not found";
            }
            
            CancelUploadResponse response = CancelUploadResponse.newBuilder()
                    .setSuccess(success)
                    .setMessage(message)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("Upload cancellation result - ID: {}, Success: {}", uploadId, success);
            
        } catch (Exception e) {
            log.error("Error cancelling upload: {}", request.getUploadId(), e);
            
            CancelUploadResponse response = CancelUploadResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error cancelling upload: " + e.getMessage())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    
    private void sendInitiateSuccess(StreamObserver<InitiateUploadResponse> responseObserver,
                                   StorageSessionDto session, List<Integer> uploadedChunks) {
        try {
            InitiateUploadResponse response = InitiateUploadResponse.newBuilder()
                    .setSuccess(true)
                    .setUploadId(session.getStorageId()) // 使用storageId作为uploadId返回
                    .setMessage("Upload session created/resumed successfully")
                    .setTotalChunks(session.getTotalChunks())
                    .setChunkSize(session.getChunkSize())
                    .addAllUploadedChunks(uploadedChunks)
                    .build();
            log.info("InitiateUploadResponse {}", JSON.toJSONString(response));
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error sending initiate success response", e);
            sendInitiateError(responseObserver, "Error retrieving upload status");
        }
    }

    private String generateUploadId(InitiateUploadRequest request) {
        String input = request.getFileName() + "_" + request.getFileSize() + "_" + 
                      request.getFileHash() + "_" + request.getClientId();
        return calculateMD5(input.getBytes());
    }
    
    private String calculateMD5(byte[] data) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
    
    private boolean validateInitiateRequest(InitiateUploadRequest request) {
		request.getFileName();
		if (request.getFileName().trim().isEmpty() || request.getFileSize() <= 0) return false;
		request.getClientId();
		if (request.getClientId().trim().isEmpty()) return false;
		request.getFileHash();
		return !request.getFileHash().trim().isEmpty();
    }
    
    private boolean validateChunkRequest(UploadChunkRequest request, StorageSessionDto session) {
		if (request.getChunkIndex() < 0 || request.getChunkIndex() >= session.getTotalChunks() || request.getChunkOffset() < 0 || request.getChunkSize() <= 0 || request.getChunkData().size() != request.getChunkSize())
			return false;
		request.getChunkHash();
		return !request.getChunkHash().trim().isEmpty();
    }
    
    private void shutdownExecutorService(ExecutorService executor) {
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    log.warn("Forcing shutdown of {} executor", "chunk processor");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.warn("Interrupted while shutting down {} executor", "chunk processor");
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    // 错误响应方法
    private void sendInitiateError(StreamObserver<InitiateUploadResponse> responseObserver, String message) {
        InitiateUploadResponse response = InitiateUploadResponse.newBuilder()
                .setSuccess(false)
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    private void sendStatusError(StreamObserver<GetUploadStatusResponse> responseObserver, String message) {
        GetUploadStatusResponse response = GetUploadStatusResponse.newBuilder()
                .setSuccess(false)
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    private void sendCompleteError(StreamObserver<CompleteUploadResponse> responseObserver, String message) {
        CompleteUploadResponse response = CompleteUploadResponse.newBuilder()
                .setSuccess(false)
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}