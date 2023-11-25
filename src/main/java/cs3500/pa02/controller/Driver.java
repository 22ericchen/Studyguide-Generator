package cs3500.pa02.controller;

import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point, creates new summary if there are 3 commandline arguments
   * and begins the study session if there are none.
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    //determine the amount of commandline arguments
    if (args.length == 3) {
      CreateSummary studyGuide = new CreateSummary(Path.of(args[0]), args[1], args[2]);
      studyGuide.run();
    } else if (args.length == 0) {
      BeginSpacedRecognition studySession =
          new BeginSpacedRecognition(new InputStreamReader(System.in));
      studySession.run();
    } else {
      throw new IllegalArgumentException("Invalid number of Commandline Arguments");
    }
  }
}
