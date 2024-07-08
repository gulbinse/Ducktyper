package typeracer.server.session;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import typeracer.communication.messages.Message;
import typeracer.communication.statuscodes.GameStatus;
import typeracer.game.TypeRacerGame;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.utils.TypingResult;

/**
 * This class represents a server-managed game session. Each instance of {@link Session} is
 * dedicated to a single typeracer game and manages all clients connected to that session. It
 * functions as a mediator, following the mediator design pattern, facilitating communication
 * between the game logic and the connected clients.
 */
public final class Session {

  /** Defines the maximum number of players allowed in one session. */
  public static final int MAX_SIZE = 5;

  private final TypeRacerGame game = new TypeRacerGame(this);
  private final Set<Integer> playerIds = Collections.synchronizedSet(new HashSet<>());

  /** The default constructor of this class. */
  public Session() {}

  /**
   * Adds a newly connected client to the session as a player.
   *
   * @param playerId the unique id of the player
   */
  public synchronized void handlePlayer(int playerId) {
    playerIds.add(playerId);
    game.addPlayer(playerId);
  }

  /**
   * Removes a recently disconnected client from the session.
   *
   * @param playerId the unique id of the player
   */
  public synchronized void unhandlePlayer(int playerId) {
    playerIds.remove(playerId);
    game.removePlayer(playerId);
  }

  /**
   * Updates the readiness status of a player.
   *
   * @param playerId the unique id of the player
   * @param ready <code>true</code> if the player is ready, <code>false</code> otherwise
   * @return <code>true</code> if the readiness status was updated, <code>false</code> otherwise
   */
  public synchronized boolean updateReadiness(int playerId, boolean ready) {
    return game.setPlayerReady(playerId, ready);
  }

  /** Starts the game. */
  public void startGame() {
    game.start();
  }

  /**
   * Broadcasts a message to all clients connected to this session.
   *
   * @param message the message to be broadcast
   */
  public void broadcastMessage(Message message) {
    playerIds.forEach(id -> sendMessage(message, id));
  }

  /**
   * Sends a message to a specific client connected to this session.
   *
   * @param message the message to be sent
   * @param playerId the unique id of the player
   */
  public void sendMessage(Message message, int playerId) {
    ConnectionManager.getInstance().sendMessage(message, playerId);
  }

  /**
   * Gets the current number of clients connected to this session.
   *
   * @return the number of connected clients
   */
  public int numberOfConnectedClients() {
    return playerIds.size();
  }

  /**
   * Validates whether the typed character is correct for the specified player.
   *
   * @param playerId the unique id of the player
   * @param character the input character
   * @return <code>true</code> if the typed character is correct, <code>false</code> otherwise
   */
  public TypingResult validateCharacter(int playerId, char character) throws IllegalArgumentException {
    return game.typeCharacter(playerId, character);
  }

  /**
   * Returns whether every player is ready.
   *
   * @return <code>true</code> if every player is ready, <code>false</code> otherwise
   */
  public boolean isEveryoneReady() {
    return game.isEveryoneReady();
  }

  /**
   * Checks whether this session's game has already started.
   *
   * @return <code>true</code> if the game has started, <code>false</code> otherwise
   */
  public boolean hasGameStarted() {
    return game.getStatus() == GameStatus.RUNNING;
  }

  /**
   * Checks whether this session is full.
   *
   * @return <code>true</code> if the session is full, <code>false</code> otherwise
   */
  public boolean isFull() {
    return numberOfConnectedClients() >= MAX_SIZE;
  }

  /**
   * Checks whether this session is empty.
   *
   * @return <code>true</code> if the session is empty, <code>false</code> otherwise
   */
  public boolean isEmpty() {
    return numberOfConnectedClients() == 0;
  }

  /**
   * Returns a set of all player ids.
   *
   * @return all player ids
   */
  public Set<Integer> getPlayerIds() {
    return Set.copyOf(playerIds);
  }
}
