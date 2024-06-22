package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.server.Lobby;
import typeracer.server.message.MessageHandler;

/**
 * Handles JoinGameRequest messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class JoinGameRequestHandler extends MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public JoinGameRequestHandler() {
    this.nextHandler = null;
  }

  private JoinGameRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, Lobby lobby, int id) {}

  @Override
  public JoinGameRequestHandler setNext(MessageHandler handler) {
    return new JoinGameRequestHandler(handler);
  }
}
