package cs3500.pa02.model.generator;

import java.util.List;

/**
 *StudyGuideReader inherits from AbstractReader and builds
 * a usable string representation of a summaraized study
 * guide
 */
public class StudyGuideGenerator implements TextGenerator {
  public StudyGuideGenerator() {
    super();
  }

  /**
   * takes a list of Strings that represent a file's text
   * and picks out headers and important info into a String,
   * separating each new line.
   *
   * @param lines a List of Strings that should represent a file's text
   * @return a String representation of the study guide
   */
  @Override
  public String generateText(List<String> lines) {
    String studyGuide = "";
    StringBuilder tempStudyGuide = new StringBuilder();

    int bracketCounter = 0;
    for (String line : lines) {
      //determine if it isn't a QA pair
      if (!line.contains(":::")) {
        if (line.startsWith("# ") || line.startsWith("## ")
            || line.startsWith("### ") || line.startsWith("#### ")) {
          studyGuide += (line + "\n");
        } else {
          for (char c : line.toCharArray()) {
            if (c == '[') {
              bracketCounter++;
            } else if (c == ']') {
              bracketCounter--;
              //write text to the result String
              if (bracketCounter == 0 && tempStudyGuide.length() > 0) {
                studyGuide += "- " + tempStudyGuide + "\n";
                tempStudyGuide.setLength(0);
              }
            }
            //when double bracket is found
            if (bracketCounter == 2) {
              if (c != '[') {
                tempStudyGuide.append(c);
              }
            } else if (bracketCounter > 2) {
              tempStudyGuide.append(c);
            }
          }
        }
      }
    }
    return studyGuide;
  }
}
