package cs3500.pa02.model.sorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;

/**
 * Class that takes a list of Paths and sorts them by their created
 * timestamp
 */
public class CreatedSorter extends AbstractSorter {
  /**
   * Constructor that takes a List of Paths and uses
   * them as an argument in the superclass constructor
   *
   * @param paths a List of Paths
   */
  public CreatedSorter(List<Path> paths) {
    super(paths);
  }

  /**
   * Compares the timestamp of when each path in the list
   * was created and organizes them from oldest to youngest
   *
   * @return a sorted List of Paths by their lifespan
   */
  @Override
  public List<Path> sort() {
    if (!requestedFiles.isEmpty()) {
      requestedFiles.sort(Comparator.comparingLong(path -> {
        try {
          BasicFileAttributes attributes =
              Files.readAttributes(path, BasicFileAttributes.class);
          return attributes.creationTime().toMillis();
        } catch (IOException e) {
          throw new RuntimeException("Sorter unable to find attributes of current file");
        }
      }));
      return requestedFiles;
    } else {
      return super.sort();
    }
  }
}
