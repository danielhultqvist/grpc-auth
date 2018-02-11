package se.typedef.grpc;

import com.beust.jcommander.Parameter;

public class GlobalConfig {

  @Parameter(names = {"-h", "--host"}, description = "gRPC server host")
  public String host = "localhost";

  @Parameter(names = {"-p", "--port"}, description = "gRPC server port")
  public int port = 8080;
}
