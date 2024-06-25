package typeracer.game;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class typeRacerGame_Test {
    static TypeRacerGame game;
    static Player dummyPlayer;
    static TextSource textSource;

    @BeforeAll
    static void setUpBeforeAll() throws Exception {
        textSource = new TextSource();
        textSource.setDefaultText();
        game = new TypeRacerGame(textSource);
        dummyPlayer = new Player("Kevin");
        game.addPlayer(1, dummyPlayer);
    }

    private void assertWrongLetter(char letter) {
        assertSame(game.typeLetter(1, letter), Player.TypingResult.UNSUCCESSFUL);
    }

    private void assertRightLetter(char letter) {
        assertSame(game.typeLetter(1, letter), Player.TypingResult.SUCCESSFUL);
    }

    private void addUserWithInvalidUsername() {
        Player testPlayer = new Player("Alphakevin");
        game.addPlayer(14, testPlayer);
        assertThrows(AssertionError.class, () -> {
            game.addPlayer(15, new Player("Alphakevin"));
        });
    }

    void addUserWithValidUsername() {
        Player testPlayer = new Player("Betakevin");
        game.addPlayer(25, testPlayer);
    }

    void addUserWithInvalidID() {
        Player testPlayer = new Player("Gammakevin");
        game.addPlayer(35, testPlayer);
        assertThrows(AssertionError.class, () -> {
            game.addPlayer(35, testPlayer);
        });
    }

    void addUserWithNoUsername() {
        Player testPlayer = new Player(null);
        Player testPlayer2 = new Player("");
        assertThrows(AssertionError.class, () -> {
            game.addPlayer(35, testPlayer);
        });
        assertThrows(AssertionError.class, () -> {
            game.addPlayer(36, testPlayer2);
        });
    }

    void removeExistingUserWithCorrectID() {
        Player testPlayer = new Player("OmegaKevin");
        game.addPlayer(45, testPlayer);
        game.removePlayer(45);
    }

    void removeUserWithWrongID() {
        assertThrows(Error.class, () -> {
            game.removePlayer(55);
        });
    }

    void removeAllPlayers() {
        for (int id : game.getState().getIds()) {
            game.removePlayer(id);
        }
    }

    void testStart(){
        removeAllPlayers();
        assertThrows(AssertionError.class, () -> {
            game.start();
        });
        Player notReadyPlayer1 = new Player("Kevinovic");
        game.addPlayer(101, notReadyPlayer1);
        assertThrows(AssertionError.class, () -> {
            game.start();
        });
        for (Player player : game.getState().getPlayers()){
            player.getState().setIsReady(true);
        }
        game.start();
    }

    @Tag("Typing")
    @ParameterizedTest
    @ValueSource(chars = {'a', 'B', 'D', 'G', 'M', 'L', 'k', 'z'})
    void testWrongLetter_differentLetters(char value) {
        assertWrongLetter(value);
    }

    @Tag("Typing")
    @Test
    void testCorrectLetter_byText() {
        for (char value : textSource.getCurrentText().toCharArray()) {
            assertRightLetter(value);
        }
    }

    @Tag("Character Validation")
    @Test
    void testAddingPlayer() {
        addUserWithInvalidUsername();
        addUserWithValidUsername();
        addUserWithInvalidID();
        addUserWithNoUsername();
    }

    @Test
    void testRemovingPlayer() {
        removeExistingUserWithCorrectID();
        removeUserWithWrongID();
        removeAllPlayers();
    }

    @Test
    void testStartGame(){
        testStart();
    }
}
