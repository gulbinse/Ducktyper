package typeracer.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/** Provides a text to use as prompt for the game. */
public class TextSource {
  private static final String TEXT_SOURCE_FOLDER = "text_sources";
  private static final String DEFAULT_TEXT_FILE = "bee_movie.txt";
  private static final String DEFAULT_TEXT_FILE_PATH =
      TEXT_SOURCE_FOLDER + File.separator + DEFAULT_TEXT_FILE;
  private static final String DEFAULT_TEXT =
      """
          According to all known laws of aviation, there is no way a bee should be able to fly.
          Its wings are too small to get its fat little body off the ground.
          The bee, of course, flies anyway because bees don't care what humans think is impossible.
          Yellow, black. Yellow, black. Yellow, black. Yellow, black.
          Ooh, black and yellow!
          Let's shake it up a little.
          """;

  private String currentText;

  /** The default constructor of this class. */
  public TextSource() {}

  /** Sets a default text to test the game. */
  public void setDefaultText() {
    currentText = DEFAULT_TEXT;
  }

  /**
   * Sets the current text to a default text read from a file.
   *
   * @throws IOException when exceptions with reading the file occur
   */
  public void setDefaultTextFromFile() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL textFileUrl = classLoader.getResource(DEFAULT_TEXT_FILE_PATH);
    assert textFileUrl != null;
    try {
      setTextFromFile(new File(textFileUrl.toURI()));
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error with converting default text file URL to URI");
    }
  }

  /**
   * Sets the text in the given file as current text.
   *
   * @param file the file to read
   * @throws IOException when exceptions with reading the file occur
   */
  public void setTextFromFile(File file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
      String line = reader.readLine();

      StringBuilder textBuilder = new StringBuilder();
      while (line != null) {
        textBuilder.append(line).append(System.lineSeparator());
        line = reader.readLine();
      }
      currentText = textBuilder.toString();
    }
  }

  /**
   * Returns the current text to use in the game.
   *
   * @return the current text
   */
  public String getCurrentText() {
    return currentText;
  }
}
