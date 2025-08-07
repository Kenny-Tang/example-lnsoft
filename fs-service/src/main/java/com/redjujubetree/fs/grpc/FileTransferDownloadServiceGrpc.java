package com.redjujubetree.fs.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: file_transfer_dowload.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FileTransferDownloadServiceGrpc {

  private FileTransferDownloadServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "file.transfer.download.FileTransferDownloadService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse> getInitiateDownloadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InitiateDownload",
      requestType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse> getInitiateDownloadMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse> getInitiateDownloadMethod;
    if ((getInitiateDownloadMethod = FileTransferDownloadServiceGrpc.getInitiateDownloadMethod) == null) {
      synchronized (FileTransferDownloadServiceGrpc.class) {
        if ((getInitiateDownloadMethod = FileTransferDownloadServiceGrpc.getInitiateDownloadMethod) == null) {
          FileTransferDownloadServiceGrpc.getInitiateDownloadMethod = getInitiateDownloadMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InitiateDownload"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferDownloadServiceMethodDescriptorSupplier("InitiateDownload"))
              .build();
        }
      }
    }
    return getInitiateDownloadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse> getDownloadChunkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DownloadChunk",
      requestType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse> getDownloadChunkMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse> getDownloadChunkMethod;
    if ((getDownloadChunkMethod = FileTransferDownloadServiceGrpc.getDownloadChunkMethod) == null) {
      synchronized (FileTransferDownloadServiceGrpc.class) {
        if ((getDownloadChunkMethod = FileTransferDownloadServiceGrpc.getDownloadChunkMethod) == null) {
          FileTransferDownloadServiceGrpc.getDownloadChunkMethod = getDownloadChunkMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DownloadChunk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferDownloadServiceMethodDescriptorSupplier("DownloadChunk"))
              .build();
        }
      }
    }
    return getDownloadChunkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse> getGetDownloadStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDownloadStatus",
      requestType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse> getGetDownloadStatusMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse> getGetDownloadStatusMethod;
    if ((getGetDownloadStatusMethod = FileTransferDownloadServiceGrpc.getGetDownloadStatusMethod) == null) {
      synchronized (FileTransferDownloadServiceGrpc.class) {
        if ((getGetDownloadStatusMethod = FileTransferDownloadServiceGrpc.getGetDownloadStatusMethod) == null) {
          FileTransferDownloadServiceGrpc.getGetDownloadStatusMethod = getGetDownloadStatusMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDownloadStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferDownloadServiceMethodDescriptorSupplier("GetDownloadStatus"))
              .build();
        }
      }
    }
    return getGetDownloadStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse> getCancelDownloadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelDownload",
      requestType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse> getCancelDownloadMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse> getCancelDownloadMethod;
    if ((getCancelDownloadMethod = FileTransferDownloadServiceGrpc.getCancelDownloadMethod) == null) {
      synchronized (FileTransferDownloadServiceGrpc.class) {
        if ((getCancelDownloadMethod = FileTransferDownloadServiceGrpc.getCancelDownloadMethod) == null) {
          FileTransferDownloadServiceGrpc.getCancelDownloadMethod = getCancelDownloadMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelDownload"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferDownloadServiceMethodDescriptorSupplier("CancelDownload"))
              .build();
        }
      }
    }
    return getCancelDownloadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse> getListFilesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListFiles",
      requestType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse> getListFilesMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse> getListFilesMethod;
    if ((getListFilesMethod = FileTransferDownloadServiceGrpc.getListFilesMethod) == null) {
      synchronized (FileTransferDownloadServiceGrpc.class) {
        if ((getListFilesMethod = FileTransferDownloadServiceGrpc.getListFilesMethod) == null) {
          FileTransferDownloadServiceGrpc.getListFilesMethod = getListFilesMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListFiles"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferDownloadServiceMethodDescriptorSupplier("ListFiles"))
              .build();
        }
      }
    }
    return getListFilesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse> getGetFileInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetFileInfo",
      requestType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest.class,
      responseType = com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest,
      com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse> getGetFileInfoMethod() {
    io.grpc.MethodDescriptor<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse> getGetFileInfoMethod;
    if ((getGetFileInfoMethod = FileTransferDownloadServiceGrpc.getGetFileInfoMethod) == null) {
      synchronized (FileTransferDownloadServiceGrpc.class) {
        if ((getGetFileInfoMethod = FileTransferDownloadServiceGrpc.getGetFileInfoMethod) == null) {
          FileTransferDownloadServiceGrpc.getGetFileInfoMethod = getGetFileInfoMethod =
              io.grpc.MethodDescriptor.<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest, com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetFileInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FileTransferDownloadServiceMethodDescriptorSupplier("GetFileInfo"))
              .build();
        }
      }
    }
    return getGetFileInfoMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FileTransferDownloadServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileTransferDownloadServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileTransferDownloadServiceStub>() {
        @java.lang.Override
        public FileTransferDownloadServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileTransferDownloadServiceStub(channel, callOptions);
        }
      };
    return FileTransferDownloadServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FileTransferDownloadServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileTransferDownloadServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileTransferDownloadServiceBlockingStub>() {
        @java.lang.Override
        public FileTransferDownloadServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileTransferDownloadServiceBlockingStub(channel, callOptions);
        }
      };
    return FileTransferDownloadServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FileTransferDownloadServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileTransferDownloadServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileTransferDownloadServiceFutureStub>() {
        @java.lang.Override
        public FileTransferDownloadServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileTransferDownloadServiceFutureStub(channel, callOptions);
        }
      };
    return FileTransferDownloadServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * ========== 文件下载功能 ==========
     * 初始化下载会话
     * </pre>
     */
    default void initiateDownload(com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitiateDownloadMethod(), responseObserver);
    }

    /**
     * <pre>
     * 下载分片（支持并发下载多个分片）
     * </pre>
     */
    default void downloadChunk(com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDownloadChunkMethod(), responseObserver);
    }

    /**
     * <pre>
     * 获取下载状态
     * </pre>
     */
    default void getDownloadStatus(com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDownloadStatusMethod(), responseObserver);
    }

    /**
     * <pre>
     * 取消下载
     * </pre>
     */
    default void cancelDownload(com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelDownloadMethod(), responseObserver);
    }

    /**
     * <pre>
     * ========== 文件管理功能 ==========
     * 列出可下载的文件
     * </pre>
     */
    default void listFiles(com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListFilesMethod(), responseObserver);
    }

    /**
     * <pre>
     * 获取文件详细信息
     * </pre>
     */
    default void getFileInfo(com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetFileInfoMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FileTransferDownloadService.
   */
  public static abstract class FileTransferDownloadServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FileTransferDownloadServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FileTransferDownloadService.
   */
  public static final class FileTransferDownloadServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FileTransferDownloadServiceStub> {
    private FileTransferDownloadServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferDownloadServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileTransferDownloadServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * ========== 文件下载功能 ==========
     * 初始化下载会话
     * </pre>
     */
    public void initiateDownload(com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitiateDownloadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 下载分片（支持并发下载多个分片）
     * </pre>
     */
    public void downloadChunk(com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getDownloadChunkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 获取下载状态
     * </pre>
     */
    public void getDownloadStatus(com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDownloadStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 取消下载
     * </pre>
     */
    public void cancelDownload(com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelDownloadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * ========== 文件管理功能 ==========
     * 列出可下载的文件
     * </pre>
     */
    public void listFiles(com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListFilesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 获取文件详细信息
     * </pre>
     */
    public void getFileInfo(com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest request,
        io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetFileInfoMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FileTransferDownloadService.
   */
  public static final class FileTransferDownloadServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FileTransferDownloadServiceBlockingStub> {
    private FileTransferDownloadServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferDownloadServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileTransferDownloadServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * ========== 文件下载功能 ==========
     * 初始化下载会话
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse initiateDownload(com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitiateDownloadMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 下载分片（支持并发下载多个分片）
     * </pre>
     */
    public java.util.Iterator<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse> downloadChunk(
        com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getDownloadChunkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 获取下载状态
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse getDownloadStatus(com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDownloadStatusMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 取消下载
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse cancelDownload(com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelDownloadMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ========== 文件管理功能 ==========
     * 列出可下载的文件
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse listFiles(com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListFilesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 获取文件详细信息
     * </pre>
     */
    public com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse getFileInfo(com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetFileInfoMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FileTransferDownloadService.
   */
  public static final class FileTransferDownloadServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FileTransferDownloadServiceFutureStub> {
    private FileTransferDownloadServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferDownloadServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileTransferDownloadServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * ========== 文件下载功能 ==========
     * 初始化下载会话
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse> initiateDownload(
        com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitiateDownloadMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 获取下载状态
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse> getDownloadStatus(
        com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDownloadStatusMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 取消下载
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse> cancelDownload(
        com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelDownloadMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * ========== 文件管理功能 ==========
     * 列出可下载的文件
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse> listFiles(
        com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListFilesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 获取文件详细信息
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse> getFileInfo(
        com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetFileInfoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INITIATE_DOWNLOAD = 0;
  private static final int METHODID_DOWNLOAD_CHUNK = 1;
  private static final int METHODID_GET_DOWNLOAD_STATUS = 2;
  private static final int METHODID_CANCEL_DOWNLOAD = 3;
  private static final int METHODID_LIST_FILES = 4;
  private static final int METHODID_GET_FILE_INFO = 5;

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
        case METHODID_INITIATE_DOWNLOAD:
          serviceImpl.initiateDownload((com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse>) responseObserver);
          break;
        case METHODID_DOWNLOAD_CHUNK:
          serviceImpl.downloadChunk((com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse>) responseObserver);
          break;
        case METHODID_GET_DOWNLOAD_STATUS:
          serviceImpl.getDownloadStatus((com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse>) responseObserver);
          break;
        case METHODID_CANCEL_DOWNLOAD:
          serviceImpl.cancelDownload((com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse>) responseObserver);
          break;
        case METHODID_LIST_FILES:
          serviceImpl.listFiles((com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse>) responseObserver);
          break;
        case METHODID_GET_FILE_INFO:
          serviceImpl.getFileInfo((com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest) request,
              (io.grpc.stub.StreamObserver<com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getInitiateDownloadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadRequest,
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.InitiateDownloadResponse>(
                service, METHODID_INITIATE_DOWNLOAD)))
        .addMethod(
          getDownloadChunkMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkRequest,
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.DownloadChunkResponse>(
                service, METHODID_DOWNLOAD_CHUNK)))
        .addMethod(
          getGetDownloadStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusRequest,
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetDownloadStatusResponse>(
                service, METHODID_GET_DOWNLOAD_STATUS)))
        .addMethod(
          getCancelDownloadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadRequest,
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.CancelDownloadResponse>(
                service, METHODID_CANCEL_DOWNLOAD)))
        .addMethod(
          getListFilesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesRequest,
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.ListFilesResponse>(
                service, METHODID_LIST_FILES)))
        .addMethod(
          getGetFileInfoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoRequest,
              com.redjujubetree.fs.grpc.FileTransferDownloadProto.GetFileInfoResponse>(
                service, METHODID_GET_FILE_INFO)))
        .build();
  }

  private static abstract class FileTransferDownloadServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FileTransferDownloadServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.redjujubetree.fs.grpc.FileTransferDownloadProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FileTransferDownloadService");
    }
  }

  private static final class FileTransferDownloadServiceFileDescriptorSupplier
      extends FileTransferDownloadServiceBaseDescriptorSupplier {
    FileTransferDownloadServiceFileDescriptorSupplier() {}
  }

  private static final class FileTransferDownloadServiceMethodDescriptorSupplier
      extends FileTransferDownloadServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FileTransferDownloadServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (FileTransferDownloadServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FileTransferDownloadServiceFileDescriptorSupplier())
              .addMethod(getInitiateDownloadMethod())
              .addMethod(getDownloadChunkMethod())
              .addMethod(getGetDownloadStatusMethod())
              .addMethod(getCancelDownloadMethod())
              .addMethod(getListFilesMethod())
              .addMethod(getGetFileInfoMethod())
              .build();
        }
      }
    }
    return result;
  }
}
