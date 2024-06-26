package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

public final class GameStateNotification implements Message {

  private final String gameStatus;

  public GameStateNotification(String gameStatus) {
    this.gameStatus = gameStatus;
  }

  public String getGameStatus() {
    return gameStatus;
  }
}
