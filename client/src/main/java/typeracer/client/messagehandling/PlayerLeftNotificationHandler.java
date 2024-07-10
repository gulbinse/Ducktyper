package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerLeftNotification;

/**
 * Handles PlayerLeftNotification messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class PlayerLeftNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler he next handler in message handling chain
   */
  public PlayerLeftNotificationHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   */
  @Override
  public void handleMessage(Message message) {
    if (message instanceof PlayerLeftNotification playerLeftNotification) {
      System.out.println("Player " + playerLeftNotification.getPlayerId() + " left the game.");
      System.out.println("There are " + playerLeftNotification.getNumPlayers() + " players left.");
      viewController.removePlayerFromGame(playerLeftNotification.getPlayerId());
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
