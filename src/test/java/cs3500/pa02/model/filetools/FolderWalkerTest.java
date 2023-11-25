package cs3500.pa02.model.filetools;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * test class for FolderWalker Class
 */
public class FolderWalkerTest {
  private static final Path TEST_DIRECTORY = Paths.get("test-directory");

  /**
   * Tests if getFiles method walks through every file in a directory and returns
   * every .md file in a list of Paths
   */
  @Test
  public void testGetFiles() {
    // Create a temporary test directory with files in it
    try {
      Files.createDirectory(TEST_DIRECTORY);
      createFilesInTestDirectory("file1.md", "file2.txt", "file3.md", "file4.md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    FolderWalker folderWalker = new FolderWalker(TEST_DIRECTORY);
    List<Path> files = folderWalker.getFiles();

    // Verify that only the files with the ".md" extension are returned
    assertEquals(3, files.size());
    assertTrue(files.contains(TEST_DIRECTORY.resolve("file1.md")));
    assertTrue(files.contains(TEST_DIRECTORY.resolve("file3.md")));
    assertTrue(files.contains(TEST_DIRECTORY.resolve("file4.md")));

    // Delete the test directory and its contents
    deleteTestDirectory();
  }

  /**
   * Tests if getFiles catches exception when it is passed a nonexistent directory
   */
  @Test
  public void testGetFilesException() {
    // Provide a non-existent directory path
    Path nonExistentDirectory = Paths.get("nonexistent-directory");

    // Verify that a RuntimeException is thrown when the directory is not found
    FolderWalker folderWalker = new FolderWalker(nonExistentDirectory);
    assertThrows(RuntimeException.class, folderWalker::getFiles);
  }

  /**
   * private helper method to delete the test Directory and all its the files
   */
  private void deleteTestDirectory() {
    try {
      Files.walk(TEST_DIRECTORY)
          .sorted((path1, path2) -> -path1.compareTo(path2))
          .forEach(path -> {
            try {

              Files.delete(path);
            } catch (IOException e) {
              throw new RuntimeException("Failed to delete test directory: ");
            }
          });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void createFilesInTestDirectory(String... filenames) throws IOException {
    for (String filename : filenames) {
      Path filePath = TEST_DIRECTORY.resolve(filename);
      Files.createFile(filePath);
    }
  }
}
