package cs3500.pa02.model.filetools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for FileToText Class
 */
public class FileToTextTest {
  private FileToText fileToText;
  private Path tempDir;

  /**
   * setup method before each test method that instantiates fileToText and
   * a temporary directory
   */
  @BeforeEach
  public void setUp() {
    fileToText = new FileToText();
    try {
      tempDir = Files.createTempDirectory("tempdir");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tests if readThroughFile correctly takes each line from a file
   * and returns them in a List of Strings
   */
  @Test
  public void testReadThroughFile() {
    // Create a temporary file and write some lines to it
    Path tempFile;
    try {
      tempFile = Files.createTempFile(tempDir, "tempfile", ".txt");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    List<String> expectedLines = List.of(
        "Line 1",
        "Line 2",
        "Line 3"
    );
    try {
      Files.write(tempFile, expectedLines);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Read the lines from the file using FileToText
    List<String> actualLines = fileToText.readThroughFile(tempFile);

    // Verify that the lines are read correctly
    assertEquals(expectedLines, actualLines);
  }

  /**
   * Tests readThroughFile's ability to catch if the
   * file passed is not existent
   */
  @Test
  public void testReadThroughFile_FileNotFound() {
    // Provide a non-existent file path
    Path nonExistentFile = tempDir.resolve("nonexistent.txt");

    // Verify that a RuntimeException is thrown when the file is not found
    assertThrows(RuntimeException.class, () -> fileToText.readThroughFile(nonExistentFile));
  }

  /**
   * Tests readThroughFile's ability to catch if the file
   * it is trying to read is not existent
   */
  @Test
  public void testReadThroughFile_IOError() {
    // Create a temporary directory instead of a file
    Path tempSubDir;
    try {
      tempSubDir = Files.createDirectory(tempDir.resolve("subDir"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Verify that a RuntimeException is thrown when trying to read a directory
    Path finalTempSubDir = tempSubDir;
    assertThrows(RuntimeException.class, () -> fileToText.readThroughFile(finalTempSubDir));
  }
}
