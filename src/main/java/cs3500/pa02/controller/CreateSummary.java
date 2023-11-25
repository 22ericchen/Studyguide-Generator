package cs3500.pa02.controller;

import cs3500.pa02.model.Events;
import cs3500.pa02.model.filetools.FileParser;
import cs3500.pa02.model.filetools.FileToText;
import cs3500.pa02.model.filetools.FolderWalker;
import cs3500.pa02.model.generator.QaGenerator;
import cs3500.pa02.model.generator.StudyGuideGenerator;
import cs3500.pa02.model.sorter.AbstractSorter;
import cs3500.pa02.model.sorter.CreatedSorter;
import cs3500.pa02.model.sorter.FilenameSorter;
import cs3500.pa02.model.sorter.ModifiedSorter;
import cs3500.pa02.view.Viewer;
import java.nio.file.Path;
import java.util.List;

/**
 * Controller class for creating a study guide.
 */
public class CreateSummary implements Controller {
  private final Viewer view = new Viewer();
  private final FileToText reader = new FileToText();
  private final Path directory;
  private final String orderingFlag;
  private final String studyGuideName;

  /**
   * CreateSummary constructor which sets the requested
   * file's directory, the requested ordering flag, and
   * the path and name of the new file
   *
   * @param directory An absolute or relative path to a
   *                  file directory
   * @param orderingFlag A String that should be some form of "created", "filename", or "modified
   * @param studyGuideName String form of the path or name of the new study guide file
   */
  public CreateSummary(Path directory, String orderingFlag, String studyGuideName) {
    this.directory = directory;
    this.orderingFlag = orderingFlag.toLowerCase();
    this.studyGuideName = studyGuideName;
  }

  /**
   * using the 3 fields, this method calls a FolderWalker to go through
   * all the files from the first command line argument which all then
   * get passed to an AbstractSorter which sorts them based on the second
   * command line argument. Using a StudyGuideGenerator object, a study guide
   * is built as a string and given to a FileParser object to write into a new
   * file along with generating a .sr file
   */
  @Override
  public void run() {
    FolderWalker walker = new FolderWalker(directory);
    //get the sorter based on the ordering flag argument
    AbstractSorter sorter = chooseSorter(orderingFlag, walker.getFiles());
    //empty strings to temporarily contain the generated study guide and QAs
    String studyGuideText = "";
    String srFileText = "";
    //generate the text
    for (Path file : sorter.sort()) {
      List<String> fileText = reader.readThroughFile(file);
      studyGuideText += new StudyGuideGenerator().generateText(fileText);
      srFileText += new QaGenerator().generateText(fileText);
    }
    //call helper method
    createFiles(studyGuideText, srFileText);
    //indicate study guides are generated
    view.promptUser(Events.STUDYGUIDE_COMPLETE);
  }

  /**
   * Helper method for choosing which sorter to use based on
   * the ordering flag
   *
   * @param ordering The String of the ordering flag requested
   * @param files A list of Paths that will be passed to the AbstractSorter object
   * @return an AbstractSorter object that aligns with the proper ordering flag
   */
  private AbstractSorter chooseSorter(String ordering, List<Path> files) {
    //maybe switch to switch statement?
    AbstractSorter result;
    if (ordering.equals("modified")) {
      result = new ModifiedSorter(files);
    } else if (ordering.equals("filename")) {
      result = new FilenameSorter(files);
    } else if (ordering.equals("created")) {
      result = new CreatedSorter(files);
    } else {
      throw new IllegalArgumentException("Invalid ordering flag argument");
    }
    return result;
  }

  /**
   * private helper method to instantiate two FileParsers for the study guide
   * and the other for a .sr file
   *
   * @param mdText the String text for the study guide
   * @param srText the String text for the question answer list
   */
  private void createFiles(String mdText, String srText) {
    //create new .md file and .sr file
    FileParser mdFile = new FileParser(studyGuideName);
    FileParser srFile = new FileParser(studyGuideName.replace(".md", ".sr"));
    //add the text to the files
    mdFile.parseFile(mdText);
    srFile.parseFile(srText);
  }

}