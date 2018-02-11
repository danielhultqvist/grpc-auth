package se.typedef.grpc;

import io.grpc.*;

import java.util.concurrent.Executor;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class Authentication implements CallCredentials {
  private static final Metadata.Key<String> USER_KEY =
      Metadata.Key.of("user", ASCII_STRING_MARSHALLER);

  private final String user;

  private Authentication(final String user) {
    this.user = user;
  }

  public static Authentication create(final String user) {
    return new Authentication(user);
  }

  @Override
  public void applyRequestMetadata(
      final MethodDescriptor<?, ?> method,
      final Attributes attrs,
      final Executor appExecutor,
      final MetadataApplier applier) {

    appExecutor.execute(
        () -> {
          try {
            final Metadata headers = new Metadata();
            headers.put(USER_KEY, user);
            applier.apply(headers);
          } catch (RuntimeException e) {
            applier.fail(Status.UNAUTHENTICATED.withCause(e));
          }
        });
  }

  @Override
  public void thisUsesUnstableApi() {}
}
