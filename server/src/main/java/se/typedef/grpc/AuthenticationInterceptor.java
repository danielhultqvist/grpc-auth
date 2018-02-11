package se.typedef.grpc;

import io.grpc.*;
import se.typedef.grpc.AccessControl.AccessRight;

import java.util.Set;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@SuppressWarnings("unchecked")
public class AuthenticationInterceptor implements ServerInterceptor {
  private static final Metadata.Key<String> USER_KEY =
      Metadata.Key.of("user", ASCII_STRING_MARSHALLER);

  private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {};

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

    final Set<AccessRight> accessRightRights = AccessControl.accessForUser(user);
    System.out.println("User " + user + " accessed with rights " + accessRightRights);

    return next.startCall(call, headers);
//    return Contexts.interceptCall(ctx, call, headers, next);
  }
}
