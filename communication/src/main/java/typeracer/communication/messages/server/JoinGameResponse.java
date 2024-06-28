package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Response indicating whether a client is allowed to join the game. This response is sent to a
 * specific client after receiving a {@link typeracer.communication.messages.client.JoinGameRequest}
 * from that client. It informs the client whether joining the game is allowed and provides a reason
 * if the request is denied.
 */
public final class JoinGameResponse implements Message {

  private final String joinStatus;
  private final String reason;

  /**
   * Constructs a new JoinGameResponse with the specified arguments.
   *
   * @param joinStatus the status of the join request, indicating whether joining is allowed
   * @param reason the reason for a denied connection, null otherwise
   */
  public JoinGameResponse(String joinStatus, String reason) {
    this.joinStatus = joinStatus;
    this.reason = reason;
  }

  /**
   * Returns the status of the join request.
   *
   * @return the status of the join request
   */
  public String getJoinStatus() {
    return joinStatus;
  }

  /**
   * Returns the reason for a denied connection or null otherwise.
   *
   * @return the reason for a denied connection, null otherwise
   */
  public String getReason() {
    return reason;
  }
}
