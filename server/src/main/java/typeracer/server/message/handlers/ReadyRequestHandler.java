package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.server.message.MessageHandler;

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
  public void handleMessage(Message message, int id) {}

  @Override
  public ReadyRequestHandler setNext(MessageHandler handler) {
    return new ReadyRequestHandler(handler);
  }
}
