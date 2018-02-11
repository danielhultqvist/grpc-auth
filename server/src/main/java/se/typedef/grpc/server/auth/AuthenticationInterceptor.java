package se.typedef.grpc.server.auth;

import com.google.protobuf.GeneratedMessageV3;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.Method;
import java.util.*;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@SuppressWarnings("unchecked")
public class AuthenticationInterceptor implements ServerInterceptor {
  private static final Metadata.Key<String> USER_KEY =
    Metadata.Key.of("user", ASCII_STRING_MARSHALLER);

  private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {
  };
  private final Map<MethodDescriptor<?, ?>, Optional<AccessRight>> accessRightsRequiredByMethod = new HashMap<>();
  private final AccessControl accessControl;

  private AuthenticationInterceptor(final AccessControl accessControl) {
    this.accessControl = accessControl;
  }

  public static AuthenticationInterceptor create(final AccessControl accessControl) {
    return new AuthenticationInterceptor(accessControl);
  }

  public io.grpc.BindableService registerService(final io.grpc.BindableService service) {
    service.bindService()
      .getMethods()
      .forEach(m -> {
        accessRightsRequiredByMethod.put(m.getMethodDescriptor(), correspondingMethod(service, m));
      });

    return service;
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
    final ServerCall<ReqT, RespT> call,
    final Metadata headers,
    final ServerCallHandler<ReqT, RespT> next) {
    final String user = headers.get(USER_KEY);
    if (user == null) {
      call.close(Status.UNAUTHENTICATED.withDescription("User must be supplied in call"), headers);
      return NOOP_LISTENER;
    }

    final Set<AccessRight> accessRightRights = accessControl.accessForUser(user);
    final Optional<AccessRight> requiredAccessRight = accessRightsRequiredByMethod.get(call.getMethodDescriptor());

    if (requiredAccessRight.isPresent() && !accessRightRights.contains(requiredAccessRight.get())) {
      call.close(Status.UNAUTHENTICATED.withDescription("User is not authorized for this endpoint"), headers);
      return NOOP_LISTENER;
    }

    return next.startCall(call, headers);
  }

  private static Optional<AccessRight> correspondingMethod(
    final BindableService service,
    final ServerMethodDefinition<?, ?> methodDefinition) {
    final Method method = grpcMethodImplementation(service, methodName(methodDefinition.getMethodDescriptor()));

    return Optional
      .ofNullable(method.getAnnotation(Authorization.class))
      .map(Authorization::requires);
  }

  private static String methodName(final MethodDescriptor<?, ?> methodDefinition) {
    final String fullMethodName = methodDefinition.getFullMethodName();
    return fullMethodName.substring(fullMethodName.indexOf("/") + 1);
  }

  /**
   * Find the corresponding implementation method for a gRPC method
   *
   * @param service gRPC service implementation
   * @param method  gRPC method to find an implementation for
   * @return Corresponding method. Exception if thrown if no method matches or multiple
   */
  @SuppressWarnings("ConstantConditions")
  private static Method grpcMethodImplementation(final BindableService service, final String method) {
    return Arrays.stream(service.getClass()
      .getMethods())
      .filter(m -> matchingGrpcMethod(method, m))
      .reduce((a, b) -> {
        throw new IllegalStateException("Service has multiple matching gRPC methods for " + method);
      })
      .orElseThrow(() -> new IllegalStateException("No matching gRPC method for " + method));
  }

  /**
   * This method performs magic. The gRPC framework does not expose the implementation method used for the request.
   * Hence, we need to lookup what methods corresponds to the gRPC endpoints.
   * <p>
   * This is only performed on startup-time, so it does not affect runtime performance
   *
   * @param methodName      gRPC method name
   * @param candidateMethod Method on service implementation
   * @return true if method matches gRPC endpoint name and structure
   */
  private static boolean matchingGrpcMethod(final String methodName, final Method candidateMethod) {
    return candidateMethod.getName().equals(methodName) &&
      candidateMethod.getReturnType().equals(void.class) &&
      candidateMethod.getParameterCount() == 2 &&
      GeneratedMessageV3.class.isAssignableFrom(candidateMethod.getParameterTypes()[0]) &&
      StreamObserver.class.isAssignableFrom(candidateMethod.getParameterTypes()[1]);
  }
}
