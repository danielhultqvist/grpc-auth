package se.typedef.grpc.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import se.typedef.grpc.*;

@Parameters(commandNames = {"g", "get"})
public class GetPost implements Command {
  @Parameter(names = "--id", description = "Id of post", required = true)
  private Long id;

  @Parameter(
    names = {"-help", "--help"},
    help = true
  )
  private String help;

  @Override
  public void run(final GlobalConfig globalConfig) {
    final ManagedChannel channel =
        ManagedChannelBuilder.forAddress(globalConfig.host, globalConfig.port)
            .usePlaintext(true)
            .build();

    final BlogPostServiceGrpc.BlogPostServiceBlockingStub service =
        BlogPostServiceGrpc.newBlockingStub(channel);

    try {
      final GetPostResponse response =
          service.getPost(GetPostRequest.newBuilder().setId(id).build());
      System.out.println(JsonFormat.printer().print(response));
    } catch (StatusRuntimeException e) {
      System.err.println(e.getMessage());
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
