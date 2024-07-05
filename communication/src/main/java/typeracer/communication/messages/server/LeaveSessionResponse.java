package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;
import typeracer.communication.statuscodes.PermissionStatus;
import typeracer.communication.statuscodes.Reason;

/**
 * Response indicating whether a client is allowed to leave its session. This response is sent to a
 * specific client after receiving a {@link
 * typeracer.communication.messages.client.LeaveSessionRequest} from that client. It informs the
 * client whether leaving the session is allowed and provides a reason if the request is denied.
 */
public final class LeaveSessionResponse implements Message {

  private final PermissionStatus leaveStatus;
  private final Reason reason;

  /**
   * Constructs a new LeaveSessionResponse with the specified arguments.
   *
   * @param leaveStatus the status of the leave request, indiciating whether leaving is allowed
   * @param reason the reason for a denied disconnection, null otherwise
   */
  public LeaveSessionResponse(PermissionStatus leaveStatus, Reason reason) {
    this.leaveStatus = leaveStatus;
    this.reason = reason;
  }

  /**
   * Returns the status of the leave request.
   *
   * @return the status of the leave request.
   */
  public PermissionStatus getLeaveStatus() {
    return leaveStatus;
  }

  /**
   * Returns the reason for a denied disconnection or null otherwise.
   *
   * @return the reason for a denied disconnection, null otherwise
   */
  public Reason getReason() {
    return reason;
  }
}
