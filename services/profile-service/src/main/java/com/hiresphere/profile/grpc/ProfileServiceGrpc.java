package com.hiresphere.profile.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ─── SERVICE ──────────────────────────────────────────────────────────────────
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: profile.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ProfileServiceGrpc {

  private ProfileServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.hiresphere.profile.ProfileService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.GetProfileRequest,
      com.hiresphere.profile.grpc.GetProfileResponse> getGetProfileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetProfile",
      requestType = com.hiresphere.profile.grpc.GetProfileRequest.class,
      responseType = com.hiresphere.profile.grpc.GetProfileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.GetProfileRequest,
      com.hiresphere.profile.grpc.GetProfileResponse> getGetProfileMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.GetProfileRequest, com.hiresphere.profile.grpc.GetProfileResponse> getGetProfileMethod;
    if ((getGetProfileMethod = ProfileServiceGrpc.getGetProfileMethod) == null) {
      synchronized (ProfileServiceGrpc.class) {
        if ((getGetProfileMethod = ProfileServiceGrpc.getGetProfileMethod) == null) {
          ProfileServiceGrpc.getGetProfileMethod = getGetProfileMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.profile.grpc.GetProfileRequest, com.hiresphere.profile.grpc.GetProfileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetProfile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.GetProfileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.GetProfileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("GetProfile"))
              .build();
        }
      }
    }
    return getGetProfileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.CreateProfileRequest,
      com.hiresphere.profile.grpc.CreateProfileResponse> getCreateProfileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateProfile",
      requestType = com.hiresphere.profile.grpc.CreateProfileRequest.class,
      responseType = com.hiresphere.profile.grpc.CreateProfileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.CreateProfileRequest,
      com.hiresphere.profile.grpc.CreateProfileResponse> getCreateProfileMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.CreateProfileRequest, com.hiresphere.profile.grpc.CreateProfileResponse> getCreateProfileMethod;
    if ((getCreateProfileMethod = ProfileServiceGrpc.getCreateProfileMethod) == null) {
      synchronized (ProfileServiceGrpc.class) {
        if ((getCreateProfileMethod = ProfileServiceGrpc.getCreateProfileMethod) == null) {
          ProfileServiceGrpc.getCreateProfileMethod = getCreateProfileMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.profile.grpc.CreateProfileRequest, com.hiresphere.profile.grpc.CreateProfileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateProfile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.CreateProfileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.CreateProfileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("CreateProfile"))
              .build();
        }
      }
    }
    return getCreateProfileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.UpdateProfileRequest,
      com.hiresphere.profile.grpc.UpdateProfileResponse> getUpdateProfileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateProfile",
      requestType = com.hiresphere.profile.grpc.UpdateProfileRequest.class,
      responseType = com.hiresphere.profile.grpc.UpdateProfileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.UpdateProfileRequest,
      com.hiresphere.profile.grpc.UpdateProfileResponse> getUpdateProfileMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.UpdateProfileRequest, com.hiresphere.profile.grpc.UpdateProfileResponse> getUpdateProfileMethod;
    if ((getUpdateProfileMethod = ProfileServiceGrpc.getUpdateProfileMethod) == null) {
      synchronized (ProfileServiceGrpc.class) {
        if ((getUpdateProfileMethod = ProfileServiceGrpc.getUpdateProfileMethod) == null) {
          ProfileServiceGrpc.getUpdateProfileMethod = getUpdateProfileMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.profile.grpc.UpdateProfileRequest, com.hiresphere.profile.grpc.UpdateProfileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateProfile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.UpdateProfileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.UpdateProfileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("UpdateProfile"))
              .build();
        }
      }
    }
    return getUpdateProfileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.SearchInterviewersRequest,
      com.hiresphere.profile.grpc.SearchInterviewersResponse> getSearchInterviewersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchInterviewers",
      requestType = com.hiresphere.profile.grpc.SearchInterviewersRequest.class,
      responseType = com.hiresphere.profile.grpc.SearchInterviewersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.SearchInterviewersRequest,
      com.hiresphere.profile.grpc.SearchInterviewersResponse> getSearchInterviewersMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.SearchInterviewersRequest, com.hiresphere.profile.grpc.SearchInterviewersResponse> getSearchInterviewersMethod;
    if ((getSearchInterviewersMethod = ProfileServiceGrpc.getSearchInterviewersMethod) == null) {
      synchronized (ProfileServiceGrpc.class) {
        if ((getSearchInterviewersMethod = ProfileServiceGrpc.getSearchInterviewersMethod) == null) {
          ProfileServiceGrpc.getSearchInterviewersMethod = getSearchInterviewersMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.profile.grpc.SearchInterviewersRequest, com.hiresphere.profile.grpc.SearchInterviewersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchInterviewers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.SearchInterviewersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.SearchInterviewersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("SearchInterviewers"))
              .build();
        }
      }
    }
    return getSearchInterviewersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.GetAvailabilityRequest,
      com.hiresphere.profile.grpc.GetAvailabilityResponse> getGetAvailabilityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAvailability",
      requestType = com.hiresphere.profile.grpc.GetAvailabilityRequest.class,
      responseType = com.hiresphere.profile.grpc.GetAvailabilityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.GetAvailabilityRequest,
      com.hiresphere.profile.grpc.GetAvailabilityResponse> getGetAvailabilityMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.GetAvailabilityRequest, com.hiresphere.profile.grpc.GetAvailabilityResponse> getGetAvailabilityMethod;
    if ((getGetAvailabilityMethod = ProfileServiceGrpc.getGetAvailabilityMethod) == null) {
      synchronized (ProfileServiceGrpc.class) {
        if ((getGetAvailabilityMethod = ProfileServiceGrpc.getGetAvailabilityMethod) == null) {
          ProfileServiceGrpc.getGetAvailabilityMethod = getGetAvailabilityMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.profile.grpc.GetAvailabilityRequest, com.hiresphere.profile.grpc.GetAvailabilityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAvailability"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.GetAvailabilityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.GetAvailabilityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("GetAvailability"))
              .build();
        }
      }
    }
    return getGetAvailabilityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.UpdateRatingRequest,
      com.hiresphere.profile.grpc.UpdateRatingResponse> getUpdateRatingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateRating",
      requestType = com.hiresphere.profile.grpc.UpdateRatingRequest.class,
      responseType = com.hiresphere.profile.grpc.UpdateRatingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.UpdateRatingRequest,
      com.hiresphere.profile.grpc.UpdateRatingResponse> getUpdateRatingMethod() {
    io.grpc.MethodDescriptor<com.hiresphere.profile.grpc.UpdateRatingRequest, com.hiresphere.profile.grpc.UpdateRatingResponse> getUpdateRatingMethod;
    if ((getUpdateRatingMethod = ProfileServiceGrpc.getUpdateRatingMethod) == null) {
      synchronized (ProfileServiceGrpc.class) {
        if ((getUpdateRatingMethod = ProfileServiceGrpc.getUpdateRatingMethod) == null) {
          ProfileServiceGrpc.getUpdateRatingMethod = getUpdateRatingMethod =
              io.grpc.MethodDescriptor.<com.hiresphere.profile.grpc.UpdateRatingRequest, com.hiresphere.profile.grpc.UpdateRatingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateRating"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.UpdateRatingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.hiresphere.profile.grpc.UpdateRatingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("UpdateRating"))
              .build();
        }
      }
    }
    return getUpdateRatingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ProfileServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ProfileServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ProfileServiceStub>() {
        @java.lang.Override
        public ProfileServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ProfileServiceStub(channel, callOptions);
        }
      };
    return ProfileServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ProfileServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ProfileServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ProfileServiceBlockingStub>() {
        @java.lang.Override
        public ProfileServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ProfileServiceBlockingStub(channel, callOptions);
        }
      };
    return ProfileServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ProfileServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ProfileServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ProfileServiceFutureStub>() {
        @java.lang.Override
        public ProfileServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ProfileServiceFutureStub(channel, callOptions);
        }
      };
    return ProfileServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * ─── SERVICE ──────────────────────────────────────────────────────────────────
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void getProfile(com.hiresphere.profile.grpc.GetProfileRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.GetProfileResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProfileMethod(), responseObserver);
    }

    /**
     */
    default void createProfile(com.hiresphere.profile.grpc.CreateProfileRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.CreateProfileResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateProfileMethod(), responseObserver);
    }

    /**
     */
    default void updateProfile(com.hiresphere.profile.grpc.UpdateProfileRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.UpdateProfileResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateProfileMethod(), responseObserver);
    }

    /**
     */
    default void searchInterviewers(com.hiresphere.profile.grpc.SearchInterviewersRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.SearchInterviewersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchInterviewersMethod(), responseObserver);
    }

    /**
     */
    default void getAvailability(com.hiresphere.profile.grpc.GetAvailabilityRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.GetAvailabilityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAvailabilityMethod(), responseObserver);
    }

    /**
     */
    default void updateRating(com.hiresphere.profile.grpc.UpdateRatingRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.UpdateRatingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateRatingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ProfileService.
   * <pre>
   * ─── SERVICE ──────────────────────────────────────────────────────────────────
   * </pre>
   */
  public static abstract class ProfileServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ProfileServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ProfileService.
   * <pre>
   * ─── SERVICE ──────────────────────────────────────────────────────────────────
   * </pre>
   */
  public static final class ProfileServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ProfileServiceStub> {
    private ProfileServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProfileServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ProfileServiceStub(channel, callOptions);
    }

    /**
     */
    public void getProfile(com.hiresphere.profile.grpc.GetProfileRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.GetProfileResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetProfileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createProfile(com.hiresphere.profile.grpc.CreateProfileRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.CreateProfileResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateProfileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateProfile(com.hiresphere.profile.grpc.UpdateProfileRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.UpdateProfileResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateProfileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchInterviewers(com.hiresphere.profile.grpc.SearchInterviewersRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.SearchInterviewersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchInterviewersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAvailability(com.hiresphere.profile.grpc.GetAvailabilityRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.GetAvailabilityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAvailabilityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateRating(com.hiresphere.profile.grpc.UpdateRatingRequest request,
        io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.UpdateRatingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateRatingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ProfileService.
   * <pre>
   * ─── SERVICE ──────────────────────────────────────────────────────────────────
   * </pre>
   */
  public static final class ProfileServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ProfileServiceBlockingStub> {
    private ProfileServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProfileServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ProfileServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.hiresphere.profile.grpc.GetProfileResponse getProfile(com.hiresphere.profile.grpc.GetProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetProfileMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.hiresphere.profile.grpc.CreateProfileResponse createProfile(com.hiresphere.profile.grpc.CreateProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateProfileMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.hiresphere.profile.grpc.UpdateProfileResponse updateProfile(com.hiresphere.profile.grpc.UpdateProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateProfileMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.hiresphere.profile.grpc.SearchInterviewersResponse searchInterviewers(com.hiresphere.profile.grpc.SearchInterviewersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchInterviewersMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.hiresphere.profile.grpc.GetAvailabilityResponse getAvailability(com.hiresphere.profile.grpc.GetAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAvailabilityMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.hiresphere.profile.grpc.UpdateRatingResponse updateRating(com.hiresphere.profile.grpc.UpdateRatingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateRatingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ProfileService.
   * <pre>
   * ─── SERVICE ──────────────────────────────────────────────────────────────────
   * </pre>
   */
  public static final class ProfileServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ProfileServiceFutureStub> {
    private ProfileServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProfileServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ProfileServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.profile.grpc.GetProfileResponse> getProfile(
        com.hiresphere.profile.grpc.GetProfileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetProfileMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.profile.grpc.CreateProfileResponse> createProfile(
        com.hiresphere.profile.grpc.CreateProfileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateProfileMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.profile.grpc.UpdateProfileResponse> updateProfile(
        com.hiresphere.profile.grpc.UpdateProfileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateProfileMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.profile.grpc.SearchInterviewersResponse> searchInterviewers(
        com.hiresphere.profile.grpc.SearchInterviewersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchInterviewersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.profile.grpc.GetAvailabilityResponse> getAvailability(
        com.hiresphere.profile.grpc.GetAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAvailabilityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hiresphere.profile.grpc.UpdateRatingResponse> updateRating(
        com.hiresphere.profile.grpc.UpdateRatingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateRatingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_PROFILE = 0;
  private static final int METHODID_CREATE_PROFILE = 1;
  private static final int METHODID_UPDATE_PROFILE = 2;
  private static final int METHODID_SEARCH_INTERVIEWERS = 3;
  private static final int METHODID_GET_AVAILABILITY = 4;
  private static final int METHODID_UPDATE_RATING = 5;

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
        case METHODID_GET_PROFILE:
          serviceImpl.getProfile((com.hiresphere.profile.grpc.GetProfileRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.GetProfileResponse>) responseObserver);
          break;
        case METHODID_CREATE_PROFILE:
          serviceImpl.createProfile((com.hiresphere.profile.grpc.CreateProfileRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.CreateProfileResponse>) responseObserver);
          break;
        case METHODID_UPDATE_PROFILE:
          serviceImpl.updateProfile((com.hiresphere.profile.grpc.UpdateProfileRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.UpdateProfileResponse>) responseObserver);
          break;
        case METHODID_SEARCH_INTERVIEWERS:
          serviceImpl.searchInterviewers((com.hiresphere.profile.grpc.SearchInterviewersRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.SearchInterviewersResponse>) responseObserver);
          break;
        case METHODID_GET_AVAILABILITY:
          serviceImpl.getAvailability((com.hiresphere.profile.grpc.GetAvailabilityRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.GetAvailabilityResponse>) responseObserver);
          break;
        case METHODID_UPDATE_RATING:
          serviceImpl.updateRating((com.hiresphere.profile.grpc.UpdateRatingRequest) request,
              (io.grpc.stub.StreamObserver<com.hiresphere.profile.grpc.UpdateRatingResponse>) responseObserver);
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
          getGetProfileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.profile.grpc.GetProfileRequest,
              com.hiresphere.profile.grpc.GetProfileResponse>(
                service, METHODID_GET_PROFILE)))
        .addMethod(
          getCreateProfileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.profile.grpc.CreateProfileRequest,
              com.hiresphere.profile.grpc.CreateProfileResponse>(
                service, METHODID_CREATE_PROFILE)))
        .addMethod(
          getUpdateProfileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.profile.grpc.UpdateProfileRequest,
              com.hiresphere.profile.grpc.UpdateProfileResponse>(
                service, METHODID_UPDATE_PROFILE)))
        .addMethod(
          getSearchInterviewersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.profile.grpc.SearchInterviewersRequest,
              com.hiresphere.profile.grpc.SearchInterviewersResponse>(
                service, METHODID_SEARCH_INTERVIEWERS)))
        .addMethod(
          getGetAvailabilityMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.profile.grpc.GetAvailabilityRequest,
              com.hiresphere.profile.grpc.GetAvailabilityResponse>(
                service, METHODID_GET_AVAILABILITY)))
        .addMethod(
          getUpdateRatingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.hiresphere.profile.grpc.UpdateRatingRequest,
              com.hiresphere.profile.grpc.UpdateRatingResponse>(
                service, METHODID_UPDATE_RATING)))
        .build();
  }

  private static abstract class ProfileServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ProfileServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.hiresphere.profile.grpc.ProfileOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ProfileService");
    }
  }

  private static final class ProfileServiceFileDescriptorSupplier
      extends ProfileServiceBaseDescriptorSupplier {
    ProfileServiceFileDescriptorSupplier() {}
  }

  private static final class ProfileServiceMethodDescriptorSupplier
      extends ProfileServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ProfileServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ProfileServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ProfileServiceFileDescriptorSupplier())
              .addMethod(getGetProfileMethod())
              .addMethod(getCreateProfileMethod())
              .addMethod(getUpdateProfileMethod())
              .addMethod(getSearchInterviewersMethod())
              .addMethod(getGetAvailabilityMethod())
              .addMethod(getUpdateRatingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
