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

  private GameStatus gameStatus = GameStatus.WAITING_FOR_READY;

  private final String textToType;

  private Map<String, Player> players = Collections.emptyMap();

  /**
   * A constructor which creates a new default GameState.
   *
   * <p>Initially the GameStatus is set to {@link GameStatus#WAITING_FOR_READY} and no text is
   * selected.
   *
   * @param textSource from which the text of the game should come from
   */
  GameState(TextSource textSource) {
    this.textToType = textSource.getCurrentText();
  }

  /**
   * Adds a Player to the game.
   *
   * <p>Not implemented yet a check if there are already maximum number of players in the game
   *
   * @param player that is added to the game
   */
  public synchronized void addPlayer(Player player) {
    String playerName = player.getUsername();
    players.put(playerName, player);
  }

  /**
   * Removes a Player from the game.
   *
   * @param player that is removed from the game
   */
  public synchronized void removePlayer(Player player) {
    String playerName = player.getUsername();
    players.remove(playerName);
  }

  /**
   * Returns list of players in the current game.
   *
   * @return List of players
   */
  public List<Player> getPlayers() {
    return List.copyOf(players.values());
  }

  /**
   * Returns the text which is to be typed.
   *
   * @return the text to type
   */
  public String getTextToType() {
    return textToType;
  }

  /**
   * Returns the status of the current game.
   *
   * <p>Possible status are: {@link GameStatus#RUNNING}, {@link GameStatus#FINISHED}, {@link
   * GameStatus#WAITING_FOR_READY}
   *
   * @return current status
   */
  public GameStatus getStatus() {
    return gameStatus;
  }

  public void setStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }
}
