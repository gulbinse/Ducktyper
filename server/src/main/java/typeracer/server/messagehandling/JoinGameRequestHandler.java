package typeracer.server.messagehandling;

import typeracer.communication.messages.Message;
import typeracer.server.GameController;

/**
 * Handles JoinGameRequest messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class JoinGameRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /**
   * Constructs a new JoinGameRequestHandler with the specified arguments.
   *
   * @param nextHandler the next handler in the chain
   */
  public JoinGameRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, GameController gameController) {}
}
