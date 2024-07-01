package typeracer.game;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/** The main class for the game, managing states and providing an interface for the server. */
public class TypeRacerGame {

  private final GameState state;
  private long gameStartTime;

  /**
   * The default constructor of this class.
   *
   * @param textSource the source to receive the text the players have to type from
   */
  public TypeRacerGame(TextSource textSource) {
    state = new GameState(textSource);
  }

  /** Starts a new game with a new text. */
  public void start() {
    if (state.getPlayers().isEmpty()) {
      throw new AssertionError("There are currently no players in the game");
    }

    for (Player player : state.getPlayers()) {
      if (!player.isReady()) {
        throw new AssertionError(
            "Player " + player.getUsername() + " not yet ready, but start was attempted");
      }
    }
    state.setGameStatus(GameState.GameStatus.RUNNING);
    gameStartTime = System.nanoTime();
  }

  /**
   * Adds a player to the game.
   *
   * @param id of the player
   * @param username of the player that will be added to the game
   */
  public void addPlayer(int id, String username) {
    if (!isValidUsername(username)) {
      throw new AssertionError(
          "An invalid player name reached the game logic. This should be handled before. Name: "
              + username);
    }
    synchronized (this) {
      if (state.getIds().contains(id)) {
        throw new AssertionError("ID " + id + " already contained in player list.");
      }
      state.addPlayer(id, new Player(username));
      // TODO: notify Mediator about added Player
    }
  }

  private boolean isValidUsername(String username) {
    if (username == null || username.isBlank()) {
      return false;
    }
    for (Player p : state.getPlayers()) {
      if (p.getUsername().equals(username)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Removes a player from the game.
   *
   * @param id of the player that will be removed
   */
  public void removePlayer(int id) {
    synchronized (this) {
      if (state.getIds().contains(id)) {
        state.removePlayer(id);
      } else {
        throw new AssertionError("ID " + id + " not contained in player list.");
      }
      // TODO: notify Mediator about removed Player
    }
  }

  /**
   * Makes the given Player type the given character.
   *
   * @param id of the player that types
   * @param character the character that is typed
   * @return The result of the typing attempt
   */
  public Player.TypingResult typeCharacter(
      int id, char character) { // TODO: Does this have to be synchronized?

    checkIfGameFinished();

    Player player = state.getPlayerById(id);
    if (!player.isFinished()) {
      return player.typeCharacter(character, state.getTextToType(), gameStartTime);
    }
    return Player.TypingResult.PLAYER_FINISHED_ALREADY;
  }

  private void checkIfGameFinished() {
    boolean allFinished = true;
    for (Player player : getPlayerList()) {
      if (!player.isFinished()) {
        allFinished = false;
        break;
      }
    }

    if (allFinished) {
      state.setGameStatus(GameState.GameStatus.FINISHED);
    }
  }

  /**
   * Returns a list of all players.
   *
   * @return a list of all players in the game
   */
  List<Player> getPlayerList() {
    return state.getPlayers();
  }

  /**
   * Returns the current game status.
   *
   * @return the current game status
   */
  public GameState.GameStatus getStatus() {
    return state.getStatus();
  }

  /**
   * Sets a player ready or not. This works only if the game is not currently waiting for players.
   *
   * @param id of the player to set the status of
   * @param isReady true if the player is ready, false otherwise
   */
  public synchronized void setPlayerReady(int id, boolean isReady) {
    if (getStatus().equals(GameState.GameStatus.WAITING_FOR_READY)) {
      state.getPlayerById(id).setIsReady(isReady);
    } else {
      throw new AssertionError(
          "Attempt to set Player with ID "
              + id
              + " to "
              + (isReady ? "" : "not")
              + " ready, but game status is "
              + getStatus());
    }
  }

  /**
   * Returns the text to type.
   *
   * @return the text to type
   */
  public String getTextToType() {
    return state.getTextToType();
  }

  /**
   * SHOULD ONLY BE USED FOR TESTING PURPOSES! Returns a set of all players' IDs.
   *
   * @return a set of all players' IDs
   */
  Set<Integer> getIds() {
    return state.getIds();
  }
}
