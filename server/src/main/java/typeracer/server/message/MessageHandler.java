package typeracer.server.message;

import typeracer.communication.messages.Message;
import typeracer.server.Lobby;
import typeracer.server.message.handlers.CharacterRequestHandler;
import typeracer.server.message.handlers.JoinGameRequestHandler;
import typeracer.server.message.handlers.ReadyRequestHandler;

/** Abstract class for handling messages in a chain of responsibility pattern. */
public abstract class MessageHandler {

  /** The default constructor of this class. */
  public MessageHandler() {}

  /**
   * Handles the incoming message. Implementations should provide specific handling logic.
   *
   * @param message the message to handle
   * @param lobby the lobby this handler belongs to
   * @param id the unique id of the player
   */
  public abstract void handleMessage(Message message, Lobby lobby, int id);

  /**
   * Sets the next handler in the chain.
   *
   * @param handler the next handler in the chain
   * @return a new MessageHandler instance with the set handler
   */
  public abstract MessageHandler setNext(MessageHandler handler);

  /**
   * Creates a chain of responsibility for handling messages. The chain consists of the following
   * handlers in order:
   *
   * <ul>
   *   <li>{@link CharacterRequestHandler}
   *   <li>{@link JoinGameRequestHandler}
   *   <li>{@link ReadyRequestHandler}
   * </ul>
   *
   * @return the first MessageHandler in the created chain
   */
  public static MessageHandler createChain() {
    return new CharacterRequestHandler()
        .setNext(new JoinGameRequestHandler().setNext(new ReadyRequestHandler()));
  }
}
