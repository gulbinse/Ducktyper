package typeracer.server.messagehandling;

import typeracer.communication.messages.Message;
import typeracer.server.GameController;

/**
 * Handles CharacterRequest messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class CharacterRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /**
   * Constructs a new CharacterRequestHandler with the specified arguments.
   *
   * @param nextHandler the next handler in the chain
   */
  public CharacterRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, GameController gameController) {}
}
