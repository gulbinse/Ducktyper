package typeracer.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Provides a text to use as prompt for the game. */
public class TextSource {
  private static final String TEXT_SOURCE_FOLDER = "text_sources";
  private static final String DEFAULT_TEXT_FILE = "bee_movie.txt";

  private String currentText;

  /** The default constructor of this class. */
  public TextSource() {}

  /**
   * Returns a default text to test the game.
   *
   * @return the default text, which is the beginning of the bee movie script
   * @throws IOException when exceptions with reading the file occur
   * @deprecated will be replaced by a method returning a randomly selected text
   */
  @Deprecated
  public String setDefaultText() throws IOException {
    currentText = getTextFromFile(new File(TEXT_SOURCE_FOLDER, DEFAULT_TEXT_FILE));
    return currentText;
  }

  /**
   * Returns the text in the given file.
   *
   * @param file the file to read
   * @return the text in the given file
   * @throws IOException when exceptions with reading the file occur
   */
  public String getTextFromFile(File file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
      String line = reader.readLine();

      StringBuilder textBuilder = new StringBuilder();
      while (line != null) {
        textBuilder.append(line).append("\n");
        line = reader.readLine();
      }
      currentText = textBuilder.toString();
      return currentText;
    }
  }

  public String getCurrentText() {
    return currentText;
  }
}
