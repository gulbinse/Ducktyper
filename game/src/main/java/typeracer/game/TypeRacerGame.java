package typeracer.game;

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
    // check if all players ready
    for (Player player : state.getPlayers()) {
      if (!player.isReady()) {
        throw new AssertionError(
            "Player " + player.getUsername() + " not yet ready, but start was attempted");
      }
    }
    state.setStatus(GameState.GameStatus.RUNNING);
    gameStartTime = System.nanoTime();
    // TODO: notify Mediator about game start
  }

  /**
   * Adds a player to the game.
   *
   * @param id of the player
   * @param player that will be added to the game
   */
  public void addPlayer(int id, Player player) {
    if (!isValidUsername(player.getUsername())) {
      throw new AssertionError(
          "An invalid player name reached the game logic. This should be handled before. Name: "
              + player.getUsername());
    }
    synchronized (this) {
      if (state.getIds().contains(id)) {
        throw new AssertionError("ID " + id + " already contained in player list.");
      }
      state.addPlayer(id, player);
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
      state.removePlayer(id);
      // TODO: notify Mediator about removed Player
    }
  }

  /**
   * Makes the given Player type the given letter.
   *
   * @param id of the player that types
   * @param letter the letter that is typed
   * @return The result of the typing attempt
   */
  public Player.TypingResult typeLetter(
      int id, char letter) { // TODO: Does this have to be synchronized?
    // TODO: Check if all players have progress of 1 (i.e. finished the game -> set status to
    // FINISHED)
    return state.getPlayerById(id).typeLetter(letter, state.getTextToType(), gameStartTime);
  }
}
