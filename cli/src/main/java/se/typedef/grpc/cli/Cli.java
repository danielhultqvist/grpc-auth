package se.typedef.grpc.cli;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import se.typedef.grpc.BlogPostServiceGrpc;
import se.typedef.grpc.cli.commands.Delete;
import se.typedef.grpc.cli.commands.Get;
import se.typedef.grpc.cli.commands.Put;

@SuppressWarnings("FieldCanBeLocal")
@Command(
  name = "cli",
  subcommands = {Delete.class, Get.class, Put.class}
)
public class Cli implements Runnable {

  @Option(
    names = {"-h", "--host"},
    description = "gRPC server host. Default: localhost",
    type = String.class
  )
  private String host = "localhost";

  @Option(
    names = {"-p", "--p"},
    description = "gRPC server port. Default: 8080",
    type = int.class
  )
  private int port = 8080;

  @Option(
    names = "--help",
    usageHelp = true,
    description = "display this help and exit"
  )
  private boolean help;

  @Option(
    names = {"-u", "--user"},
    required = true,
    description = "Required: username for authentication"
  )
  private String user;

  public static void main(final String[] args) {
    CommandLine.run(new Cli(), System.out, args);
  }

  @Override
  public void run() {
    System.out.println("Please specify a sub command");
  }

  public BlogPostServiceGrpc.BlogPostServiceBlockingStub service() {
    final ManagedChannel channel =
      ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();

    return BlogPostServiceGrpc.newBlockingStub(channel)
      .withCallCredentials(Authentication.create(user));
  }
}
