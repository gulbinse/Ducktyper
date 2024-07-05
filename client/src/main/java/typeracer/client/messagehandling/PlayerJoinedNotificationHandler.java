package typeracer.client.messagehandling;

import typeracer.client.Client;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerJoinedNotification;

import java.io.IOException;

/**
 * Handles PlayerJoinedNotification messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class PlayerJoinedNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler next handler in chain
   */
  public PlayerJoinedNotificationHandler(MessageHandler nextHandler) {
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
    if (message instanceof PlayerJoinedNotification playerJoinedNotification) {
      System.out.println("Player " + playerJoinedNotification.getPlayerName() + " joined the game");
      System.out.println("There are " + playerJoinedNotification.getNumPlayers() + " are in the lobby");

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, client);
    }
  }
}
