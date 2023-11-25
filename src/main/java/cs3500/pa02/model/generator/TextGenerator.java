package cs3500.pa02.model.generator;

import java.util.List;

/**
 * Interface for classes that generate text in a certain format
 */
public interface TextGenerator {
  String generateText(List<String> lines);
}
