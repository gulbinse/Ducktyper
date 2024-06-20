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

    public synchronized GameState addPlayer(Player player) {
        String playerName = player.getName();
        Map<String, Player> updatedPlayers = new HashMap<>(players);
        updatedPlayers.put(playerName, player);
        Map<Player, Integer> updatedProgress = new HashMap<>(progress);
        updatedProgress.put(player, player.getProgress());
        return new GameState(textSource, currentText, gameStatus, updatedPlayers, updatedProgress);
    }

    public synchronized GameState removePlayer(Player player) {
        String playerName = player.getName();
        Map<String, Player> updatedPlayers = new HashMap<>(players);
        updatedPlayers.remove(playerName);
        return new GameState(textSource, currentText, gameStatus, updatedPlayers, progress);
    }

    public synchronized GameState nextRound(String newText) {
        return new GameState(textSource, newText, gameStatus, players, progress);
    }

    public List<Player> getPlayers() {
        return List.copyOf(players.values());
    }

    public Map<Player, Integer> getProgress() {
        return Map.copyOf(progress);
    }

    public TextSource getTextSource() {
        return textSource;
    }

    public GameStatus getStatus() {
        return gameStatus;
    }
}
