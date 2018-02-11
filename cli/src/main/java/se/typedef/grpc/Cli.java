package se.typedef.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import picocli.CommandLine;
import se.typedef.grpc.commands.Delete;
import se.typedef.grpc.commands.Get;
import se.typedef.grpc.commands.Put;

@SuppressWarnings("FieldCanBeLocal")
@CommandLine.Command(
  name = "cli",
  subcommands = {Delete.class, Get.class, Put.class}
)
public class Cli implements Runnable {

  @CommandLine.Option(
    names = {"-h", "--host"},
    description = "gRPC server host. Default: localhost",
    type = String.class
  )
  private String host = "localhost";

  @CommandLine.Option(
    names = {"-p", "--p"},
    description = "gRPC server port. Default: 8080",
    type = int.class
  )
  private int port = 8080;

  @CommandLine.Option(
    names = "--help",
    usageHelp = true,
    description = "display this help and exit"
  )
  boolean help;

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
    return BlogPostServiceGrpc.newBlockingStub(channel);
  }
}
