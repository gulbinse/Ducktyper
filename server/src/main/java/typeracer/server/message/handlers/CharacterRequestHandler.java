package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.client.CharacterRequest;
import typeracer.communication.messages.server.CharacterResponse;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.message.MessageHandler;
import typeracer.server.session.Session;
import typeracer.server.session.SessionManager;

/**
 * Handles CharacterRequest messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class CharacterRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public CharacterRequestHandler() {
    this.nextHandler = null;
  }

  private CharacterRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {
    if (message instanceof CharacterRequest characterRequest) {
      Session session = SessionManager.getInstance().getSessionByClientId(clientId);
      if (session != null) {
        if(!session.checkIfPlayerHasFinished(clientId)) {
          boolean correct = session.validateCharacter(clientId, characterRequest.getCharacter());
          ConnectionManager.getInstance().sendMessage(new CharacterResponse(correct), clientId);
        }
      }
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, clientId);
    }
  }

  @Override
  public CharacterRequestHandler setNext(MessageHandler handler) {
    return new CharacterRequestHandler(handler);
  }
}
