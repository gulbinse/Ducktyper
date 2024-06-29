package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Response indicating whether a client is allowed to change its readiness status. This response is
 * sent from the server to a specific client after receiving a {@link
 * typeracer.communication.messages.client.ReadyRequest} from that client. It informs the client
 * whether changing the readiness status is allowed and provides a reason if the request is denied.
 */
public final class ReadyResponse implements Message {

  private final String readyStatus;
  private final String reason;

  /**
   * Constructs a new ReadyResponse with the specified arguments.
   *
   * @param readyStatus the status of the ready request, indicating whether changing is allowed
   * @param reason the reason for a denied request, null otherwise
   */
  public ReadyResponse(String readyStatus, String reason) {
    this.readyStatus = readyStatus;
    this.reason = reason;
  }

  /**
   * Returns the status of the ready request.
   *
   * @return the status of the ready request
   */
  public String getReadyStatus() {
    return readyStatus;
  }

  /**
   * Returns the reason for a denied request or null otherwise.
   *
   * @return the reason for a denied request, null otherwise
   */
  public String getReason() {
    return reason;
  }
}
