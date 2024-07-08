package typeracer.client.messagehandling;

import typeracer.client.Client;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerStateNotification;


/**
 * Handles PlayerStateNotification messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class PlayerStateNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public PlayerStateNotificationHandler(MessageHandler nextHandler) {
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
    if (message instanceof PlayerStateNotification playerStateNotification) {
      viewController.updatePlayerState(playerStateNotification.getPlayerId(), playerStateNotification.getAccuracy(),
          playerStateNotification.getProgress(), playerStateNotification.getWpm());

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, client);
    }
  }
}