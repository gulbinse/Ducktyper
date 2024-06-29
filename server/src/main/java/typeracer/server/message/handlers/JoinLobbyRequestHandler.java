package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.server.message.MessageHandler;

/**
 * Handles JoinLobbyRequest messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class JoinLobbyRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public JoinLobbyRequestHandler() {
    this.nextHandler = null;
  }

  private JoinLobbyRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {}

  @Override
  public JoinLobbyRequestHandler setNext(MessageHandler handler) {
    return new JoinLobbyRequestHandler(handler);
  }
}
