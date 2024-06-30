package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.client.ReadyRequest;
import typeracer.communication.messages.server.ReadyResponse;
import typeracer.communication.statuscodes.PermissionStatus;
import typeracer.communication.statuscodes.Reason;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.message.MessageHandler;
import typeracer.server.session.Session;
import typeracer.server.session.SessionManager;

/**
 * Handles ReadyRequest messages in a chain of responsibility pattern. If the message is not of the
 * specified type, it will be passed to the next handler in the chain, if any.
 */
public class ReadyRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public ReadyRequestHandler() {
    this.nextHandler = null;
  }

  private ReadyRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {
    if (message instanceof ReadyRequest readyRequest) {
      SessionManager sessionManager = new SessionManager();
      Session session = sessionManager.getSessionByClientId(clientId);
      if (session != null) {
        boolean success = session.setReady(clientId, readyRequest.isReady());
        ConnectionManager connectionManager = new ConnectionManager();
        if (success) {
          connectionManager.sendMessage(
              new ReadyResponse(PermissionStatus.ACCEPTED, null), clientId);
        } else {
          connectionManager.sendMessage(
              new ReadyResponse(PermissionStatus.DENIED, Reason.UNKNOWN), clientId);
        }
      }
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, clientId);
    }
  }

  @Override
  public ReadyRequestHandler setNext(MessageHandler handler) {
    return new ReadyRequestHandler(handler);
  }
}
