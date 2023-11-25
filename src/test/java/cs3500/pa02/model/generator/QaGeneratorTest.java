package cs3500.pa02.model.generator;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QaGeneratorTest {

  /**
   * Tests if generateText method correctly takes important
   * question answer pairs and returns them as a String
   */
  @Test
  public void testGenerateText() {
    // Create a list of lines representing a file with question answer pairs
    List<String> lines = new ArrayList<>();
    lines.add("[[Question 1:::Answer 1]]");
    lines.add("not important text");
    lines.add("[[Question 2:::Answer 2]]");
    lines.add("not important text");
    lines.add("[[Question 3:::Answer 3]]");
    lines.add("[[not a Question answer pair]]");
    lines.add("[[Question with []::: answer with []]]");

    // Use QaGenerator to generate the question-answer pairs text
    QaGenerator generator = new QaGenerator();
    String qaPairsText = generator.generateText(lines);

    // Verify that the question answer pairs text is generated correctly
    String expectedQaPairsText =
        "Question 1:::Answer 1 :: H\n" +
            "Question 2:::Answer 2 :: H\n" +
            "Question 3:::Answer 3 :: H\n" +
            "Question with []::: answer with [] :: H\n";
    assertEquals(expectedQaPairsText, qaPairsText);
  }

  @Test
  public void testGenerateText_NoPairs() {
    // Create a list of lines without any important question answer pairs
    List<String> lines = new ArrayList<>();
    lines.add("not important text");
    lines.add("[[not a Question answer pair]]");
    lines.add("not important text with :::");

    // Use QaGenerator to generate the question answer pairs text
    QaGenerator generator = new QaGenerator();
    String qaPairsText = generator.generateText(lines);

    // Verify that an empty string is returned since there are no question answer pairs
    assertEquals("", qaPairsText);
  }
}
