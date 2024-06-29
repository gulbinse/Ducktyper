package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a player is attempting to join a session. This request is sent from a
 * client to the server to inform it about the player's attempt to join.
 */
public final class JoinSessionRequest implements Message {

  private final int sessionId;

  /**
   * Constructs a new JoinSessionRequest with the specified arguments.
   *
   * @param sessionId the id of the session the player is attempting to join
   */
  public JoinSessionRequest(int sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * Returns the id of the session the player is attempting to join.
   *
   * @return the id of the session
   */
  public int getSessionId() {
    return sessionId;
  }
}
