package typeracer.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

/** Provides a text to use as prompt for the game. */
public class TextSource {
  private static final String TEXT_SOURCE_FOLDER = "text_sources";
  private static final String CORPUS_SOURCE_FOLDER = "corpus_sources";
  private static final String DEFAULT_TEXT_FILE = "bee_movie.txt";
  private static final String DEFAULT_CORPUS_FILE = "alice_in_wonderland.txt";
  private static final String DEFAULT_TEXT_FILE_PATH =
      TEXT_SOURCE_FOLDER + File.separator + DEFAULT_TEXT_FILE;
  private static final String DEFAULT_CORPUS_FILE_PATH = CORPUS_SOURCE_FOLDER + File.separator + DEFAULT_CORPUS_FILE;
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

  private final Random random;

  /** The default constructor of this class. */
  public TextSource() {
    random = new Random();
  }

  /** Sets a default text to test the game. */
  public void setDefaultText() {
    currentText = DEFAULT_TEXT;
  }

  /**
   * Sets the current text to the default text read from the file {@value DEFAULT_TEXT_FILE}.
   *
   * @throws IOException when exceptions with reading the file occur
   */
  public void setTextFromDefaultFile() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL textFileUrl = classLoader.getResource(DEFAULT_TEXT_FILE_PATH);
    assert textFileUrl != null;
    try {
      setTextFromFile(new File(textFileUrl.toURI()));
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error with converting default text file URL to URI");
    }
  }

  public void setTextGeneratedFromDefaultCorpus() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL textFileUrl = classLoader.getResource(DEFAULT_CORPUS_FILE_PATH);
    assert textFileUrl != null;
    try {
      setTextGeneratedFromCorpus(new File(textFileUrl.toURI()));
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error with converting default text file URL to URI");
    }
  }

  /**
   * Sets a text randomly from one of the files in the default folder {@value TEXT_SOURCE_FOLDER}.
   *
   * @throws IOException when exceptions with reading the file occur
   */
  public void setRandomTextFromAllCorpora() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL textFolderUrl = classLoader.getResource(CORPUS_SOURCE_FOLDER);
    assert textFolderUrl != null;
    try {
      File[] files = new File(textFolderUrl.toURI()).listFiles();
      assert files != null;
      List<File> fileList = Arrays.stream(files).filter(file -> !file.isDirectory()).toList();
      setTextGeneratedFromCorpora(fileList);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error with converting default text folder URL to URI");
    }
  }

  public void setRandomTextFromDefaultFiles() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL corpusFolderUrl = classLoader.getResource(TEXT_SOURCE_FOLDER);
    assert corpusFolderUrl != null;
    try {
      File[] files = new File(corpusFolderUrl.toURI()).listFiles();
      assert files != null;
      List<File> fileList = Arrays.stream(files).filter(file -> !file.isDirectory()).toList();
      setRandomTextFromFiles(fileList);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error with converting default text folder URL to URI");
    }
  }

  /**
   * Sets the text in the given file as current text.
   *
   * @param file the file to read
   * @throws IOException when exceptions with reading the file occur
   */
  public void setTextFromFile(File file) throws IOException {
    currentText = getTextFromFile(file);
  }

  private String getTextFromFile(File file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
      String line = reader.readLine();

      StringBuilder textBuilder = new StringBuilder();
      while (line != null) {
        textBuilder.append(line).append(" ");

        // skip all newlines
        do {
          line = reader.readLine();
          if (line == null) {
            break;
          }
        } while (line.isEmpty());
      }
      textBuilder.deleteCharAt(textBuilder.length() - 1); // Remove last space character
      return textBuilder.toString();
    }
  }

  /**
   * Sets the current text randomly to the contents of one of the given files.
   *
   * @param files a nonempty list of the files, one of which the text will be taken from
   * @throws IOException when exceptions with reading the file occur
   */
  public void setRandomTextFromFiles(@NotNull List<File> files) throws IOException {
    assert !files.isEmpty();
    int fileNum = random.nextInt(files.size());
    File file = files.get(fileNum);
    setTextFromFile(file);
  }

  public void setTextGeneratedFromCorpora(@NotNull List<File> files) throws IOException {
    assert !files.isEmpty();
    int fileNum = random.nextInt(files.size());
    File file = files.get(fileNum);
    System.out.println("A text, generated by a model trained on " + file.getName());
    setTextGeneratedFromCorpus(file);
  }

  public void setTextGeneratedFromCorpus(File file) throws IOException {
    TextGenerator textGenerator = new TextGenerator(getTextFromFile(file));
    textGenerator.trainModel(file.getName());
    setTextGeneratedFromTextGenerator(textGenerator);
  }

  public void setTextGeneratedFromTextGenerator(TextGenerator textGenerator)  {
    currentText = textGenerator.generateText(100);
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
