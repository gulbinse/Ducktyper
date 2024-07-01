package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.client.JoinSessionRequest;
import typeracer.communication.messages.server.JoinSessionResponse;
import typeracer.communication.statuscodes.PermissionStatus;
import typeracer.communication.statuscodes.Reason;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.message.MessageHandler;
import typeracer.server.session.SessionManager;

/**
 * Handles JoinSessionRequest messages in a chain of responsibility pattern. If the message is not
 * of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class JoinSessionRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public JoinSessionRequestHandler() {
    this.nextHandler = null;
  }

  private JoinSessionRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {
    if (message instanceof JoinSessionRequest joinSessionRequest) {
      int sessionId = joinSessionRequest.getSessionId();
      SessionManager.OperationStatus status =
          SessionManager.getInstance().joinSessionById(clientId, sessionId);

      JoinSessionResponse response;
      switch (status) {
        case SUCCESS -> response = new JoinSessionResponse(PermissionStatus.ACCEPTED, null);
        case SESSION_NOT_FOUND ->
            response = new JoinSessionResponse(PermissionStatus.DENIED, Reason.SESSION_NOT_FOUND);
        case SESSION_GAME_ALREADY_STARTED ->
            response =
                new JoinSessionResponse(
                    PermissionStatus.DENIED, Reason.SESSION_GAME_ALREADY_STARTED);
        case SESSION_FULL ->
            response = new JoinSessionResponse(PermissionStatus.DENIED, Reason.SESSION_FULL);
        default -> response = new JoinSessionResponse(PermissionStatus.DENIED, Reason.UNKNOWN);
      }
      ConnectionManager.getInstance().sendMessage(response, clientId);
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, clientId);
    }
  }

  @Override
  public JoinSessionRequestHandler setNext(MessageHandler handler) {
    return new JoinSessionRequestHandler(handler);
  }
}
