package se.typedef.grpc.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import se.typedef.grpc.*;

@Parameters(commandNames = {"p", "put"})
public class PutPost implements Command {
  @Parameter(names = "--author", description = "Author of the post", required = true)
  private String author;

  @Parameter(names = "--text", description = "Text for the post", required = true)
  private String text;

  @Override
  public void run(final GlobalConfig globalConfig) {
    final ManagedChannel channel =
        ManagedChannelBuilder.forAddress(globalConfig.host, globalConfig.port)
            .usePlaintext(true)
            .build();

    final BlogPostServiceGrpc.BlogPostServiceBlockingStub service =
        BlogPostServiceGrpc.newBlockingStub(channel);

    try {
      final PutPostResponse response =
          service.putPost(PutPostRequest.newBuilder().setAuthor(author).setText(text).build());
      System.out.println(JsonFormat.printer().print(response));
    } catch (StatusRuntimeException e) {
      System.err.println(e.getMessage());
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
