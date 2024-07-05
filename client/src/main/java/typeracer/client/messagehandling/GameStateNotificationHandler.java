package typeracer.client.messagehandling;

import typeracer.client.Client;
import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.GameStateNotification;


/**
 * Handles GameStateNotification messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class GameStateNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public GameStateNotificationHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   * @param client client associated with the message handling
   */
  @Override
  public void handleMessage(Message message, Client client) {
    try {
      if (message instanceof GameStateNotification gameStateNotification) {
        client.setCurrentGameState(gameStateNotification);
        switch (gameStateNotification.getGameStatus()) {
          case "RUNNING":
            System.out.println("Game is still running.");
            break;
          case "FINISHED":
            // ends the game
            viewController.endGame();
            break;
          case "WAITING_FOR_READY":
            System.out.println("Game is waiting for ready.");
        }

      } else if (nextHandler != null) {
        nextHandler.handleMessage(message, client);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
