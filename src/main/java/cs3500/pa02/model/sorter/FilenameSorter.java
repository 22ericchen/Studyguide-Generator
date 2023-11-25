package cs3500.pa02.model.sorter;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * Class that takes a list of Paths and sorts their names by their
 * alphabetical order
 */
public class FilenameSorter extends AbstractSorter {
  /**
   * Constructor that takes a List of Paths and uses
   * them as an argument in the superclass constructor
   *
   * @param paths a List of Paths
   */
  public FilenameSorter(List<Path> paths) {
    super(paths);
  }

  /**
   * Compares the Strings of each file name and sorts them
   * by alphabetical order
   *
   * @return a sorted List of Paths by their lifespan
   */
  @Override
  public List<Path> sort() {
    if (!requestedFiles.isEmpty()) {
      requestedFiles.sort(Comparator.comparing(path -> path.getFileName().toString()));
      return requestedFiles;
    } else {
      return super.sort();
    }
  }
}
