package cs3500.pa02.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * QaBank Test class
 */
public class QaBankTest {
  private QaBank qaBank;

  /**
   * set up method that instantiates the qaBank
   */
  @BeforeEach
  public void setUp() {
    List<String> questions = Arrays.asList(
        "Q1 ::: A1 :: H",
        "Q2 ::: A2 :: H",
        "Q3 ::: A3 :: E",
        "Q4 ::: A4 :: E",
        "No QA :: No QA :: H"
    );
    qaBank = new QaBank(questions);
  }

  /**
   * tests the QaBank Constructor and determines if the number of questions in
   * each bank is correct
   */
  @Test
  public void testQaBankConstructor() {
    // Test the number of questions in the bank
    assertEquals(4, qaBank.getNumQuestions());
    assertEquals(2, qaBank.getNumHardQuestions());
    assertEquals(2, qaBank.getNumEasyQuestions());
  }

  /**
   * tests for the exceptional case that there are no questions given it the constructor
   */
  @Test
  public void testEmptyQaBankException() {
    assertThrows(IllegalArgumentException.class, ()-> {new QaBank(new ArrayList<>());});
  }

  /**
   * Tests the updateSrFile method to determine if it properly updates or writes to a file
   * passed as a parameter
   */
  @Test
  public void testUpdateSrFile(){
    File tempFile;
    try {
      tempFile = File.createTempFile("tempfile", ".sr");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    qaBank.updateSrFile(tempFile);

    // Read the contents of the file to verify the output
    String fileContent;
    try {
      fileContent = Files.readString(tempFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String expected = "Q1 ::: A1 :: H\nQ2 ::: A2 :: H\nQ4 ::: A4 :: E\nQ3 ::: A3 :: E\n";
    assertEquals(expected, fileContent);
  }

  /**
   * tests if the getQuestion method randomly returns a hard question if there are
   * hard questions and if it returns an easy questions once there are no more
   */
  @Test
  public void testGetQuestion() {
    List<String> questions = new ArrayList<>();
    questions.add(qaBank.getQuestion());
    questions.add(qaBank.getQuestion());

    // Verify that two different questions are returned and are hard questions
    assertEquals(2, questions.size());
    assertNotEquals(questions.get(0), questions.get(1));
    assertTrue(qaBank.shownHardQuestionBank.containsKey(questions.get(0)));
    assertTrue(qaBank.shownHardQuestionBank.containsKey(questions.get(1)));

    //verify once hard questions run out question is from easy question bank
    questions.add(qaBank.getQuestion());
    assertTrue(qaBank.shownEasyQuestionBank.containsKey(questions.get(2)));
  }

  /**
   * Tests if getAnswer returns the correct answer for each question
   */
  @Test
  public void testGetAnswer() {
    //move every question to shown question bank to test get answer
    qaBank.getQuestion();
    qaBank.getQuestion();
    qaBank.getQuestion();
    qaBank.getQuestion();

    //test if proper answers are given for each question
    String answer1 = qaBank.getAnswer("Q1 ");
    assertEquals(" A1 ", answer1);
    String answer2 = qaBank.getAnswer("Q2 ");
    assertEquals(" A2 ", answer2);
    String answer3 = qaBank.getAnswer("Q3 ");
    assertEquals(" A3 ", answer3);
    String answer4 = qaBank.getAnswer("Q4 ");
    assertEquals(" A4 ", answer4);
  }

  /**
   * tests if hardToEasy correctly switches question difficulty
   * if it was hard then it should be moved to the easy bank
   * if it was easy it should remain in the same bank
   */
  @Test
  public void testHardToEasy() {
    qaBank.getQuestion();
    qaBank.getQuestion();
    qaBank.getQuestion();
    qaBank.getQuestion();
    // Move a question from hard to easy
    qaBank.hardToEasy("Q1 ");

    // Verify the question is moved and counters are updated
    assertEquals(1, qaBank.getNumHardQuestions());
    assertEquals(3, qaBank.getNumEasyQuestions());
    assertEquals(1, qaBank.getChangedToEasy());

    qaBank.hardToEasy("Q3 ");
    // Verify that nothing has changed since it was easy to start with
    assertEquals(1, qaBank.getNumHardQuestions());
    assertEquals(3, qaBank.getNumEasyQuestions());
    assertEquals(1, qaBank.getChangedToEasy());
  }

  /**
   * tests if hardToEasy correctly switches question difficulty
   * if it was easy then it should be moved to the hard bank
   * if it was hard it should remain in the same bank
   */
  @Test
  public void testEasyToHard() {
    qaBank.getQuestion();
    qaBank.getQuestion();
    qaBank.getQuestion();
    qaBank.getQuestion();
    // Move a question from easy to hard
    qaBank.easyToHard("Q3 ");
    qaBank.easyToHard("Q1 ");

    // Verify the question is moved and counters are updated
    assertEquals(3, qaBank.getNumHardQuestions());
    assertEquals(1, qaBank.getNumEasyQuestions());
    assertEquals(1, qaBank.getChangedToHard());

    qaBank.easyToHard("Q1 ");
    // Verify nothing has changed since it was hard to begin with
    assertEquals(3, qaBank.getNumHardQuestions());
    assertEquals(1, qaBank.getNumEasyQuestions());
    assertEquals(1, qaBank.getChangedToHard());
  }
}
 
