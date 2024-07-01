package typeracer.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

  private Map<Integer, Player> players = new HashMap<>(); // Map of IDs to Players

  /**
   * A constructor which creates a new default GameState.
   *
   * <p>Initially the GameStatus is set to {@link GameStatus#WAITING_FOR_READY}.
   *
   * @param textSource from which the text of the game should come from
   */
  GameState(TextSource textSource) {
    this.textToType = textSource.getCurrentText();
  }

  /**
   * Adds a Player to the game.
   *
   * @param id of the player
   * @param player that is added to the game
   */
  synchronized void addPlayer(int id, Player player) {
    players.put(id, player);
  }

  /**
   * Removes a Player from the game.
   *
   * @param id of the Player that is removed from the game
   */
  synchronized void removePlayer(int id) {
    players.remove(id);
  }

  /**
   * Returns list of players in the current game.
   *
   * @return List of players
   */
  List<Player> getPlayers() {
    return List.copyOf(players.values());
  }

  /**
   * Returns the text which is to be typed.
   *
   * @return the text to type
   */
  String getTextToType() {
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
  GameStatus getStatus() {
    return gameStatus;
  }

  /**
   * Sets the game status to the specified value.
   *
   * @param gameStatus the value to set the game status to
   */
  void setGameStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  /**
   * Returns a Set of all Player's IDs.
   *
   * @return a Set of all Player's IDs
   */
  Set<Integer> getIds() {
    return Set.copyOf(players.keySet());
  }

  /**
   * Returns the player identified by the given ID.
   *
   * @param id of the player
   * @return the player belonging to the given ID
   */
  synchronized Player getPlayerById(int id) {
    if (!players.containsKey(id)) {
      throw new NullPointerException("Player with ID " + id + " not contained in list of players.");
    }
    return players.get(id);
  }
}
