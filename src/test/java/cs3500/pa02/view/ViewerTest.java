package cs3500.pa02.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import cs3500.pa02.model.Events;
import static org.junit.jupiter.api.Assertions.*;

public class ViewerTest {
  private Viewer viewer;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  /**
   * setup before each test that instantiates the Viewer object and ByteArrayOutputStream
   */
  @BeforeEach
  public void setUp() {
    viewer = new Viewer();
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  /**
   * tests promptUser method by calling and passing every possible valid Event
   * and asserting that the print stream output is correct
   */
  @Test
  public void testPromptUser() {
    viewer.promptUser(Events.GREET_USER);
    assertEquals("Welcome to Spaced Recognition Practice!", outputStreamCaptor.toString().trim());

    outputStreamCaptor.reset();

    viewer.promptUser(Events.NUM_QUESTIONS);
    assertEquals("How many questions would you like to practice?", outputStreamCaptor.toString().trim());

    outputStreamCaptor.reset();

    viewer.promptUser(Events.SHOW_OPTIONS);
    assertEquals("Choose an option: \n1. Mark Easy 2. Mark Hard 3. Show Answer 4. Exit", outputStreamCaptor.toString().trim());

    outputStreamCaptor.reset();

    viewer.promptUser(Events.COMPLETE);
    assertEquals("Great Job!", outputStreamCaptor.toString().trim());

    outputStreamCaptor.reset();

    viewer.promptUser(Events.STUDYGUIDE_COMPLETE);
    assertEquals("Study Guide Successfully made!", outputStreamCaptor.toString().trim());
  }

  /**
   * Tests promptUser's ability to catch and throw exceptions if invalid event is passed
   */
  @Test
  public void testPromptUserException() {
    assertThrows(IllegalArgumentException.class, () -> {viewer.promptUser(Events.INVALID_FILE);});
  }

  /**
   * Tests showError method by calling each possible event and asserting that it equals
   * to the proper print stream output
   */
  @Test
  public void testShowError() {
    viewer.showError(Events.INVALID_INT);
    assertEquals("Invalid int. Please provide a valid integer.", outputStreamCaptor.toString().trim());

    outputStreamCaptor.reset();

    viewer.showError(Events.INVALID_FILE);
    assertEquals("Please provide a valid File.", outputStreamCaptor.toString().trim());

    outputStreamCaptor.reset();

    viewer.showError(Events.INVALID_INT_OR_STR);
    assertEquals("Invalid option. Please provide a valid integer or String.", outputStreamCaptor.toString().trim());
  }

  /**
   *Tests showError's ability to catch throw an IllegalArgumentException if an invalid Event is
   * inputted
   */
  @Test
  public void testShowErrorException() {
    assertThrows(IllegalArgumentException.class, () -> {viewer.showError(Events.NUM_QUESTIONS);});
  }

  /**
   * Tests the showQuestion method by making method calls and comparing
   * the print stream output to the expected String. Tests if the
   * question number increments properly as well
   */
  @Test
  public void testShowQuestion() {
    viewer.showQuestion("What is 1 + 1 = ?");
    assertEquals("question 1\nWhat is 1 + 1 = ?", outputStreamCaptor.toString().trim());

    //clearing the output stream reader
    outputStreamCaptor.reset();

    viewer.showQuestion("HELLO WORLD!");
    assertEquals("question 2\nHELLO WORLD!", outputStreamCaptor.toString().trim());
  }

  /**
   * Tests the ShowAnswer method by making method call and asserting their
   * equality to expected Strings
   */
  @Test
  public void testShowAnswer() {
    viewer.showAnswer("HELLO WORLD");
    assertEquals("HELLO WORLD", outputStreamCaptor.toString().trim());

    //clearing the output stream reader
    outputStreamCaptor.reset();

    viewer.showAnswer("1234567890");
    assertEquals("1234567890", outputStreamCaptor.toString().trim());
  }

  /**
   * tests the show summary method by making a method call and then compares the
   * String to the expected String.
   */
  @Test
  public void testShowSummary() {
    viewer.showSummary(2, 1, 3, 4);
    String expectedOutput = "**SESSION SUMMARY**\n" +
        "Questions Answered: 0\n" +
        "Questions Changed From Easy to Hard: 2\n" +
        "Questions Changed From Hard to Easy: 1\n" +
        "Number of Hard Questions: 3\n" +
        "Number of Easy Questions: 4";

    assertEquals(expectedOutput, normalizeOutput(outputStreamCaptor.toString()));
  }

  /**
   * private helper method to remove extra trailing spaces
   * or line spacers
   *
   * @param output the string that needs to be reformatted
   * @return the reformatted string
   */
  private String normalizeOutput(String output) {
    return output.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
  }
}
