package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a new player is attempting to connect. This request is sent from a client
 * to the server to inform it about the player's attempt to connect.
 */
public final class HandshakeRequest implements Message {

  private final String playerName;

  /**
   * Constructs a new HandshakeRequest with the specified arguments.
   *
   * @param playerName the name of the player attempting to connect
   */
  public HandshakeRequest(String playerName) {
    this.playerName = playerName;
  }

  /**
   * Returns the name of the player attempting to connect.
   *
   * @return the player's name
   */
  public String getPlayerName() {
    return playerName;
  }
}
