package cs3500.pa02.model.filetools;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * File writer/parser for a new .sr file
 */
public class FileParser {
  private String outputFile;
  private BufferedWriter writer;

  /**
   * SpaceRecognitionParser constructor that initializes the
   * name of the new file and the writer to write to it
   *
   * @param newFileName String of the name of the new file
   */
  public FileParser(String newFileName) {
    outputFile = newFileName;
    try {
      writer = new BufferedWriter(new FileWriter(outputFile));
    } catch (IOException e) {
      throw new RuntimeException(
          "FileWriter couldn't create the requested path" + outputFile);
    }
  }

  /**
   * writes in the file based on the String argument
   *
   * @param text the String of the requested text to write to the file
   */
  public void parseFile(String text) {
    try {
      writer.write(text);
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(
          "FileWriter couldn't write to the requested path" + outputFile);
    }
  }
}
