package typeracer.game;

import typeracer.server.session.Session;
import typeracer.server.utils.Enums.TypingResult;



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

  public TypeRacerGame(Session session){
    state = new GameState(new TextSource());
  }

  /** Starts a new game with a new text. */
  public void start() {
    if (getPlayerList().isEmpty()) {
      throw new AssertionError("There are currently no players in the game");
    }

    for (Player player : getPlayerList()) {
      if (!player.isReady()) {
        throw new AssertionError(
            "Player " + player.getId() + " not yet ready, but start was attempted");
      }
    }
    state.setGameStatus(GameState.GameStatus.RUNNING);
    gameStartTime = System.nanoTime();
  }

  /**
   * Adds a player to the game.
   *
   * @param id of the player
   */
  public synchronized void addPlayer(int id) {
    if (state.getIds().contains(id)) {
      throw new AssertionError("ID " + id + " already contained in player list.");
    }
    state.addPlayer(id, new Player(id));
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
    }
  }

  /**
   * Makes the given Player type the given character.
   *
   * @param id of the player that types
   * @param character the character that is typed
   * @return The result of the typing attempt
   */
  public TypingResult typeCharacter(
      int id, char character) { // TODO: Does this have to be synchronized?

    if (isGameFinished()) {
      return TypingResult.PLAYER_FINISHED_ALREADY;
    }

    Player player = state.getPlayerById(id);
    if (!player.isFinished()) {
      return player.typeCharacter(character, state.getTextToType(), gameStartTime);
    }
    return TypingResult.PLAYER_FINISHED_ALREADY;
  }

  private boolean isGameFinished() {
    boolean allFinished = true;
    for (Player player : getPlayerList()) {
      if (!player.isFinished()) {
        allFinished = false;
        break;
      }
    }

    if (allFinished) {
      state.setGameStatus(GameState.GameStatus.FINISHED);
      return true;
    }
    return false;
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
  public synchronized boolean setPlayerReady(int id, boolean isReady) {
    GameState.GameStatus statusBefore = getStatus();
    if (getStatus().equals(GameState.GameStatus.WAITING_FOR_READY)) {
      state.getPlayerById(id).setIsReady(isReady);
      return (statusBefore != getStatus());
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
