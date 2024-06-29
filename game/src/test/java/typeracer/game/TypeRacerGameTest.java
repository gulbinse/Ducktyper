package typeracer.game;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TypeRacerGameTest {
  TypeRacerGame game;
  String dummyPlayerName = "Kevin";
  TextSource textSource;

  @BeforeEach
  void setUpBeforeAll() {
    textSource = new TextSource();
    textSource.setDefaultText();
    game = new TypeRacerGame(textSource);
    game.addPlayer(1, dummyPlayerName);
  }

  private void assertWrongLetter(char letter) {
    assertSame(game.typeLetter(1, letter), Player.TypingResult.UNSUCCESSFUL);
  }

  private void assertRightLetter(char letter) {
    assertSame(game.typeLetter(1, letter), Player.TypingResult.SUCCESSFUL);
  }

  private void typingWithInvalidId(char letter) {
    assertThrows(
        NullPointerException.class,
        () -> game.typeLetter(Collections.max(game.getIds()) + 1, letter));
  }

  private void addUserWithInvalidUsername() {
    game.addPlayer(14, "AlphaKevin");
    assertThrows(AssertionError.class, () -> game.addPlayer(15, "AlphaKevin"));
  }

  void addUserWithValidUsername() {
    game.addPlayer(25, "BetaKevin");
  }

  void addUserWithInvalidId() {
    game.addPlayer(35, "GammaKevin");
    assertThrows(AssertionError.class, () -> game.addPlayer(35, "GammaKevin1"));
  }

  void addUserWithNoUsername() {
    assertThrows(AssertionError.class, () -> game.addPlayer(35, null));
    assertThrows(AssertionError.class, () -> game.addPlayer(36, ""));
  }

  void removeExistingUserWithCorrectId() {
    game.addPlayer(45, "OmegaKevin");
    game.removePlayer(45);
  }

  void removeUserWithWrongId() {
    assertThrows(Error.class, () -> game.removePlayer(55));
  }

  void removeAllPlayers() {
    for (int id : game.getIds()) {
      game.removePlayer(id);
    }
  }

  @Tag("Typing")
  @ParameterizedTest
  @MethodSource("charProvider")
  void testWrongLetter_differentLetters(char value) {
    assertWrongLetter(value);
  }

  void testStart() {
    removeAllPlayers();
    assertThrows(AssertionError.class, game::start);
    game.addPlayer(101, "Kevinovic");
    assertThrows(AssertionError.class, game::start);
    for (Player player : game.getPlayerList()) {
      player.setIsReady(true);
    }
    game.start();
  }

  static Stream<Character> charProvider() {
    // The following way to write special characters is necessary in order for Checkstyle to work
    // correctly
    // Otherwise, the file wouldn't compile
    char ss = StandardCharsets.UTF_8.encode("ß").getChar(0);
    char ae = StandardCharsets.UTF_8.encode("ä").getChar(0);
    return Stream.of('a', 'B', 'D', 'G', 'M', 'L', 'k', 'z', ss, '1', ae);
  }

  @Tag("Typing")
  @Test
  void testCorrectLetter_byText() {
    for (char value : textSource.getCurrentText().toCharArray()) {
      assertRightLetter(value);
    }
  }

  @Tag("Typing")
  @Test
  void testTypingWithInvalidPlayerId() {
    for (char value : textSource.getCurrentText().toCharArray()) {
      typingWithInvalidId(value);
    }
  }

  @Tag("CharacterValidation")
  @Test
  void testAddingPlayer() {
    addUserWithInvalidUsername();
    addUserWithValidUsername();
    addUserWithInvalidId();
    addUserWithNoUsername();
  }

  @Test
  void testRemovingPlayer() {
    removeExistingUserWithCorrectId();
    removeUserWithWrongId();
    removeAllPlayers();
  }

  @Test
  void testStartGame() {
    testStart();
  }
}
