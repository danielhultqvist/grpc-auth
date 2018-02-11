package se.typedef.grpc;

public interface Command {
  void run(final GlobalConfig globalConfig);
}
