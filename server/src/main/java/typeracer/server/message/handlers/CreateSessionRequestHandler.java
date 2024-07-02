package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.client.CreateSessionRequest;
import typeracer.communication.messages.server.CreateSessionResponse;
import typeracer.communication.statuscodes.Reason;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.message.MessageHandler;
import typeracer.server.session.SessionManager;

/**
 * Handles CreateSessionRequest messages in a chain of responsibility pattern. If the message is not
 * of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class CreateSessionRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public CreateSessionRequestHandler() {
    this.nextHandler = null;
  }

  private CreateSessionRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {
    if (message instanceof CreateSessionRequest) {
      int id = SessionManager.getInstance().createNewSession();
      Reason reason = id > 0 ? Reason.SUCCESS : Reason.SESSION_CREATE_NO_PERMISSION;
      ConnectionManager.getInstance().sendMessage(new CreateSessionResponse(reason, id), clientId);
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, clientId);
    }
  }

  @Override
  public CreateSessionRequestHandler setNext(MessageHandler handler) {
    return new CreateSessionRequestHandler(handler);
  }
}
