package cs3500.pa02.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateSummaryTest {
  private final String testStudyGuideFile = "src/test/resources/studyguide.md";
  private Path tempDirectory;

  /**
   * setup method before each test method
   */
  @BeforeEach
  public void setUp() {
    try {
      //create a temp directory with files with text that are out of order
      tempDirectory = Files.createTempDirectory("testData");
      createTestFile(tempDirectory, "D.md", "# Header 1\nSome text [[Info1]]");
      Thread.sleep(1000);
      createTestFile(tempDirectory, "B.md", "## Header 2\n[[Info2\n Continued Info]]");
      Thread.sleep(1000);
      createTestFile(tempDirectory, "C.md", "### Header 3\n[[Info3 with[]]]");
      Thread.sleep(1000);
      createTestFile(tempDirectory, "A.md", "#### Header 4\n[[Info4]] some text");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * tests run with modified ordering flag
   */
  @Test
  public void testRunModified() {
    CreateSummary summary = new CreateSummary(tempDirectory, "modified", testStudyGuideFile);
    //calling run method
    summary.run();

    assertTrue(tempDirectory.toFile().exists());

    List<String> lines = readAllLines(testStudyGuideFile);
    assertEquals(8, lines.size());

    assertEquals("#### Header 4", lines.get(0));
    assertEquals("- Info4", lines.get(1));
    assertEquals("### Header 3", lines.get(2));
    assertEquals("- Info3 with[]", lines.get(3));
    assertEquals("## Header 2", lines.get(4));
    assertEquals("- Info2 Continued Info", lines.get(5));
    assertEquals("# Header 1", lines.get(6));
    assertEquals("- Info1", lines.get(7));
  }

  /**
   * tests run with filename ordering flag
   */
  @Test
  public void testRunFilename() {
      CreateSummary summary = new CreateSummary(tempDirectory, "filename", testStudyGuideFile);
      summary.run();
      //check if the file was created
      assertTrue(tempDirectory.toFile().exists());

      List<String> lines = readAllLines(testStudyGuideFile);
      //check if the number of lines in the file is correct
      assertEquals(8, lines.size());
      //check if the content is correct
      assertEquals("#### Header 4", lines.get(0));
      assertEquals("- Info4", lines.get(1));
      assertEquals("## Header 2", lines.get(2));
      assertEquals("- Info2 Continued Info", lines.get(3));
      assertEquals("### Header 3", lines.get(4));
      assertEquals("- Info3 with[]", lines.get(5));
      assertEquals("# Header 1", lines.get(6));
      assertEquals("- Info1", lines.get(7));
  }

  /**
   * tests run with created ordering flag
   */
  @Test
  public void testRunCreated() {
    CreateSummary summary = new CreateSummary(tempDirectory, "created", testStudyGuideFile);
    summary.run();

    //check if the file was created
    assertTrue(tempDirectory.toFile().exists());

    List<String> lines = readAllLines(testStudyGuideFile);
    //check if there are the proper number of lines in the file
    assertEquals(8, lines.size());
    //check if the content is correct
    assertEquals("# Header 1", lines.get(0));
    assertEquals("- Info1", lines.get(1));
    assertEquals("## Header 2", lines.get(2));
    assertEquals("- Info2 Continued Info", lines.get(3));
    assertEquals("### Header 3", lines.get(4));
    assertEquals("- Info3 with[]", lines.get(5));
    assertEquals("#### Header 4", lines.get(6));
    assertEquals("- Info4", lines.get(7));
  }

  /**
   * tests run with an invalid ordering flag
   */
  @Test
  public void testRunOrderingException() {
    CreateSummary summary = new CreateSummary(tempDirectory, "notAnOrderingFlag", testStudyGuideFile);
    assertThrows(IllegalArgumentException.class, () -> {summary.run();});
  }

  /**
   * tests run with an invalid directory path
   */
  @Test
  public void testRunDirectoryException() {
    //trying to create a summary with invalid path
    CreateSummary summary = new CreateSummary(Path.of("/nonexistent"), "filename", testStudyGuideFile);
    assertThrows(RuntimeException.class, () -> {summary.run();});
  }

  /**
   * Helper method to Create a temporary file in the temporary directory
   *
   * @param directory Path of the temp Directory
   * @param fileName reference name of the file
   * @param content the text content of the file
   */
  private void createTestFile(Path directory, String fileName, String content) {
    Path filePath = directory.resolve(fileName);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
      writer.write(content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Helper method to get every line of a given markdown file
   *
   * @param filePath Path to a file
   * @return a List of Strings of every line in the file
   */
  private List<String> readAllLines(String filePath) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return lines;
  }
}