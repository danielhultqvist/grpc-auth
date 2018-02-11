package se.typedef.grpc.cli.commands;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.StatusRuntimeException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import se.typedef.grpc.cli.Cli;
import se.typedef.grpc.GetPostRequest;
import se.typedef.grpc.GetPostResponse;

@Command(name = "get", description = "get existing post")
public class Get implements Runnable {

  @ParentCommand private Cli cli;

  @Option(
    names = {"-id", "--id"},
    required = true,
    description = "id of post to get"
  )
  private long id;

  @Option(names = "--help", usageHelp = true, description = "display this help and exit")
  boolean help;

  @Override
  public void run() {
    try {
      final GetPostResponse response =
        cli.service().getPost(GetPostRequest.newBuilder().setId(id).build());
      System.out.println(JsonFormat.printer().print(response));
    } catch (StatusRuntimeException e) {
      System.err.println(e.getMessage());
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
