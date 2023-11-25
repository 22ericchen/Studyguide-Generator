package cs3500.pa02.model.sorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class that takes a list of Paths and sorts them by their last
 * modified timestamp
 */
public class ModifiedSorter extends AbstractSorter {
  /**
   * Constructor that takes a List of Paths and uses
   * them as an argument in the superclass constructor
   *
   * @param paths a List of Paths
   */
  public ModifiedSorter(List<Path> paths) {
    super(paths);
  }

  /**
   * Compares the timestamp of when each path in the list
   * was last modified and organizes them from most
   * recent to the oldest changes
   *
   * @return a sorted List of Paths by their lifespan
   */
  @Override
  public List<Path> sort() {
    if (!requestedFiles.isEmpty()) {
      requestedFiles.sort(Comparator.comparingLong(path -> {
        try {
          return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
          throw new RuntimeException("Sorter unable to find attributes of current file");
        }
      }));
      Collections.reverse(requestedFiles);
      return requestedFiles;
    } else {
      return super.sort();
    }
  }
}
