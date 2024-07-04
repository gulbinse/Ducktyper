package typeracer.client.messagehandling;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerLeftNotification;

public class PlayerLeftNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  public PlayerLeftNotificationHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message) {
    if (message instanceof PlayerLeftNotification playerLeftNotification) {
      System.out.println("Player " + playerLeftNotification.getPlayerId() + " left the game.");
      System.out.println("There are " + playerLeftNotification.getNumPlayers() + " players left.");

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
