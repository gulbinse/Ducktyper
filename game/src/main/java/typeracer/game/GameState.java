package typeracer.game;

import java.util.Collections;
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
        RUNNING,
        /** Game is over because all players have finished their text. */
        FINISHED,
        /** Game hasn't started yet because not every player is ready yet. */
        WAITING_FOR_READY
    }

    private GameStatus gameStatus;

    private final TextSource textSource;

    private Map<String, Player> players;

    /**
     * A constructor which creates a new default gameState.
     *
     * <p>Initially the GameStatus is set to {@link GameStatus#WAITING_FOR_READY} and no text is selected.</p>
     * @param textSource from which the Texts of the Game should come from
     */
    GameState(TextSource textSource) {
        this(textSource, GameStatus.WAITING_FOR_READY, Collections.emptyMap());
    }

    /**
     * Private Constructor for GameState class.
     *
     * @param textSource for the Game
     * @param gameStatus of the Game
     * @param players in the Lobby
     */
    private GameState(
            TextSource textSource,
            GameStatus gameStatus,
            Map<String, Player> players) {
        this.textSource = textSource;
        this.players = Map.copyOf(players);
        this.gameStatus = gameStatus;
    }

    /**
     * Adds a Player to the game.
     *
     * <p>Not implemented yet a check if there are already maximum number of players in the game</p>
     * @param player that is added to the game
     */
    public synchronized void addPlayer(Player player) {
        String playerName = player.getName();
        players.put(playerName, player);
    }

    /**
     * Removes a Player from the game.
     *
     * @param player that is removed from the game
     */
    public synchronized void removePlayer(Player player) {
        String playerName = player.getName();
        players.remove(playerName);
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
     * <p>Possible Status is: {@link GameStatus#RUNNING}, {@link GameStatus#FINISHED}, {@link GameStatus#WAITING_FOR_READY} </p>
     * @return current Status
     */
    public GameStatus getStatus() {
        return gameStatus;
    }

    public void setStatus(GameStatus status) {
        gameStatus = status;
    }
}
