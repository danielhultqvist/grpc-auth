package se.typedef.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import se.typedef.grpc.server.auth.AuthenticationInterceptor;
import se.typedef.grpc.server.auth.DummyAccessControl;
import se.typedef.grpc.server.endpoints.BlogPostService;

import java.io.IOException;

public class Main {

  private static final int PORT = 8080;

  public static void main(final String[] args) throws IOException, InterruptedException {
    final AuthenticationInterceptor authenticationInterceptor =
      AuthenticationInterceptor.create(DummyAccessControl.create());
    final Server server =
      ServerBuilder.forPort(PORT)
        .addService(authenticationInterceptor.registerService(new BlogPostService()))
        .intercept(authenticationInterceptor)
        .build();

    server.start();
    System.out.println("Started gRPC server at port " + PORT);
    server.awaitTermination();
  }
}
