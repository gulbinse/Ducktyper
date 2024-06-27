package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.server.message.MessageHandler;

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
  public void handleMessage(Message message, int id) {}

  @Override
  public CharacterRequestHandler setNext(MessageHandler handler) {
    return new CharacterRequestHandler(handler);
  }
}
