package typeracer.game;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import typeracer.communication.messages.server.GameStateNotification;
import typeracer.communication.messages.server.PlayerStateNotification;
import typeracer.communication.messages.server.TextNotification;
import typeracer.communication.statuscodes.GameStatus;
import typeracer.game.timer.PlayerStateNotifier;
import typeracer.server.session.Session;
import typeracer.server.utils.TypingResult;

/** The main class for the game, managing states and providing an interface for the server. */
public final class TypeRacerGame { // made final to prevent finalizer attacks in constructor

  private final GameState state;
  private long gameStartTime;
  private final Session session;
  private final PlayerStateNotifier notifier;

  /**
   * Allows to create an instance of this class with a custom {@link TextSource}.
   *
   * @param textSource the source to receive the text the players have to type from
   * @param session the session this game is running in. Used to send messages to the server
   */
  public TypeRacerGame(TextSource textSource, Session session) {
    state = new GameState(textSource);
    this.session = session;
    notifier = PlayerStateNotifier.create(this);
  }

  /**
   * Constructs a new Typeracer game and sets the text to type to the default text.
   *
   * @param session the session this game belongs to
   */
  public TypeRacerGame(Session session) {
    TextSource textSource = new TextSource();
    try {
      textSource.setRandomTextFromDefaultFiles();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    state = new GameState(textSource);
    this.session = session;
    notifier = PlayerStateNotifier.create(this);
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
    GameStatus running = GameStatus.RUNNING;
    state.setGameStatus(running);
    session.broadcastMessage(new GameStateNotification(running));
    gameStartTime = System.nanoTime();
    session.broadcastMessage(new TextNotification(getTextToType()));
    notifier.start();
  }

  /** Stops the game. */
  public void stop() {
    GameStatus finished = GameStatus.FINISHED;
    state.setGameStatus(finished);
    session.broadcastMessage(new GameStateNotification(finished));
    notifier.stop();
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
    if (getIds().contains(id)) {
      if (!isGameFinished()) {
        Player player = state.getPlayerById(id);
        if (!player.isFinished()) {
          return player.typeCharacter(character, state.getTextToType(), gameStartTime);
        }
      }
      return TypingResult.PLAYER_FINISHED_ALREADY;
    } else {
      throw new IllegalArgumentException(id + "is an invalid playerID");
    }
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
      stop();
      return true;
    }
    return false;
  }

  /** Broadcasts every player's state to every player. */
  public void broadcastPlayerStates() {
    for (Player player : getPlayerList()) {
      session.broadcastMessage(
          new PlayerStateNotification(
              player.getAccuracy(),
              player.getId(),
              player.getProgress(),
              player.getWordsPerMinute()));
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
  public GameStatus getStatus() {
    return state.getStatus();
  }

  /**
   * Sets a player ready or not. This works only if the game is not currently waiting for players.
   *
   * @param id of the player to set the status of
   * @param isReady true if the player is ready, false otherwise
   * @return true if the ReadyStatus of the player has been changed
   */
  public synchronized boolean setPlayerReady(int id, boolean isReady) {
    if (getStatus().equals(GameStatus.WAITING_FOR_PLAYERS)) {
      state.getPlayerById(id).setIsReady(isReady);
      return true;
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
   * Returns whether the specified player is ready.
   *
   * @param playerId the id of the player
   * @return <code>true</code> if the player is ready, <code>false</code> otherwise
   */
  public boolean isPlayerReady(int playerId) {
    return state.getPlayerById(playerId).isReady();
  }

  /**
   * Returns whether every player is ready.
   *
   * @return <code>true</code> if every player is ready, <code>false</code> otherwise
   */
  public boolean isEveryoneReady() {
    return getPlayerList().stream().allMatch(Player::isReady);
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
