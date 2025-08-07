package com.redjujubetree.fs.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: file_transfer_upload.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FileTransferUploadServiceGrpc {

  private FileTransferUploadServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "file.transfer.upload.FileTransferUploadService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse> getInitiateUploadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InitiateUpload",
      requestType = com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse> getInitiateUploadMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse> getInitiateUploadMethod;
    if ((getInitiateUploadMethod = FileTransferUploadServiceGrpc.getInitiateUploadMethod) == null) {
      synchronized (FileTransferUploadServiceGrpc.class) {
        if ((getInitiateUploadMethod = FileTransferUploadServiceGrpc.getInitiateUploadMethod) == null) {
          FileTransferUploadServiceGrpc.getInitiateUploadMethod = getInitiateUploadMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InitiateUpload"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferUploadServiceMethodDescriptorSupplier("InitiateUpload"))
              .build();
        }
      }
    }
    return getInitiateUploadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse> getUploadChunkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UploadChunk",
      requestType = com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse> getUploadChunkMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse> getUploadChunkMethod;
    if ((getUploadChunkMethod = FileTransferUploadServiceGrpc.getUploadChunkMethod) == null) {
      synchronized (FileTransferUploadServiceGrpc.class) {
        if ((getUploadChunkMethod = FileTransferUploadServiceGrpc.getUploadChunkMethod) == null) {
          FileTransferUploadServiceGrpc.getUploadChunkMethod = getUploadChunkMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UploadChunk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferUploadServiceMethodDescriptorSupplier("UploadChunk"))
              .build();
        }
      }
    }
    return getUploadChunkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse> getGetUploadStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUploadStatus",
      requestType = com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse> getGetUploadStatusMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse> getGetUploadStatusMethod;
    if ((getGetUploadStatusMethod = FileTransferUploadServiceGrpc.getGetUploadStatusMethod) == null) {
      synchronized (FileTransferUploadServiceGrpc.class) {
        if ((getGetUploadStatusMethod = FileTransferUploadServiceGrpc.getGetUploadStatusMethod) == null) {
          FileTransferUploadServiceGrpc.getGetUploadStatusMethod = getGetUploadStatusMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUploadStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferUploadServiceMethodDescriptorSupplier("GetUploadStatus"))
              .build();
        }
      }
    }
    return getGetUploadStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse> getCompleteUploadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CompleteUpload",
      requestType = com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse> getCompleteUploadMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse> getCompleteUploadMethod;
    if ((getCompleteUploadMethod = FileTransferUploadServiceGrpc.getCompleteUploadMethod) == null) {
      synchronized (FileTransferUploadServiceGrpc.class) {
        if ((getCompleteUploadMethod = FileTransferUploadServiceGrpc.getCompleteUploadMethod) == null) {
          FileTransferUploadServiceGrpc.getCompleteUploadMethod = getCompleteUploadMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CompleteUpload"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferUploadServiceMethodDescriptorSupplier("CompleteUpload"))
              .build();
        }
      }
    }
    return getCompleteUploadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse> getCancelUploadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelUpload",
      requestType = com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest,
      com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse> getCancelUploadMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse> getCancelUploadMethod;
    if ((getCancelUploadMethod = FileTransferUploadServiceGrpc.getCancelUploadMethod) == null) {
      synchronized (FileTransferUploadServiceGrpc.class) {
        if ((getCancelUploadMethod = FileTransferUploadServiceGrpc.getCancelUploadMethod) == null) {
          FileTransferUploadServiceGrpc.getCancelUploadMethod = getCancelUploadMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest, com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelUpload"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferUploadServiceMethodDescriptorSupplier("CancelUpload"))
              .build();
        }
      }
    }
    return getCancelUploadMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FileTransferUploadServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileTransferUploadServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileTransferUploadServiceStub>() {
        @java.lang.Override
        public FileTransferUploadServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileTransferUploadServiceStub(channel, callOptions);
        }
      };
    return FileTransferUploadServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FileTransferUploadServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileTransferUploadServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileTransferUploadServiceBlockingStub>() {
        @java.lang.Override
        public FileTransferUploadServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileTransferUploadServiceBlockingStub(channel, callOptions);
        }
      };
    return FileTransferUploadServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FileTransferUploadServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileTransferUploadServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileTransferUploadServiceFutureStub>() {
        @java.lang.Override
        public FileTransferUploadServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileTransferUploadServiceFutureStub(channel, callOptions);
        }
      };
    return FileTransferUploadServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * 初始化上传会话
     * </pre>
     */
    default void initiateUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitiateUploadMethod(), responseObserver);
    }

    /**
     * <pre>
     * 并发上传分片（支持多个并发流）
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest> uploadChunk(
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getUploadChunkMethod(), responseObserver);
    }

    /**
     * <pre>
     * 查询已上传的分片状态
     * </pre>
     */
    default void getUploadStatus(com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUploadStatusMethod(), responseObserver);
    }

    /**
     * <pre>
     * 完成上传（合并文件）
     * </pre>
     */
    default void completeUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCompleteUploadMethod(), responseObserver);
    }

    /**
     * <pre>
     * 取消上传
     * </pre>
     */
    default void cancelUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelUploadMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FileTransferUploadService.
   */
  public static abstract class FileTransferUploadServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FileTransferUploadServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FileTransferUploadService.
   */
  public static final class FileTransferUploadServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FileTransferUploadServiceStub> {
    private FileTransferUploadServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferUploadServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileTransferUploadServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 初始化上传会话
     * </pre>
     */
    public void initiateUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitiateUploadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 并发上传分片（支持多个并发流）
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest> uploadChunk(
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getUploadChunkMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * 查询已上传的分片状态
     * </pre>
     */
    public void getUploadStatus(com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUploadStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 完成上传（合并文件）
     * </pre>
     */
    public void completeUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCompleteUploadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 取消上传
     * </pre>
     */
    public void cancelUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelUploadMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FileTransferUploadService.
   */
  public static final class FileTransferUploadServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FileTransferUploadServiceBlockingStub> {
    private FileTransferUploadServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferUploadServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileTransferUploadServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 初始化上传会话
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse initiateUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitiateUploadMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 查询已上传的分片状态
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse getUploadStatus(com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUploadStatusMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 完成上传（合并文件）
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse completeUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCompleteUploadMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 取消上传
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse cancelUpload(com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelUploadMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FileTransferUploadService.
   */
  public static final class FileTransferUploadServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FileTransferUploadServiceFutureStub> {
    private FileTransferUploadServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferUploadServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileTransferUploadServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 初始化上传会话
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse> initiateUpload(
        com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitiateUploadMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 查询已上传的分片状态
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse> getUploadStatus(
        com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUploadStatusMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 完成上传（合并文件）
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse> completeUpload(
        com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCompleteUploadMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 取消上传
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse> cancelUpload(
        com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelUploadMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INITIATE_UPLOAD = 0;
  private static final int METHODID_GET_UPLOAD_STATUS = 1;
  private static final int METHODID_COMPLETE_UPLOAD = 2;
  private static final int METHODID_CANCEL_UPLOAD = 3;
  private static final int METHODID_UPLOAD_CHUNK = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INITIATE_UPLOAD:
          serviceImpl.initiateUpload((com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse>) responseObserver);
          break;
        case METHODID_GET_UPLOAD_STATUS:
          serviceImpl.getUploadStatus((com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse>) responseObserver);
          break;
        case METHODID_COMPLETE_UPLOAD:
          serviceImpl.completeUpload((com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse>) responseObserver);
          break;
        case METHODID_CANCEL_UPLOAD:
          serviceImpl.cancelUpload((com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPLOAD_CHUNK:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.uploadChunk(
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getInitiateUploadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadRequest,
              com.redjujubetree.fs.grpc.FileTransferUploadProto.InitiateUploadResponse>(
                service, METHODID_INITIATE_UPLOAD)))
        .addMethod(
          getUploadChunkMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkRequest,
              com.redjujubetree.fs.grpc.FileTransferUploadProto.UploadChunkResponse>(
                service, METHODID_UPLOAD_CHUNK)))
        .addMethod(
          getGetUploadStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusRequest,
              com.redjujubetree.fs.grpc.FileTransferUploadProto.GetUploadStatusResponse>(
                service, METHODID_GET_UPLOAD_STATUS)))
        .addMethod(
          getCompleteUploadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadRequest,
              com.redjujubetree.fs.grpc.FileTransferUploadProto.CompleteUploadResponse>(
                service, METHODID_COMPLETE_UPLOAD)))
        .addMethod(
          getCancelUploadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadRequest,
              com.redjujubetree.fs.grpc.FileTransferUploadProto.CancelUploadResponse>(
                service, METHODID_CANCEL_UPLOAD)))
        .build();
  }

  private static abstract class FileTransferUploadServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FileTransferUploadServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.redjujubetree.fs.grpc.FileTransferUploadProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FileTransferUploadService");
    }
  }

  private static final class FileTransferUploadServiceFileDescriptorSupplier
      extends FileTransferUploadServiceBaseDescriptorSupplier {
    FileTransferUploadServiceFileDescriptorSupplier() {}
  }

  private static final class FileTransferUploadServiceMethodDescriptorSupplier
      extends FileTransferUploadServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FileTransferUploadServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FileTransferUploadServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FileTransferUploadServiceFileDescriptorSupplier())
              .addMethod(getInitiateUploadMethod())
              .addMethod(getUploadChunkMethod())
              .addMethod(getGetUploadStatusMethod())
              .addMethod(getCompleteUploadMethod())
              .addMethod(getCancelUploadMethod())
              .build();
        }
      }
    }
    return result;
  }
}
