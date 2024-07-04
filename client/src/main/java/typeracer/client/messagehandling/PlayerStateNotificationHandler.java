package typeracer.client.messagehandling;


import typeracer.client.Client;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerStateNotification;


public class PlayerStateNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final Client client;

  public PlayerStateNotificationHandler(MessageHandler nextHandler, Client client) {
    this.nextHandler = nextHandler;
    this.client = client;
  }

  @Override
  public void handleMessage(Message message) {
    if (message instanceof PlayerStateNotification playerStateNotification) {

      int playerId = playerStateNotification.getPlayerId();
      double accuracy = playerStateNotification.getAccuracy();
      double progress = playerStateNotification.getProgress();
      double wpm = playerStateNotification.getWpm();

      client.updatePlayerState(playerId, accuracy, progress, wpm);

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}