package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.client.HandshakeRequest;
import typeracer.communication.messages.server.HandshakeResponse;
import typeracer.communication.statuscodes.PermissionStatus;
import typeracer.communication.statuscodes.Reason;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.message.MessageHandler;

/**
 * Handles HandshakeRequest messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class HandshakeRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public HandshakeRequestHandler() {
    this.nextHandler = null;
  }

  private HandshakeRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {
    if (message instanceof HandshakeRequest handshakeRequest) {
      ConnectionManager.OperationStatus status =
          ConnectionManager.getInstance()
              .handlePlayerName(clientId, handshakeRequest.getPlayerName());

      HandshakeResponse response;
      switch (status) {
        case SUCCESS -> response = new HandshakeResponse(PermissionStatus.ACCEPTED, Reason.SUCCESS);
        case INVALID_USERNAME ->
            response = new HandshakeResponse(PermissionStatus.DENIED, Reason.INVALID_USERNAME);
        default -> response = new HandshakeResponse(PermissionStatus.DENIED, Reason.UNKNOWN);
      }
      ConnectionManager.getInstance().sendMessage(response, clientId);
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, clientId);
    }
  }

  @Override
  public HandshakeRequestHandler setNext(MessageHandler handler) {
    return new HandshakeRequestHandler(handler);
  }
}
