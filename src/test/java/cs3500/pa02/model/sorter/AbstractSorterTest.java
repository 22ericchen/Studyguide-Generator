package cs3500.pa02.model.sorter;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractSorterTest {
  private Path tempFile1;
  private Path tempFile2;
  private Path tempFile3;
  private Path tempFile4;
  private List<Path> paths;

  /**
   * set up method before each test method that instantiates a list
   * of temp files
   */
  @BeforeEach
  public void setUp() {
    try {
      tempFile1 = Files.createTempFile("D", ".md");
      Thread.sleep(500);
      tempFile2 = Files.createTempFile("B", ".md");
      Thread.sleep(500);
      tempFile3 = Files.createTempFile("C", ".md");
      tempFile4 = Files.createTempFile("A", ".md");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    paths = new ArrayList<>(Arrays.asList(tempFile1, tempFile2, tempFile3, tempFile4));
  }

  /**
   * Tests if a ModifiedSorter object sorts the list of paths
   * in the order of recently modified to oldest modified
   */
  @Test
  public void testModifiedSorter() {
    AbstractSorter sorter = new ModifiedSorter(paths);
    List<Path> sortedPaths = sorter.sort();
    //should be a.md, c.md, b.md, d.md
    assertEquals(tempFile4, sortedPaths.get(0));
    assertEquals(tempFile3, sortedPaths.get(1));
    assertEquals(tempFile2, sortedPaths.get(2));
    assertEquals(tempFile1, sortedPaths.get(3));
    AbstractSorter sorter2 = new ModifiedSorter(new ArrayList<>());
    assertNull(sorter2.sort());
  }

  /**
   * Tests if a modifiedSorter object catches when it tries to sort a path
   * that doesn't exist
   */
  @Test
  public void testModifiedSorterException() {
    AbstractSorter sorter = new ModifiedSorter(paths);
    paths.add(Path.of("/nonexistant.md"));
    assertThrows(RuntimeException.class, ()-> {sorter.sort();});
  }

  /**
   * Tests if the FileNameSorter object sorts the List of Paths by
   * their alphabetical order
   */
  @Test
  public void testFilenameSorter() {
    AbstractSorter sorter = new FilenameSorter(paths);
    List<Path> sortedPaths = sorter.sort();
    //should be a.md, b.md, c.md, d.md
    assertEquals(tempFile4, sortedPaths.get(0));
    assertEquals(tempFile2, sortedPaths.get(1));
    assertEquals(tempFile3, sortedPaths.get(2));
    assertEquals(tempFile1, sortedPaths.get(3));
    AbstractSorter sorter2 = new FilenameSorter(new ArrayList<>());
    assertNull(sorter2.sort());
  }

  /**
   * Tests if a CreatedSorterObject sorts a list of paths by their
   * creation time stamp
   */
  @Test
  public void testCreatedSorter() {
    AbstractSorter sorter = new CreatedSorter(paths);
    List<Path> sortedPaths = sorter.sort();
    //Should be d.md, b.md, c.md, a.md
    assertEquals(tempFile1, sortedPaths.get(0));
    assertEquals(tempFile2, sortedPaths.get(1));
    assertEquals(tempFile3, sortedPaths.get(2));
    assertEquals(tempFile4, sortedPaths.get(3));
    AbstractSorter sorter2 = new CreatedSorter(new ArrayList<>());
    assertNull(sorter2.sort());
  }

  /**
   * Tests if a CreatedSorter object catches when it tries to sort a path
   * that doesn't exist
   */
  @Test
  public void testCreatedSorterException() {
    AbstractSorter sorter = new CreatedSorter(paths);
    paths.add(Path.of("/nonexistant.md"));
    assertThrows(RuntimeException.class, ()-> {sorter.sort();});
  }

}