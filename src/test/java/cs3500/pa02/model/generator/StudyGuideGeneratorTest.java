package cs3500.pa02.model.generator;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * StudyGuideGeneratorTest
 */
public class StudyGuideGeneratorTest {

  /**
   * tests if the GenerateText method correctly pulls out
   * important headers
   */
  @Test
  public void testGenerateTextHeaders() {
    // Create a list of headers
    List<String> lines = new ArrayList<>();
    lines.add("# Chapter 1");
    lines.add("not important text");
    lines.add("## Chapter 2");
    lines.add("not important text");
    lines.add("### Section 3");
    lines.add("not important text");
    lines.add("#### section 4");

    // Use StudyGuideGenerator to generate the study guide text
    StudyGuideGenerator generator = new StudyGuideGenerator();
    String studyGuide = generator.generateText(lines);

    // Verify that the study guide is generated correctly
    String expectedStudyGuide =
        "# Chapter 1\n## Chapter 2\n### Section 3\n#### section 4\n";
    assertEquals(expectedStudyGuide, studyGuide);
  }

  /**
   * method to test if the generateText method correct takes
   * and formats important lines
   */
  @Test
  public void testGenerateText() {
    //list of important lines
    List<String> lines = new ArrayList<>();
    lines.add("[[this is important text]]");
    lines.add("[[text with [] in it]]");
    lines.add("[[continuous text");
    lines.add(" continues here]]");
    //add line with a question answer pair
    lines.add("[[ has a ::: Question Answer pair]]");

    // Use StudyGuideGenerator to generate the study guide text
    StudyGuideGenerator generator = new StudyGuideGenerator();
    String studyGuide = generator.generateText(lines);

    // Verify that the study guide is generated correctly
    String expectedStudyGuide =
        "- this is important text\n" +
            "- text with [] in it\n" +
            "- continuous text continues here\n";
     assertEquals(expectedStudyGuide, studyGuide);
  }
}
