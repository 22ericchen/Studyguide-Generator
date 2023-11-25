package cs3500.pa02.model.filetools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to walk through every file within a directory
 */
public class FolderWalker {
  private Path directory;

  public FolderWalker(Path directory) {
    this.directory = directory;
  }

  /**
   *Walks through each file within the directory path argument and
   * puts paths to every file within that directory in a list
   *
   * @return a list of Paths to every file within the directory
   */
  public List<Path> getFiles() {
    List<Path> files;
    try {
      //get all .md files in a given directory
      files = Files.walk(directory)
          .filter(path -> path.toString().endsWith(".md")).collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("directory " + directory + " doesn't exist");
    }
    return files;
  }
}
