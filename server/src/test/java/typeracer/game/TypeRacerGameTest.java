package typeracer.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import typeracer.communication.statuscodes.GameStatus;
import typeracer.server.session.Session;
import typeracer.server.utils.IdentifierGenerator;
import typeracer.server.utils.TypingResult;

class TypeRacerGameTest {
  TypeRacerGame game;
  TextSource textSource;
  IdentifierGenerator idGenerator;
  Set<Integer> playerIds;

  @BeforeEach
  void setUpBeforeAll() {
    textSource = new TextSource();
    textSource.setDefaultText();
    idGenerator = new IdentifierGenerator();
    game = new TypeRacerGame(textSource, new Session());
    playerIds = new HashSet<>();
    int id = idGenerator.generateId();
    game.addPlayer(id);
    playerIds.add(id);
  }

  Integer getValidPlayerId() {
    Iterator<Integer> iterator = playerIds.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    } else {
      throw new IndexOutOfBoundsException() {};
    }
  }

  void addValidUser() {
    int userId = idGenerator.generateId();
    playerIds.add(userId);
    game.addPlayer(userId);
  }

  private void assertWrongCharacter(char character) {
    assertEquals(game.typeCharacter(getValidPlayerId(), character), TypingResult.INCORRECT);
  }

  private void assertRightCharacter(char character) {
    assertEquals(game.typeCharacter(getValidPlayerId(), character), TypingResult.CORRECT);
  }

  private void typingWithInvalidId(char character) {
    assertThrows(
        IllegalArgumentException.class,
        () -> game.typeCharacter(Collections.max(game.getIds()) + 1, character));
  }

  void addUserWithInvalidId() {
    int id = idGenerator.generateId();
    game.addPlayer(id);
    assertThrows(AssertionError.class, () -> game.addPlayer(id));
  }

  void removeExistingUserWithCorrectId() {
    final List<Player> playerListBefore = game.getPlayerList();
    final Set<Integer> idSetBefore = game.getIds();
    int id = idGenerator.generateId();
    game.addPlayer(id);
    game.removePlayer(id);
    assertEquals(playerListBefore, game.getPlayerList());
    assertEquals(idSetBefore, game.getIds());
  }

  void removeUserWithWrongId() {
    assertThrows(Error.class, () -> game.removePlayer(Collections.max(game.getIds()) + 1));
  }

  void removeAllPlayers() {
    for (int id : game.getIds()) {
      game.removePlayer(id);
    }
    assertTrue(game.getPlayerList().isEmpty());
  }

  void stopGame() {
    game.stop();
  }

  void finishGame() {
    removeAllPlayers();
    int id = getValidPlayerId();
    game.addPlayer(id);
    for (char character : textSource.getCurrentText().toCharArray()) {
      game.typeCharacter(id, character);
    }
    assertEquals(game.typeCharacter(id, 'a'), TypingResult.PLAYER_FINISHED_ALREADY);
  }

  void setReady() {
    removeAllPlayers();
    int id1 = idGenerator.generateId();
    int id2 = idGenerator.generateId();
    game.addPlayer(id1);
    game.addPlayer(id2);
    game.setPlayerReady(id1, true);
    assertFalse(game.isEveryoneReady());
    game.setPlayerReady(id2, true);
    assertTrue(game.isEveryoneReady());
    game.setPlayerReady(id1, false);
    assertFalse(game.isEveryoneReady());
    game.setPlayerReady(id1, true);
    game.start();
    assertThrows(AssertionError.class, () -> game.setPlayerReady(id1, false));
  }

  void createAnotherGame() {
    TypeRacerGame game2 = new TypeRacerGame(textSource, new Session());
    int id = idGenerator.generateId();
    game.addPlayer(id);
    game2.addPlayer(id);
    game.setPlayerReady(id, true);
    game2.setPlayerReady(id, false);
    assertThrows(AssertionError.class, game2::start);
    game.addPlayer(idGenerator.generateId());
    assertEquals(game2.getStatus(), GameStatus.WAITING_FOR_PLAYERS);
    game2.setPlayerReady(id, true);
    game2.start();
    assertEquals(game2.getStatus(), GameStatus.RUNNING);
    assertEquals(game.getStatus(), GameStatus.WAITING_FOR_PLAYERS);
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
    game.addPlayer(idGenerator.generateId());
    assertThrows(AssertionError.class, game::start);
    for (Player player : game.getPlayerList()) {
      player.setIsReady(true);
    }
    assertEquals(GameStatus.WAITING_FOR_PLAYERS, game.getStatus());
    game.start();
    assertEquals(GameStatus.RUNNING, game.getStatus());
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

  @Test
  void testWritingCharactersAfterFinishing() {
    finishGame();
  }

  @Test
  void testSetPlayerReady() {
    setReady();
  }

  @Test
  void testStartingAnotherGame() {
    createAnotherGame();
  }

  @Test
  void testStopGame() {
    stopGame();
  }
}
