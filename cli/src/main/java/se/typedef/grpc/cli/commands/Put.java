package se.typedef.grpc.cli.commands;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.StatusRuntimeException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import se.typedef.grpc.cli.Cli;
import se.typedef.grpc.PutPostRequest;
import se.typedef.grpc.PutPostResponse;

@Command(name = "put", description = "add new post")
public class Put implements Runnable {

  @ParentCommand private Cli cli;

  @Option(
    names = {"-author", "--author"},
    required = true,
    description = "author of post"
  )
  private String author;

  @Option(
    names = {"-text", "--text"},
    required = true,
    description = "the post text"
  )
  private String text;

  @Option(names = "--help", usageHelp = true, description = "display this help and exit")
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
