package typeracer.server.message;

import typeracer.communication.messages.Message;

/** Interface for handling messages in a chain of responsibility pattern. */
public interface MessageHandler {

  /**
   * Handles the incoming message. Implementations should provide specific handling logic.
   *
   * @param message the message to handle
   * @param clientId the unique id of the client
   */
  void handleMessage(Message message, int clientId);

  /**
   * Sets the next handler in the chain.
   *
   * @param handler the next handler in the chain
   * @return a new MessageHandler instance with the set handler
   */
  MessageHandler setNext(MessageHandler handler);
}
