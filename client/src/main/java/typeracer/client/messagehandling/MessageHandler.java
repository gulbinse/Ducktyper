package typeracer.client.messagehandling;

import typeracer.communication.messages.Message;

/** Interface for handling messages in a chain of responsibility pattern. */
public interface MessageHandler {

  /**
   * Handles the incoming message. Implementations should provide specific handling logic.
   *
   * @param message the message to handle
   */
  void handleMessage(Message message);
}