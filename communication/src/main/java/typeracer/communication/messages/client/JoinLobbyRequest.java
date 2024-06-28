package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a player is attempting to join a lobby. This request is sent from a
 * client to the server to inform it about the player's attempt to join.
 */
public final class JoinLobbyRequest implements Message {

  private final int lobbyId;

  /**
   * Constructs a new JoinLobbyRequest with the specified arguments.
   *
   * @param lobbyId the id of the lobby the player is attempting to join
   */
  public JoinLobbyRequest(int lobbyId) {
    this.lobbyId = lobbyId;
  }

  /**
   * Returns the id of the lobby the player is attempting to join.
   *
   * @return the id of the lobby
   */
  public int getLobbyId() {
    return lobbyId;
  }
}
