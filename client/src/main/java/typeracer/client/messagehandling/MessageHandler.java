package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;

/** Interface for handling messages in a chain of responsibility pattern. */
public interface MessageHandler {

  /**
   * Creates and initializes a new instance of the specified MessageHandler class.
   *
   * @param <T> the type of MessageHandler to be created.
   * @param handler the class of the MessageHandler to be created.
   * @param nextHandler the next handler in the chain of responsibility.
   * @param viewController the ViewController to manage views and handle interactions.
   * @return a new instance of the specified MessageHandler class.
   * @throws RuntimeException if the handler instance cannot be created.
   */
  static <T extends MessageHandler> T create(Class<T> handler, MessageHandler nextHandler,
                                             ViewController viewController) {
    try {
      return handler.getDeclaredConstructor(MessageHandler.class, ViewController.class)
              .newInstance(nextHandler, viewController);
    } catch (Exception e) {
      throw new RuntimeException("Cannot create instance of " + handler.getName(), e);
    }
  }

  /**
   * Handles the incoming message. Implementations should provide specific handling logic.
   *
   * @param message the message to handle
   */
  void handleMessage(Message message);
}
