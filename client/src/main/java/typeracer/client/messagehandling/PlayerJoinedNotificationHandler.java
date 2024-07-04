package typeracer.client.messagehandling;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerJoinedNotification;

import java.io.IOException;

public class PlayerJoinedNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  public PlayerJoinedNotificationHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message) {
    if (message instanceof PlayerJoinedNotification playerJoinedNotification) {
      System.out.println("Player " + playerJoinedNotification.getPlayerName() + " joined the game");
      System.out.println("There are " + playerJoinedNotification.getNumPlayers() + " are in the lobby");

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
