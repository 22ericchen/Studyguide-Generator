package cs3500.pa02.model.filetools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test Class for FileParser class
 */
public class FileParserTest {
  private FileParser fileParser;
  private Path testFilePath;

  /**
   * set up method that instantiates testFilePath and a FileParser object
   * before each test method
   */
  @BeforeEach
  public void setUp() {
    // Create a temporary directory
    try {
      testFilePath = Files.createTempDirectory("fileparsertest");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // Provide a non-writable path to simulate an IOException
    fileParser = new FileParser(testFilePath.resolve("testfile.sr").toString());
  }

  /**
   * Tests the is parseFile method correctly parses the String to the temporary
   * file
   */
  @Test
  public void testParseFile() {
    String testText = "Test String\nTeststring";
    fileParser.parseFile(testText);

    Path filePath = Paths.get(testFilePath.resolve("testfile.sr").toString());
    // Read the contents of the file to verify the output
    String fileContent;
    try {
      fileContent = Files.readString(filePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(testText, fileContent);

    // Clean up the test file after the test
    try {
      Files.deleteIfExists(filePath);
      Files.deleteIfExists(testFilePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * tests the constructors ability to catch an invalid file that was passed to it
   */
  @Test
  public void testFileParserConstructorException() {
    assertThrows(RuntimeException.class, () -> {new FileParser("/nonexistent/testfile.sr");});
  }
}
