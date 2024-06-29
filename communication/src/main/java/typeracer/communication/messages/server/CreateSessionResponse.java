package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Response indicating whether a client successfully created a session. This response is sent to a
 * specific client after receiving a {@link
 * typeracer.communication.messages.client.CreateSessionRequest} from that client. It informs the
 * client whether creating the session was successful by returning the id of the session or provides
 * a reason if the request is denied.
 */
public final class CreateSessionResponse implements Message {

  private final String reason;
  private final int sessionId;

  /**
   * Constructs a new CreateSessionResponse with the specified arguments.
   *
   * @param reason the reason for a denied request, null otherwise
   * @param sessionId the id of the created session or -1 if the request was denied
   */
  public CreateSessionResponse(String reason, int sessionId) {
    this.reason = reason;
    this.sessionId = sessionId;
  }

  /**
   * Returns the reason for a denied request or null otherwise.
   *
   * @return the reason for a denied request, null otherwise
   */
  public String getReason() {
    return reason;
  }

  /**
   * Returns the id of the created session. If session creation was denied, this method returns -1
   * instead.
   *
   * @return the id of the created session or -1 if the request was denied
   */
  public int getSessionId() {
    return sessionId;
  }
}
