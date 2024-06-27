package typeracer.server.message;

import typeracer.communication.messages.Message;
import typeracer.server.message.handlers.CharacterRequestHandler;
import typeracer.server.message.handlers.JoinGameRequestHandler;
import typeracer.server.message.handlers.ReadyRequestHandler;

/** Creates a chain of responsibility for messages. */
public class MessageHandlerChain {

  private final MessageHandler firstHandler;

  /**
   * Constructs a chain of responsibility for handling messages. The chain consists of the following
   * handlers in order:
   *
   * <ul>
   *   <li>{@link CharacterRequestHandler}
   *   <li>{@link JoinGameRequestHandler}
   *   <li>{@link ReadyRequestHandler}
   * </ul>
   */
  public MessageHandlerChain() {
    this.firstHandler = createChain();
  }

  private MessageHandler createChain() {
    return new CharacterRequestHandler()
        .setNext(new JoinGameRequestHandler().setNext(new ReadyRequestHandler()));
  }

  /**
   * Handles messages in a chain of responsibility.
   *
   * @param message the message to handle
   * @param id the id of the client requesting the message handling
   */
  public void handleMessage(Message message, int id) {
    firstHandler.handleMessage(message, id);
  }
}
