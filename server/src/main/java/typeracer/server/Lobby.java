package typeracer.server;

import java.util.ArrayList;
import java.util.Collection;
import typeracer.communication.messages.Message;

/**
 * This class represents a server-managed game lobby. Each instance of {@link Lobby} is dedicated to
 * a single typeracer game and manages all clients connected to that game. It functions as a
 * mediator, following the mediator design pattern, facilitating communication between the game
 * logic and the connected clients.
 */
public final class Lobby {

  /** Defines the maximum number of players allowed in a lobby. */
  public static final int MAX_LOBBY_SIZE = 5;

  private final Collection<ClientHandler> clientHandlers = new ArrayList<>(MAX_LOBBY_SIZE);

  /** The default constructor of this class. */
  public Lobby() {}

  /**
   * Adds a newly connected client to the game as a player.
   *
   * @param playerId the unique id of the player
   */
  public void addPlayer(int playerId) {}

  /**
   * Removes a recently disconnected client from the game.
   *
   * @param playerId the unique id of the player
   */
  public void removePlayer(int playerId) {}

  /**
   * Updates the readiness status of a player.
   *
   * @param playerId the unique id of the player
   * @param ready <code>true</code> if the player is ready, <code>false</code> otherwise
   */
  public void setReady(int playerId, boolean ready) {}

  /**
   * Broadcasts a message to all clients connected to this lobby.
   *
   * @param message the message to be broadcast
   */
  public void broadcastMessage(Message message) {}

  /**
   * Sends a message to a specific client connected to this lobby.
   *
   * @param playerId the unique id of the player
   * @param message the message to be sent
   */
  public void sendMessage(int playerId, Message message) {}

  /**
   * Gets the current number of clients connected to this lobby.
   *
   * @return the number of connected clients
   */
  public int numberOfConnectedClients() {
    return clientHandlers.size();
  }

  /**
   * Validates whether the typed character is correct for the specified player.
   *
   * @param playerId the unique id of the player
   * @param character the input character
   * @return <code>true</code> if the typed character is correct, <code>false</code> otherwise
   */
  public boolean validateCharacter(int playerId, char character) {
    return false;
  }

  /**
   * Checks whether a client can join this lobby.
   *
   * @return <code>true</code> if the client can join, <code>false</code> otherwise
   */
  public boolean canJoin() {
    return false;
  }

  /**
   * Checks whether this lobby is joinable.
   *
   * @return <code>true</code> if the lobby is joinable, <code>false</code> otherwise
   */
  public boolean isJoinable() {
    return false;
  }

  /**
   * Checks whether this lobby is full.
   *
   * @return <code>true</code> if the lobby is full, <code>false</code> otherwise
   */
  public boolean isFull() {
    return false;
  }
}
