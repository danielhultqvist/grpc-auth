package se.typedef.grpc.commands;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import se.typedef.grpc.*;

@Command(name = "delete", description = "Add new post")
public class Delete implements Runnable {

  @ParentCommand private Cli cli;

  @Option(
    names = {"-id", "--id"},
    required = true,
    description = "Id of post to delete"
  )
  private long id;

  @Option(names = "--help", usageHelp = true, description = "Shows this help test")
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
