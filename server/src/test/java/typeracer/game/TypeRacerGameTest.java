package typeracer.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import typeracer.server.utils.Enums.TypingResult;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TypeRacerGameTest {
  TypeRacerGame game;
  TextSource textSource;

  @BeforeEach
  void setUpBeforeAll() {
    textSource = new TextSource();
    textSource.setDefaultText();
    game = new TypeRacerGame(textSource);
    game.addPlayer(1);
  }

  private void assertWrongCharacter(char character) {
    assertEquals(game.typeCharacter(1, character), TypingResult.INCORRECT);
  }

  private void assertRightCharacter(char character) {
    assertEquals(game.typeCharacter(1, character), TypingResult.CORRECT);
  }

  private void typingWithInvalidId(char character) {
    assertThrows(
        NullPointerException.class,
        () -> game.typeCharacter(Collections.max(game.getIds()) + 1, character));
  }

  void addValidUser() {
    game.addPlayer(25);
  }

  void addUserWithInvalidId() {
    game.addPlayer(35);
    assertThrows(AssertionError.class, () -> game.addPlayer(35));
  }

  void removeExistingUserWithCorrectId() {
    final List<Player> playerListBefore = game.getPlayerList();
    final Set<Integer> idSetBefore = game.getIds();
    game.addPlayer(45);
    game.removePlayer(45);
    assertEquals(playerListBefore, game.getPlayerList());
    assertEquals(idSetBefore, game.getIds());
  }

  void removeUserWithWrongId() {
    assertThrows(Error.class, () -> game.removePlayer(55));
  }

  void removeAllPlayers() {
    for (int id : game.getIds()) {
      game.removePlayer(id);
    }
    assertTrue(game.getPlayerList().isEmpty());
  }

  @Tag("Typing")
  @ParameterizedTest
  @MethodSource("charProvider")
  void testWrongCharacter_differentCharacters(char character) {
    assertWrongCharacter(character);
  }

  void testStart() {
    removeAllPlayers();
    assertThrows(AssertionError.class, game::start);
    game.addPlayer(101);
    assertThrows(AssertionError.class, game::start);
    for (Player player : game.getPlayerList()) {
      player.setIsReady(true);
    }
    assertEquals(GameState.GameStatus.WAITING_FOR_READY, game.getStatus());
    game.start();
    assertEquals(GameState.GameStatus.RUNNING, game.getStatus());
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
  void testCorrectCharacter_byText() {
    for (char character : textSource.getCurrentText().toCharArray()) {
      assertRightCharacter(character);
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
    addValidUser();
    addUserWithInvalidId();
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
