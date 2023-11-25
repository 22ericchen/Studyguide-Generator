package cs3500.pa02.model.sorter;

import java.nio.file.Path;
import java.util.List;

/**
 * Abstract class for different sorting classes
 */
public abstract class AbstractSorter {
  List<Path> requestedFiles;

  /**
   * Constructor that initializes the requestedFiles
   * variable
   *
   * @param paths takes a List of Paths that want to be sorted
   */
  public AbstractSorter(List<Path> paths) {
    requestedFiles = paths;
  }

  /**
   * Abstract sorting method
   *
   * @return a List of sorted Paths
   */
  public List<Path> sort() {
    return null;
  }
}
