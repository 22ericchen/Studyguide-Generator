package cs3500.pa02.model.filetools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that enables a class to convert a file path to a
 * more usable data type
 */
public class FileToText {
  /**
   * AbstractReader Constructor
   */
  public FileToText() {
  }

  /**
   * reads through the file of the given path argument
   * and converts and puts every line of the file as a
   * String into an ArrayList
   *
   * @return a List of every line in the file as a String
   */
  public List<String> readThroughFile(Path file) {
    //Instantiate new ArrayList
    List<String> fileLines = new ArrayList<>();

    BufferedReader reader;
    String line;

    //initialize BufferedReader if possible
    try {
      reader = new BufferedReader(new java.io.FileReader(file.toFile()));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(file + " could not be found in the directory.");
    }

    //Go through each line and add to new list
    try {
      while ((line = reader.readLine()) != null) {
        fileLines.add(line);
      }
    } catch (IOException e) {
      throw new RuntimeException("Reader tried reading lines in " + file + "that didn't exist");
    }
    return fileLines;
  }
}
