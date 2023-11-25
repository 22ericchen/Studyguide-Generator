package cs3500.pa02.view;

import cs3500.pa02.model.Events;

/**
 * Class for Displaying messages to the user
 */
public class Viewer {
  private int questionCounter = 1;

  /**
   * Viewer constructor
   */
  public Viewer() {
  }

  /**
   * Determines which study session prompt to give to the user based
   * on the passed enumerated Event
   *
   * @param event Event Enum class that represents individual events
   */
  public void promptUser(Events event) {
    switch (event) {
      case GREET_USER -> System.out.println("\nWelcome to Spaced Recognition Practice!");
      case NUM_QUESTIONS -> System.out.println("How many questions would you like to practice?");
      case SHOW_OPTIONS -> System.out.println("Choose an option:"
          + " \n1. Mark Easy 2. Mark Hard 3. Show Answer 4. Exit");
      case COMPLETE -> System.out.println("Great Job!");
      case STUDYGUIDE_COMPLETE -> System.out.println("Study Guide Successfully made!");
      default -> throw new IllegalArgumentException("Not a valid user event");
    }
  }

  /**
   * Determines which error to show to the user based on the passed enumerated
   * Event
   *
   * @param event Event Enum class that represents individual error events
   */
  public void showError(Events event) {
    switch (event) {
      case INVALID_INT -> System.out.println("Invalid int. Please provide a valid integer.");
      case INVALID_FILE -> System.out.println("Please provide a valid File.");
      case INVALID_INT_OR_STR -> System.out.println("Invalid option. Please provide a valid integer or String.");
      default -> throw new IllegalArgumentException("Not a valid error event");
    }
  }

  /**
   * Shows the passed question to user along with the question number.
   *
   * @param question The String of the question
   */
  public void showQuestion(String question) {
    System.out.println("question " + (questionCounter++) + "\n" + question);
  }

  /**
   * Shows the passed answer parameter to the user
   *
   * @param answer The String of the answer
   */
  public void showAnswer(String answer) {
    System.out.println(answer);
  }

  /**
   * shows the user the study sessions statistics including
   * the number of the questions that were shown, the number of
   * questions that changed from easy to hard, the number of questions
   * that changed from hard to easy, the total number of hard questions,
   * and the total number of easy questions
   *
   * @param numToHard int of the number of questions marked easy to hard
   * @param numToEasy int of the number of questions marked hard to easy
   * @param numHard int of the number of hard questions
   * @param numEasy int of the number of easy questions
   */
  public void showSummary(int numToHard, int numToEasy, int numHard, int numEasy) {
    System.out.println("\n**SESSION SUMMARY**\nQuestions Answered: " + (questionCounter - 1));
    System.out.println("Questions Changed From Easy to Hard: " + numToHard);
    System.out.println("Questions Changed From Hard to Easy: " + numToEasy);
    System.out.println("Number of Hard Questions: " + numHard);
    System.out.println("Number of Easy Questions: " + numEasy);
  }
}
