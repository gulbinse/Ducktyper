package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a new player is attempting to join the game. This request is sent from a
 * client to the server to inform it about the player's attempt to join.
 */
public final class JoinGameRequest implements Message {

  private final String playerName;

  /**
   * Constructs a new JoinGameRequest with the specified arguments.
   *
   * @param playerName the name of the player attempting to join the game
   */
  public JoinGameRequest(String playerName) {
    this.playerName = playerName;
  }

  /**
   * Returns the name of the player attempting to join the game.
   *
   * @return the player's name
   */
  public String getPlayerName() {
    return playerName;
  }
}
