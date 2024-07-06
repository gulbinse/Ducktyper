package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.client.LeaveSessionRequest;
import typeracer.communication.messages.server.LeaveSessionResponse;
import typeracer.communication.messages.server.PlayerLeftNotification;
import typeracer.communication.statuscodes.PermissionStatus;
import typeracer.communication.statuscodes.Reason;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.message.MessageHandler;
import typeracer.server.session.Session;
import typeracer.server.session.SessionManager;

/**
 * Handles LeaveSessionRequest messages in a chain of responsibility pattern. If the message is not
 * of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class LeaveSessionRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public LeaveSessionRequestHandler() {
    this.nextHandler = null;
  }

  private LeaveSessionRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {
    if (message instanceof LeaveSessionRequest) {
      Session session = SessionManager.getInstance().getSessionByClientId(clientId);
      if (session != null) {
        boolean success = SessionManager.getInstance().leaveSession(clientId);
        if (success) {
          ConnectionManager.getInstance().sendMessage(new LeaveSessionResponse(PermissionStatus.ACCEPTED, Reason.SUCCESS), clientId);
          session.broadcastMessage(new PlayerLeftNotification(session.numberOfConnectedClients(), clientId));
        } else {
          ConnectionManager.getInstance().sendMessage(new LeaveSessionResponse(PermissionStatus.DENIED, Reason.UNKNOWN), clientId);
        }
      }
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, clientId);
    }
  }

  @Override
  public LeaveSessionRequestHandler setNext(MessageHandler handler) {
    return new LeaveSessionRequestHandler(handler);
  }
}
