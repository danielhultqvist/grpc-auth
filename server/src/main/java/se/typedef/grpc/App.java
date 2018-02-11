package se.typedef.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class App {

  private static final int PORT = 8080;

  public static void main(final String[] args) throws IOException, InterruptedException {
    final Server server =
        ServerBuilder.forPort(PORT)
            .addService(new BlogPostService())
            .intercept(new AuthenticationInterceptor())
            .build();

    System.out.println("Starting gRPC server at port " + PORT);
    server.start().awaitTermination();
  }
}
