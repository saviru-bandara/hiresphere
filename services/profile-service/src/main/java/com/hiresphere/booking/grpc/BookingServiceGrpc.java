package com.hiresphere.booking.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: booking.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BookingServiceGrpc {

  private BookingServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.hiresphere.booking.BookingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.hiresphere.booking.grpc.VerifyBookingRequest,
      com.hiresphere.booking.grpc.VerifyBookingResponse> getVerifyBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyBooking",
      requestType = com.hiresphere.booking.grpc.VerifyBookingRequest.class,
      responseType = com.hiresphere.booking.grpc.VerifyBookingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.booking.grpc.VerifyBookingRequest,
      com.hiresphere.booking.grpc.VerifyBookingResponse> getVerifyBookingMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.booking.grpc.VerifyBookingRequest, com.hiresphere.booking.grpc.VerifyBookingResponse> getVerifyBookingMethod;
    if ((getVerifyBookingMethod = BookingServiceGrpc.getVerifyBookingMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getVerifyBookingMethod = BookingServiceGrpc.getVerifyBookingMethod) == null) {
          BookingServiceGrpc.getVerifyBookingMethod = getVerifyBookingMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.booking.grpc.VerifyBookingRequest, com.hiresphere.booking.grpc.VerifyBookingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.booking.grpc.VerifyBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.booking.grpc.VerifyBookingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("VerifyBooking"))
              .build();
        }
      }
    }
    return getVerifyBookingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hiresphere.booking.grpc.GetBookingRequest,
      com.hiresphere.booking.grpc.GetBookingResponse> getGetBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBooking",
      requestType = com.hiresphere.booking.grpc.GetBookingRequest.class,
      responseType = com.hiresphere.booking.grpc.GetBookingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.booking.grpc.GetBookingRequest,
      com.hiresphere.booking.grpc.GetBookingResponse> getGetBookingMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.booking.grpc.GetBookingRequest, com.hiresphere.booking.grpc.GetBookingResponse> getGetBookingMethod;
    if ((getGetBookingMethod = BookingServiceGrpc.getGetBookingMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getGetBookingMethod = BookingServiceGrpc.getGetBookingMethod) == null) {
          BookingServiceGrpc.getGetBookingMethod = getGetBookingMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.booking.grpc.GetBookingRequest, com.hiresphere.booking.grpc.GetBookingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.booking.grpc.GetBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.booking.grpc.GetBookingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("GetBooking"))
              .build();
        }
      }
    }
    return getGetBookingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BookingServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceStub>() {
        @java.lang.Override
        public BookingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceStub(channel, callOptions);
        }
      };
    return BookingServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BookingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceBlockingStub>() {
        @java.lang.Override
        public BookingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceBlockingStub(channel, callOptions);
        }
      };
    return BookingServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BookingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceFutureStub>() {
        @java.lang.Override
        public BookingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceFutureStub(channel, callOptions);
        }
      };
    return BookingServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void verifyBooking(com.hiresphere.booking.grpc.VerifyBookingRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.booking.grpc.VerifyBookingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyBookingMethod(), responseObserver);
    }

    /**
     */
    default void getBooking(com.hiresphere.booking.grpc.GetBookingRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.booking.grpc.GetBookingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBookingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BookingService.
   */
  public static abstract class BookingServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BookingServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BookingService.
   */
  public static final class BookingServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BookingServiceStub> {
    private BookingServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceStub(channel, callOptions);
    }

    /**
     */
    public void verifyBooking(com.hiresphere.booking.grpc.VerifyBookingRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.booking.grpc.VerifyBookingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyBookingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBooking(com.hiresphere.booking.grpc.GetBookingRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.booking.grpc.GetBookingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBookingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BookingService.
   */
  public static final class BookingServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BookingServiceBlockingStub> {
    private BookingServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.hiresphere.booking.grpc.VerifyBookingResponse verifyBooking(com.hiresphere.booking.grpc.VerifyBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyBookingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.hiresphere.booking.grpc.GetBookingResponse getBooking(com.hiresphere.booking.grpc.GetBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBookingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BookingService.
   */
  public static final class BookingServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BookingServiceFutureStub> {
    private BookingServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.booking.grpc.VerifyBookingResponse> verifyBooking(
        com.hiresphere.booking.grpc.VerifyBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyBookingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.booking.grpc.GetBookingResponse> getBooking(
        com.hiresphere.booking.grpc.GetBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBookingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VERIFY_BOOKING = 0;
  private static final int METHODID_GET_BOOKING = 1;

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
        case METHODID_VERIFY_BOOKING:
          serviceImpl.verifyBooking((com.hiresphere.booking.grpc.VerifyBookingRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.booking.grpc.VerifyBookingResponse>) responseObserver);
          break;
        case METHODID_GET_BOOKING:
          serviceImpl.getBooking((com.hiresphere.booking.grpc.GetBookingRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.booking.grpc.GetBookingResponse>) responseObserver);
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
          getVerifyBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.booking.grpc.VerifyBookingRequest,
              com.hiresphere.booking.grpc.VerifyBookingResponse>(
                service, METHODID_VERIFY_BOOKING)))
        .addMethod(
          getGetBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.booking.grpc.GetBookingRequest,
              com.hiresphere.booking.grpc.GetBookingResponse>(
                service, METHODID_GET_BOOKING)))
        .build();
  }

  private static abstract class BookingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BookingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.hiresphere.booking.grpc.BookingOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BookingService");
    }
  }

  private static final class BookingServiceFileDescriptorSupplier
      extends BookingServiceBaseDescriptorSupplier {
    BookingServiceFileDescriptorSupplier() {}
  }

  private static final class BookingServiceMethodDescriptorSupplier
      extends BookingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BookingServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (BookingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BookingServiceFileDescriptorSupplier())
              .addMethod(getVerifyBookingMethod())
              .addMethod(getGetBookingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
