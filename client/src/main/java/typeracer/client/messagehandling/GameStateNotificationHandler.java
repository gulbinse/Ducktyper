package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.GameStateNotification;

import static typeracer.game.GameState.GameStatus.*;

public class GameStateNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  public GameStateNotificationHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message) {
    try {
      if (message instanceof GameStateNotification gameStateNotification) {
        switch (gameStateNotification.getGameStatus()) {
          case "RUNNING":
            System.out.println("Game is still running.");
            break;
          case "FINISHED":
            viewController.endGame();
            break;
          case "WAITING_FOR_READY":
            System.out.println("Game is waiting for ready.");
        }

      } else if (nextHandler != null) {
        nextHandler.handleMessage(message);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
