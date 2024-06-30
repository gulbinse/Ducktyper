package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;
import typeracer.communication.statuscodes.PermissionStatus;
import typeracer.communication.statuscodes.Reason;

/**
 * Response indicating whether a client is allowed to join the session. This response is sent to a
 * specific client after receiving a {@link
 * typeracer.communication.messages.client.JoinSessionRequest} from that client. It informs the
 * client whether joining the session is allowed and provides a reason if the request is denied.
 */
public final class JoinSessionResponse implements Message {

  private final PermissionStatus joinStatus;
  private final Reason reason;

  /**
   * Constructs a new JoinSessionResponse with the specified arguments.
   *
   * @param joinStatus the status of the join request, indicating whether joining is allowed
   * @param reason the reason for a denied connection, null otherwise
   */
  public JoinSessionResponse(PermissionStatus joinStatus, Reason reason) {
    this.joinStatus = joinStatus;
    this.reason = reason;
  }

  /**
   * Returns the status of the join request.
   *
   * @return the status of the join request
   */
  public PermissionStatus getJoinStatus() {
    return joinStatus;
  }

  /**
   * Returns the reason for a denied connection or null otherwise.
   *
   * @return the reason for a denied connection, null otherwise
   */
  public Reason getReason() {
    return reason;
  }
}
