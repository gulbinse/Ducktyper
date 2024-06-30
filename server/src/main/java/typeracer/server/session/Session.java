package typeracer.server.session;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import typeracer.communication.messages.Message;
import typeracer.server.connection.ConnectionManager;

/**
 * This class represents a server-managed game session. Each instance of {@link Session} is
 * dedicated to a single typeracer game and manages all clients connected to that session. It
 * functions as a mediator, following the mediator design pattern, facilitating communication
 * between the game logic and the connected clients.
 */
public final class Session {

  /** Defines the maximum number of players allowed in one session. */
  public static final int MAX_SIZE = 5;

  private final Set<Integer> playerIds = Collections.synchronizedSet(new HashSet<>());

  /** The default constructor of this class. */
  public Session() {}

  /**
   * Adds a newly connected client to the session as a player.
   *
   * @param playerId the unique id of the player
   */
  public synchronized void addPlayer(int playerId) {
    // TODO notify game about connected player
  }

  /**
   * Removes a recently disconnected client from the session.
   *
   * @param playerId the unique id of the player
   */
  public synchronized void removePlayer(int playerId) {
    // TODO notify game about disconnected player
  }

  /**
   * Updates the readiness status of a player.
   *
   * @param playerId the unique id of the player
   * @param ready <code>true</code> if the player is ready, <code>false</code> otherwise
   * @return <code>true</code> if the readiness status was updated, <code>false</code> otherwise
   */
  public synchronized boolean setReady(int playerId, boolean ready) {
    // TODO notify game about readiness update
    return false;
  }

  /**
   * Broadcasts a message to all clients connected to this session.
   *
   * @param message the message to be broadcast
   */
  public void broadcastMessage(Message message) {
    ConnectionManager connectionManager = new ConnectionManager();
    playerIds.forEach(id -> connectionManager.sendMessage(message, id));
  }

  /**
   * Sends a message to a specific client connected to this session.
   *
   * @param message the message to be sent
   * @param playerId the unique id of the player
   */
  public void sendMessage(Message message, int playerId) {
    ConnectionManager connectionManager = new ConnectionManager();
    connectionManager.sendMessage(message, playerId);
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
  public boolean validateCharacter(int playerId, char character) {
    // TODO notify game about character input
    return false;
  }

  /**
   * Checks whether this session is joinable.
   *
   * @return <code>true</code> if the session is joinable, <code>false</code> otherwise
   */
  public boolean isJoinable() {
    return !hasGameStarted() && !isFull();
  }

  /**
   * Checks whether this session's game has already started.
   *
   * @return <code>true</code> if the game has started, <code>false</code> otherwise
   */
  public boolean hasGameStarted() {
    // TODO return game status
    return false;
  }

  /**
   * Checks whether this session is full.
   *
   * @return <code>true</code> if the session is full, <code>false</code> otherwise
   */
  public boolean isFull() {
    return numberOfConnectedClients() == MAX_SIZE;
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
