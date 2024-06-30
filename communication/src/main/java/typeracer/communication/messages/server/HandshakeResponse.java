package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;
import typeracer.communication.statuscodes.PermissionStatus;
import typeracer.communication.statuscodes.Reason;

/**
 * Response indicating whether a client is allowed to connect. This response is sent to a specific
 * client after receiving a {@link typeracer.communication.messages.client.HandshakeRequest} from
 * that client. It informs the client whether connecting is allowed and provides a reason if the
 * request is denied.
 */
public final class HandshakeResponse implements Message {

  private final PermissionStatus connectionStatus;
  private final Reason reason;

  /**
   * Constructs a new HandshakeResponse with the specified arguments.
   *
   * @param connectionStatus the status of the request, indicating whether connecting is allowed
   * @param reason the reason for a denied connection, null otherwise
   */
  public HandshakeResponse(PermissionStatus connectionStatus, Reason reason) {
    this.connectionStatus = connectionStatus;
    this.reason = reason;
  }

  /**
   * Returns the status of the connection request.
   *
   * @return the status of the connection request
   */
  public PermissionStatus getConnectionStatus() {
    return connectionStatus;
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
