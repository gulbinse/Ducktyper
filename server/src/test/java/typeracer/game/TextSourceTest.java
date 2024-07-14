package typeracer.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.jupiter.api.Test;

class TextSourceTest {
  private final TextSource textSource = new TextSource();
  private static final String expectedDefaultText =
      "According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway because bees don't care what humans think is impossible. Yellow, black. Yellow, black. Yellow, black. Yellow, black. Ooh, black and yellow! Let's shake it up a little.";

  @Deprecated
  @Test
  void testDefaultTextFromFile() throws IOException {
    textSource.setTextFromDefaultFile();

    assertEquals(expectedDefaultText, textSource.getCurrentText());
  }

  @Deprecated
  @Test
  void testTextFromFile() throws IOException, URISyntaxException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL textFileUrl = classLoader.getResource("text_sources" + File.separator + "bee_movie.txt");
    assert textFileUrl != null;
    textSource.setTextFromFile(new File(textFileUrl.toURI()));

    assertEquals(expectedDefaultText, textSource.getCurrentText());
  }

  @Test
  void testTextFromWrongPath() {
    assertThrows(
        FileNotFoundException.class,
        () ->
            textSource.setTextFromFile(
                new File("server/src/main/resources/text_sources/dasIstKeineValideURL.txt")));
  }
}
