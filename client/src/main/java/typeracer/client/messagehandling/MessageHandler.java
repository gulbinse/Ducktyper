package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;

/** Interface for handling messages in a chain of responsibility pattern. */
public interface MessageHandler {

  static <T extends MessageHandler> T create(Class<T> handler, MessageHandler nextHandler, ViewController viewController) {
    try {
      return handler.getDeclaredConstructor(MessageHandler.class, ViewController.class).newInstance(nextHandler, viewController);
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
