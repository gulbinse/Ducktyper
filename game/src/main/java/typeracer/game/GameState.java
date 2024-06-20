package typeracer.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the state of the game.
 */
public class GameState {

    public enum GameStatus {
        ONGOING,
        FINISHED,
        WAITINGFORPLAYERS
    }

    private final GameStatus gameStatus;

    private final TextSource textSource;
    private final String currentText;

    private final Map<String, Player> players;
    private final Map<Player, Integer> progress;

    /**
     * A constructor which creates a new default gameState.
     *
     * @param textSource from which the Texts of the Game should come from
     */
    GameState(TextSource textSource) {
        this(textSource, "", GameStatus.WAITINGFORPLAYERS, Collections.emptyMap(), Collections.emptyMap());
    }

    /**
     * The default constructor of this class.
     */
    private GameState(
            TextSource textSource,
            String newText,
            GameStatus gameStatus,
            Map<String, Player> players,
            Map<Player, Integer> progress) {
        this.textSource = textSource;
        currentText = newText;
        this.players = Map.copyOf(players);
        this.progress = Map.copyOf(progress);
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
        Map<Player, Integer> updatedProgress = new HashMap<>(progress);
        updatedProgress.put(player, player.getProgress());
        return new GameState(textSource, currentText, gameStatus, updatedPlayers, updatedProgress);
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
        return new GameState(textSource, currentText, gameStatus, updatedPlayers, progress);
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
        return new GameState(textSource, newText, gameStatus, players, progress);
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
     * Returns Map with Player and their corresponding progress.
     *
     * @return Map with progress.
     */
    public Map<Player, Integer> getProgress() {
        return Map.copyOf(progress);
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
     * @return currrent Status
     */
    public GameStatus getStatus() {
        return gameStatus;
    }
}
