package cs3500.pa02.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Spaced recognition practice controller
 */
class BeginSpacedRecognitionTest {
  private ByteArrayOutputStream outputStreamCaptor;
  Path tempFile;
  Path emptyTempFile;

  /**
   * setup method for each test method, instantiates ByteArrayOutputStream and
   * test temp files
   */
  @BeforeEach
  public void setUp() {
    //initialize console output stream reader
    outputStreamCaptor = new ByteArrayOutputStream();
    try {
      //initialize temp files
      tempFile = Files.createTempFile("testFile", ".sr");
      emptyTempFile = Files.createTempFile("emptyFile", ".sr");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    //set the QA's for the .sr file
    setUpTestFile(tempFile, "Q1:::A1:: H\nQ2:::A2:: H\nQ3:::A3:: E");
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  /**
   * tests if controller catches if a passed .sr file is empty
   */
  @Test
  public void testRunEmptyFile() {
    //user input of an empty .sr file
    String userInputs = emptyTempFile.toString();
    Readable input = new StringReader(userInputs);

    BeginSpacedRecognition studySession = new BeginSpacedRecognition(input);
    assertThrows(IllegalArgumentException.class, () -> {studySession.run();});
  }

  /**
   * tests if run method works normally without error and corresponding
   * inputs get an expected summary
   */
  @Test
  public void testRun() {
    //user input of an .sr file, 3 questions, mark easy, mark hard, show answer, and mark easy
    String userInputs = tempFile.toString() + "\n3\n1\n2\n3\n1";
    Readable input = new StringReader(userInputs);

    BeginSpacedRecognition studySession = new BeginSpacedRecognition(input);
    //run the study session
    studySession.run();
    //expected Greeting
    String expectedGreeting = "Welcome to Spaced Recognition Practice!\n";
    //expected Prompt for user to provide valid file
    String expectedFilePrompt = "Please provide a valid File.\n";
    //expected prompt for user to provide number of questions to practice
    String expectedNumQuestionsPrompt = "How many questions would you like to practice?\n";
    //expected summary output
    String expectedSummaryOutput =
        "**SESSION SUMMARY**\n"  +
        "Questions Answered: 3\n" +
        "Questions Changed From Easy to Hard: 0\n" +
        "Questions Changed From Hard to Easy: 1\n" +
        "Number of Hard Questions: 1\n" +
        "Number of Easy Questions: 2";

    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedFilePrompt));
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedNumQuestionsPrompt));
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedGreeting));
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedSummaryOutput));
  }

  @Test
  public void testRunExit() {
    //user input of an .sr file, user exits after first question is shown
    String userInputs = tempFile.toString() + "\n3\n4";
    Readable input = new StringReader(userInputs);

    BeginSpacedRecognition studySession = new BeginSpacedRecognition(input);
    //run the study session
    studySession.run();
    //expected summary output, only one questions should be shown
    //nothing else should change
    String expectedSummaryOutput =
        "**SESSION SUMMARY**\n"  +
            "Questions Answered: 1\n" +
            "Questions Changed From Easy to Hard: 0\n" +
            "Questions Changed From Hard to Easy: 0\n" +
            "Number of Hard Questions: 2\n" +
            "Number of Easy Questions: 1";
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedSummaryOutput));
  }
  /**
   * tests if run method works normally without error and corresponding
   * String inputs get an expected summary
   */
  @Test
  public void testRunWithStringInputs() {
    //user input of an .sr file, 3 questions, mark easy, mark easy, show answer, and mark hard
    String userInputs = tempFile.toString() + "\n3\nmark easy\nMark Easy\nShow answer\nmark Hard";
    Readable input = new StringReader(userInputs);

    BeginSpacedRecognition studySession = new BeginSpacedRecognition(input);
    //start study session
    studySession.run();
    //expected summary output
    String expectedSummaryOutput =
        "**SESSION SUMMARY**\n"  +
            "Questions Answered: 3\n" +
            "Questions Changed From Easy to Hard: 1\n" +
            "Questions Changed From Hard to Easy: 2\n" +
            "Number of Hard Questions: 1\n" +
            "Number of Easy Questions: 2";
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedSummaryOutput));
  }

  /**
   * tests if run method works normally without error and corresponding
   * inputs get an expected summary despite invalid inputs
   */
  @Test
  public void testRunWithPoorInputs() {
    //simulates user inputting options that don't abide by what the program asks for
    String userInputs = "Not a path\n" + tempFile.toString() + "\ninvalid number\n-1\n10\nnot option\n1\n3\nExit";
    Readable input = new StringReader(userInputs);
    BeginSpacedRecognition studySession = new BeginSpacedRecognition(input);
    //start the study session
    studySession.run();
    //expected error messages shown to the user, but no errors should be thrown
    String expectedErrorMessage1 = "Invalid int. Please provide a valid integer.";
    String expectedErrorMessage2 = "Invalid option. Please provide a valid integer or String.";
    //expected summary output
    String expectedSummaryOutput =
        "**SESSION SUMMARY**\n"  +
            "Questions Answered: 2\n" +
            "Questions Changed From Easy to Hard: 0\n" +
            "Questions Changed From Hard to Easy: 1\n" +
            "Number of Hard Questions: 1\n" +
            "Number of Easy Questions: 2";
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedErrorMessage1));
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedErrorMessage2));
    assertTrue(normalizeOutput(outputStreamCaptor.toString()).contains(expectedSummaryOutput));
  }

  /**
   * Helper method to Create a temporary file in the temporary directory
   *
   * @param file reference name of the file
   * @param content the text content of the file
   */
  private void setUpTestFile(Path file, String content) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
      writer.write(content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * private helper method to remove extra trailing spaces
   * or indents
   * @param output the string that needs to be reformatted
   * @return the reformatted string
   */
  private String normalizeOutput(String output) {
    return output.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
  }
}