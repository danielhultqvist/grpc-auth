package se.typedef.grpc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class Cli {

  private static class GetPost {
    static final String NAME = "get";

    @Parameter(names = "--id", description = "Id of post", required = true)
    private Long id;
  }

  private static class PutPost {
    static final String NAME = "put";

    @Parameter(names = "--author", description = "Author of the post", required = true)
    private String author;

    @Parameter(names = "--text", description = "Text for the post", required = true)
    private String text;
  }

  public static void main(final String[] args) {
    final GetPost getParams = new GetPost();
    final PutPost putParams = new PutPost();
    final JCommander jc =
        JCommander.newBuilder()
            .addCommand(GetPost.NAME, getParams)
            .addCommand(PutPost.NAME, putParams)
            .build();

    try {
      jc.parse(args);
    } catch (ParameterException e) {
      System.out.println(e.getMessage());
      return;
    }

    if (jc.getParsedCommand() == null) {
      System.out.println("Must specify command");
      return;
    }

    switch (jc.getParsedCommand()) {
      case GetPost.NAME:
        getPost(getParams);
        break;
      case PutPost.NAME:
        putPost(putParams);
        break;
      default:
        System.out.println("Unknown command " + jc.getParsedCommand());
    }
  }

  private static void getPost(final GetPost getParams) {
    final ManagedChannel channel =
        ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext(true).build();

    final BlogPostServiceGrpc.BlogPostServiceBlockingStub service =
        BlogPostServiceGrpc.newBlockingStub(channel);

    try {
      final GetPostResponse response =
          service.getPost(GetPostRequest.newBuilder().setId(getParams.id).build());
      System.out.println(JsonFormat.printer().print(response));
    } catch (StatusRuntimeException e) {
      System.err.println(e.getMessage());
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private static void putPost(final PutPost putParams) {
    final ManagedChannel channel =
        ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext(true).build();

    final BlogPostServiceGrpc.BlogPostServiceBlockingStub service =
        BlogPostServiceGrpc.newBlockingStub(channel);

    try {
      final PutPostResponse response =
          service.putPost(
              PutPostRequest.newBuilder()
                  .setAuthor(putParams.author)
                  .setText(putParams.text)
                  .build());
      System.out.println(JsonFormat.printer().print(response));
    } catch (StatusRuntimeException e) {
      System.err.println(e.getMessage());
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
