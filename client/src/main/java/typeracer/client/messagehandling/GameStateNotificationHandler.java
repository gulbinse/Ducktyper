package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.GameStateNotification;

/**
 * Handles GameStateNotification messages in a chain of responsibility pattern. If the message is
 * not of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class GameStateNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public GameStateNotificationHandler(MessageHandler nextHandler, ViewController viewController) {
    this.nextHandler = nextHandler;
    this.viewController = viewController;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   */
  @Override
  public void handleMessage(Message message) {
    try {
      if (message instanceof GameStateNotification gameStateNotification) {
        switch (gameStateNotification.getGameStatus()) {
          case RUNNING:
            viewController.startNewGame();
            break;
          case FINISHED:
            // ends the game
            viewController.endGame();
            break;
          case WAITING_FOR_PLAYERS:
            System.out.println("Game is waiting for players.");
          default:
            break;
        }

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
