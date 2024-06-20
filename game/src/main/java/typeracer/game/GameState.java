package typeracer.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class stores the current state of the {@link TypeRacerGame} game, including the game status,
 * the current text, the players, and the progress of each player.
 */
public class GameState {

    /** Represents the Status of the TypeWriter game. */
    public enum GameStatus {
        /** Game is still running. */
        ONGOING,
        /** Game is over because all players have finished their text. */
        FINISHED,
        /** Game hasn't started yet because there are not enough players. */
        WAITING_FOR_READY
    }

    private final GameStatus gameStatus;

    private final TextSource textSource;
    private final String currentText;

    private final Map<String, Player> players;

    /**
     * A constructor which creates a new default gameState.
     *
     * <p>Initially the GameStatus is set to WAITING_FOR_PLAYERS and no text is selected.</p>
     * @param textSource from which the Texts of the Game should come from
     */
    GameState(TextSource textSource) {
        this(textSource, "", GameStatus.WAITING_FOR_READY, Collections.emptyMap());
    }

    /**
     * Private Constructor for GameState class.
     *
     * @param textSource for the Game
     * @param newText which shall be copied
     * @param gameStatus of the Game
     * @param players in the Lobby
     */
    private GameState(
            TextSource textSource,
            String newText,
            GameStatus gameStatus,
            Map<String, Player> players) {
        this.textSource = textSource;
        currentText = newText;
        this.players = Map.copyOf(players);
        this.gameStatus = gameStatus;
    }

    /**
     * Adds a Player to the game.
     *
     * <p>Not implemented yet a check if there are already maximum number of players in the game</p>
     * @param player that is added to the game
     * @return the new GameState
     */
    public synchronized GameState addPlayer(Player player) {
        String playerName = player.getName();
        Map<String, Player> updatedPlayers = new HashMap<>(players);
        updatedPlayers.put(playerName, player);
        return new GameState(textSource, currentText, GameStatus.WAITING_FOR_READY, updatedPlayers);
    }

    /**
     * Removes a Player from the game.
     *
     * @param player that is removed from the game
     * @return the new GameState
     */
    public synchronized GameState removePlayer(Player player) {
        String playerName = player.getName();
        Map<String, Player> updatedPlayers = new HashMap<>(players);
        updatedPlayers.remove(playerName);
        return new GameState(textSource, currentText, gameStatus, updatedPlayers);
    }

    /**
     * Starts a new round.
     *
     * @param newText which shall be copied in the next round
     * @return new GameState with new text
     */
    //Man könnte auch in TextSource eine Methode implementieren, die den nächsten Text zurück gibt.
    //Dann könnte man statt dem Parameter new Text GameState mit newGamestate(..., textSource.getNextText, ...) initialisieren
    public synchronized GameState nextRound(String newText) {
        return new GameState(textSource, newText, gameStatus, players);
    }

    /**
     * Returns List of players in the current game.
     *
     * @return List of players
     */
    public List<Player> getPlayers() {
        return List.copyOf(players.values());
    }

    /**
     * Returns the TextSource of the current GameState.
     *
     * @return TextSource
     */
    public TextSource getTextSource() {
        return textSource;
    }

    /**
     * Returns the Status of the current game.
     *
     * <p>Possible Status is: ONGOING, FINISHED, WAITINGFORPLAYERS </p>
     * @return current Status
     */
    public GameStatus getStatus() {
        return gameStatus;
    }
}
