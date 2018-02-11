package se.typedef.grpc.cli.commands;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.StatusRuntimeException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import se.typedef.grpc.*;
import se.typedef.grpc.cli.Cli;

@Command(name = "delete", description = "add new post")
public class Delete implements Runnable {

  @ParentCommand private Cli cli;

  @Option(
    names = {"-id", "--id"},
    required = true,
    description = "id of post to delete"
  )
  private long id;

  @Option(names = "--help", usageHelp = true, description = "display this help and exit")
  boolean help;

  @Override
  public void run() {
    try {
      final DeletePostResponse response =
          cli.service().deletePost(DeletePostRequest.newBuilder().setId(id).build());
      System.out.println(JsonFormat.printer().print(response));
    } catch (StatusRuntimeException e) {
      System.err.println(e.getMessage());
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
