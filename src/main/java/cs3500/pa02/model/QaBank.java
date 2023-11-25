package cs3500.pa02.model;

import cs3500.pa02.model.filetools.FileParser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Instance class that stores and access every question
 * answer pair that is given to it using Hashmaps
 */
public class QaBank {
  private final int numQuestions;
  private Map<String, String> hardQuestionBank = new HashMap<>();
  private Map<String, String> easyQuestionBank = new HashMap<>();
  protected Map<String, String> shownHardQuestionBank = new HashMap<>();
  protected Map<String, String> shownEasyQuestionBank = new HashMap<>();
  private int changedToEasy = 0;
  private int changedToHard = 0;

  /**
   * QABank constructor that takes every  valid question answer pair
   * from a list of String lines and puts them into the proper
   * hashmap bank depending on their difficulty
   *
   * @param questions A List of Strings that contain lines from a file
   */
  public QaBank(List<String> questions) {
    if (questions.isEmpty()) {
      throw new IllegalArgumentException("There must be existing questions");
    }
    for (String qa : questions) {
      if (qa.contains(":::")) {
        String question = qa.substring(0, qa.indexOf(":::"));
        String answer = qa.substring(qa.indexOf(":::") + 3, qa.length() - 4);
        if (qa.endsWith("H")) {
          hardQuestionBank.put(question, answer);
        } else {
          easyQuestionBank.put(question, answer);
        }
      }
    }
    numQuestions = hardQuestionBank.size() + easyQuestionBank.size();
  }

  /**
   * method to update a file given in the argument based on the questions
   * in the question hashmap bank fields using FileParser class
   *
   * @param file A File that needs to updated or written in
   */
  public void updateSrFile(File file) {
    String updatedQa = "";
    for (Map.Entry<String, String> current : hardQuestionBank.entrySet()) {
      updatedQa += (current.getKey() + ":::" + current.getValue() + ":: H\n");
    }
    for (Map.Entry<String, String> current : shownHardQuestionBank.entrySet()) {
      updatedQa += (current.getKey() + ":::" + current.getValue() + ":: H\n");
    }
    for (Map.Entry<String, String> current : easyQuestionBank.entrySet()) {
      updatedQa += (current.getKey() + ":::" + current.getValue() + ":: E\n");
    }
    for (Map.Entry<String, String> current : shownEasyQuestionBank.entrySet()) {
      updatedQa += (current.getKey() + ":::" + current.getValue() + ":: E\n");
    }
    FileParser updater = new FileParser(file.toPath().toString());
    updater.parseFile(updatedQa);
  }

  /**
   * getter method for the size of the initial question bank
   *
   * @return an int of the number of questions in the bank
   */
  public int getNumQuestions() {
    return numQuestions;
  }

  /**
   * getter method for the number of hard questions that
   * exist in the banks
   *
   * @return an int of the sum of the size of the two hard
   *        hashmaps
   */
  public int getNumHardQuestions() {
    return hardQuestionBank.size() + shownHardQuestionBank.size();
  }

  /**
   * getter method for the number of easy questiosn that
   * exist in the banks
   *
   * @return an int of the sum of the size of the two easy
   *        hashmaps
   */
  public int getNumEasyQuestions() {
    return easyQuestionBank.size() + shownEasyQuestionBank.size();
  }

  /**
   * method for deciding which private getter method
   * to use to either get an easy or hard question, calling
   * the hard question getter if there are hard questions
   * left
   *
   * @return the key of the QA set which is the String of the
   *         question
   */
  public String getQuestion() {
    if (hardQuestionBank.size() > 0) {
      return getHardQuestion();
    } else {
      return getEasyQuestion();
    }
  }

  /**
   * getter method for the answer Strings of the answers
   * for a question
   *
   * @param key the question String
   * @return A String of the answer from the hashmap
   */
  public String getAnswer(String key) {
    if (shownHardQuestionBank.containsKey(key)) {
      return shownHardQuestionBank.get(key);
    } else {
      return shownEasyQuestionBank.get(key);
    }
  }

  /**
   * The getter method for the number of questions changed
   * from hard to easy difficulty
   *
   * @return an int of the number of questions that switch from H to E
   */
  public int getChangedToEasy() {
    return changedToEasy;
  }

  /**
   * The getter method for the number of questions changed
   * from easy to hard difficulty
   *
   * @return an int of the number of questions that switch from E to H
   */
  public int getChangedToHard() {
    return changedToHard;
  }

  /**
   * takes a key and map value of the question answer pair if it
   * is in the shown hard hashmap and puts it in the shown easy
   * hashmap and removes it from the previous hashmap. Increments
   * a counter for the number of questions that shift from H to E
   *
   * @param key the String of the question
   */
  public void hardToEasy(String key) {
    if (shownHardQuestionBank.containsKey(key)) {
      shownEasyQuestionBank.put(key, shownHardQuestionBank.get(key));
      shownHardQuestionBank.remove(key);
      changedToEasy++;
    }
  }

  /**
   * takes a key and map value of the question answer pair if it
   * is in the shown Easy hashmap and puts it in the shown Hard
   * hashmap and removes it from the previous hashmap. Increments
   * a counter for the number of questions that shift from E to H
   *
   * @param key the String of the question
   */
  public void easyToHard(String key) {
    if (shownEasyQuestionBank.containsKey(key)) {
      shownHardQuestionBank.put(key, shownEasyQuestionBank.get(key));
      shownEasyQuestionBank.remove(key);
      changedToHard++;
    }
  }

  /**
   * Private helper method to return a random key from the hard hashmap
   * as the question and moves it to the shown hard hashmap
   *
   * @return the String of the question
   */
  private String getHardQuestion() {
    List<String> keysAsArray = new ArrayList<>(hardQuestionBank.keySet());
    int randIndex = (int) (Math.random() * (hardQuestionBank.size() - 1));
    String question = keysAsArray.get(randIndex);
    shownHardQuestionBank.put(question, hardQuestionBank.get(question));
    hardQuestionBank.remove(question);
    return question;
  }

  /**
   * Private helper method to return a random key from the easy hashmap
   * as the question and moves it to the shown easy hashmap
   *
   * @return the String of the question
   */
  private String getEasyQuestion() {
    List<String> keysAsArray = new ArrayList<>(easyQuestionBank.keySet());
    int randIndex = (int) (Math.random() * (easyQuestionBank.size() - 1));
    String question = keysAsArray.get(randIndex);
    shownEasyQuestionBank.put(question, easyQuestionBank.get(question));
    easyQuestionBank.remove(question);
    return question;
  }

}
