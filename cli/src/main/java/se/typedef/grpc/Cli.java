package se.typedef.grpc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import se.typedef.grpc.commands.GetPost;
import se.typedef.grpc.commands.PutPost;

import java.util.List;

public class Cli {

  public static void main(final String[] args) {
    final GlobalConfig globalConfig = new GlobalConfig();
    final JCommander jc =
        JCommander.newBuilder()
            .addObject(globalConfig)
            .addCommand(new GetPost())
            .addCommand(new PutPost())
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

    runCommand(jc, globalConfig);
  }

  private static void runCommand(final JCommander jc, final GlobalConfig globalConfig) {
    final List<Object> params = jc.getCommands().get(jc.getParsedCommand()).getObjects();
    final Command command = (Command) params.get(0);
    command.run(globalConfig);
  }
}
