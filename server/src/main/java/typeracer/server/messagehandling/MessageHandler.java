package typeracer.server.messagehandling;

import typeracer.communication.messages.Message;
import typeracer.server.GameController;

/** Interface for handling messages in a chain of responsibility pattern. */
public interface MessageHandler {

  /**
   * Handles the incoming message. Implementations should provide specific handling logic.
   *
   * @param message the message to handle
   * @param gameController the controller responsible for managing the game
   */
  void handleMessage(Message message, GameController gameController);
}
