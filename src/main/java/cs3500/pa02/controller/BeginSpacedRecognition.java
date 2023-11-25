package cs3500.pa02.controller;

import cs3500.pa02.model.Events;
import cs3500.pa02.model.QaBank;
import cs3500.pa02.model.filetools.FileToText;
import cs3500.pa02.view.Viewer;
import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Begins the Spaced Recognition practice and directs the user's Readable
 * inputs. Prompts the user for a valid .sr file and generates a question
 * bank from the QA's within the file and then provides questions based on
 * how many the user requested. Once the user exits or the session finishes
 * a summary is shown for the user.
 */
public class BeginSpacedRecognition implements Controller {
  private final Scanner input;
  private final Viewer view = new Viewer();
  private QaBank questionBank;
  private File srFile = null;
  private int counter = 0;
  private boolean finished = false;

  /**
   * Constructor for BeginSpacedRecognition which sets the
   * Scanner reference variable, input.
   *
   * @param input the InputStreamReader
   */
  public BeginSpacedRecognition(Readable input) {
    this.input = new Scanner(input);
  }

  /**
   * Directs the flow of user inputs and structures the study session
   * by calling private helper methods below
   */
  @Override
  public void run() {
    view.promptUser(Events.GREET_USER);
    //determine the .sr file path the user wants to use
    srFile = setSrFile();
    //generate question bank
    generateQuestionBank();
    //determine how many questions the user wants
    int numQuestions = setNumQuestions();

    while (!finished) {
      String question = questionBank.getQuestion();
      view.showQuestion(question);
      counter++;
      takeQuestionInput(question);
      if (counter == numQuestions) {
        exit();
      }
    }
  }

  /**
   * private helper method that sets the finished boolean variable
   * to true to exit the session. Calls the updateSrFile method from
   * the questionBank object to update the original .sr file and the
   * showSummary method from the view object to show the session statistics
   */
  private void exit() {
    //update the .sr file
    questionBank.updateSrFile(srFile);
    view.promptUser(Events.COMPLETE);
    //show the session statistics
    view.showSummary(questionBank.getChangedToHard(),
        questionBank.getChangedToEasy(), questionBank.getNumHardQuestions(),
        questionBank.getNumEasyQuestions());
    finished = true;
  }

  /**
   * Helper method that is used to prompt the user for every question
   * shown. Directs the program based on one of the four options the user
   * chooses, mark easy, mark hard, show answer, or exit. It will
   * keep prompting the user if the user doesn't provide a valid answer
   *
   * @param question the String of the question
   */
  private void takeQuestionInput(String question) {
    //prompt the user
    view.promptUser(Events.SHOW_OPTIONS);
    boolean validAnswer = false;
    // until a valid question response is provide keep prompting the user
    while (!validAnswer) {
      String answer = input.nextLine();
      if (answer.equals("1") || answer.equalsIgnoreCase("mark easy")) {
        questionBank.hardToEasy(question);
        validAnswer = true;
      } else if (answer.equals("2") || answer.equalsIgnoreCase("mark hard")) {
        questionBank.easyToHard(question);
        validAnswer = true;
      } else if (answer.equals("3") || answer.equalsIgnoreCase("show answer")) {
        view.showAnswer(questionBank.getAnswer(question));
        validAnswer = true;
        takeQuestionInput(question);
      } else if (answer.equals("4") || answer.equalsIgnoreCase("exit")) {
        exit();
        validAnswer = true;
      } else {
        view.showError(Events.INVALID_INT_OR_STR);
      }
    }
  }

  /**
   * Helper method for setting the value of the srFile field.
   * it will keep asking the user to provide a valid .sr file
   * until one is provided
   *
   * @return a valid .sr file
   */
  private File setSrFile() {
    File result = null;
    //until there is a valid sr File path keep asking the user for one
    while (result == null) {
      view.showError(Events.INVALID_FILE);
      String path = input.nextLine();
      try {
        if (path.endsWith(".sr")) {
          result = Path.of(path).toFile();
        }
        //catch if the user tries to input something that isn't a valid file
      } catch (RuntimeException e) {
        result = null;
      }
    }
    return result;
  }

  /**
   * Helper method that uses the srFile field to instantiate the
   * question bank.
   */
  private void generateQuestionBank() {
    try {
      questionBank = new QaBank(new FileToText().readThroughFile(srFile.toPath()));
    } catch (RuntimeException e) {
      throw new IllegalArgumentException("The srFile is null or is empty.");
    }
  }

  /**
   * Helper method that is used to retrieve the number of
   * questions the user wants to practice based on their
   * input. It will keep prompting the user until a valid
   * integer above 0 is provided
   *
   * @return an int of the number of questions to show
   */
  private int setNumQuestions() {
    int result = 0;
    //while there isn't a valid number of questions provided
    while (result <= 0) {
      view.promptUser(Events.NUM_QUESTIONS);
      try {
        result = input.nextInt();
      } catch (Exception e) {
        view.showError(Events.INVALID_INT);
      }
      input.nextLine(); // Consume the newline character after the valid input
    }
    //if the valid int is greater than the total number of questions
    //set it to the maximum
    if (result > questionBank.getNumQuestions()) {
      result = questionBank.getNumQuestions();
    }
    return result;
  }
}
