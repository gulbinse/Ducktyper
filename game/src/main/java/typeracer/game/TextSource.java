package typeracer.game;

import java.io.*;

/** Provides a text to use as prompt for the game. */
public class TextSource {
  private final String TEXT_SOURCE_FOLDER = "text_sources";
  private final String DEFAULT_TEXT_FILE = "bee_movie.txt";

  private String currentText;

  /** The default constructor of this class. */
  public TextSource() {}

  @Deprecated
  public String setDefaultText() throws IOException {
    currentText = getTextFromFile(new File(TEXT_SOURCE_FOLDER, DEFAULT_TEXT_FILE));
    return currentText;
  }

  public String getTextFromFile(File file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
