package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Response indicating whether a client is allowed to join the lobby. This response is sent to a
 * specific client after receiving a {@link
 * typeracer.communication.messages.client.JoinLobbyRequest} from that client. It informs the client
 * whether joining the lobby is allowed and provides a reason if the request is denied.
 */
public final class JoinLobbyResponse implements Message {

  private final String joinStatus;
  private final String reason;

  /**
   * Constructs a new JoinLobbyResponse with the specified arguments.
   *
   * @param joinStatus the status of the join request, indicating whether joining is allowed
   * @param reason the reason for a denied connection, null otherwise
   */
  public JoinLobbyResponse(String joinStatus, String reason) {
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
