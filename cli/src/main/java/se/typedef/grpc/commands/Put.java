package se.typedef.grpc.commands;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import se.typedef.grpc.BlogPostServiceGrpc;
import se.typedef.grpc.Cli;
import se.typedef.grpc.PutPostRequest;
import se.typedef.grpc.PutPostResponse;

@Command(name = "put", description = "Add new post")
public class Put implements Runnable {

  @ParentCommand private Cli cli;

  @Option(
    names = {"-author", "--author"},
    required = true,
    description = "Author of post"
  )
  private String author;

  @Option(
    names = {"-text", "--text"},
    required = true,
    description = "The post text"
  )
  private String text;

  @Option(names = "--help", usageHelp = true, description = "Shows this help test")
  boolean help;

  @Override
  public void run() {
    try {
      final PutPostResponse response =
          cli.service()
              .putPost(PutPostRequest.newBuilder().setAuthor(author).setText(text).build());
      System.out.println(JsonFormat.printer().print(response));
    } catch (StatusRuntimeException e) {
      System.err.println(e.getMessage());
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
