package cs3500.pa02.model.generator;

import java.util.List;

/**
 * Text generator for constructing the Question Answer pairs
 * in the correct format
 */
public class QaGenerator implements TextGenerator {
  /**
   * QaGenerator constructor which calls its
   */
  public QaGenerator() {}

  /**
   * Generates a String form of the list of all the
   * question answer pairs and sets the difficulty as
   * hard.
   *
   * @param lines a List of Strings that should include
   *              all the text from a file
   * @return A String of QA pairs so that every new line is
   *         a pair
   */
  @Override
  public String generateText(List<String> lines) {
    String srText = "";
    StringBuilder tempSrText = new StringBuilder();
    int bracketCounter = 0;
    for (String line : lines) {
      //determine if it is a QA pair
      if (line.contains(":::")) {
        for (char c : line.toCharArray()) {
          if (c == '[') {
            bracketCounter++;
          } else if (c == ']') {
            bracketCounter--;
            //write text to the result String
            if (bracketCounter == 0 && tempSrText.length() > 0) {
              srText += tempSrText + " :: H" + "\n";
              tempSrText.setLength(0);
            }
          }
          //when double bracket is found
          if (bracketCounter == 2) {
            if (c != '[') {
              tempSrText.append(c);
            }
          } else if (bracketCounter > 2) {
            tempSrText.append(c);
          }
        }
      }
    }
    return srText;
  }
}
