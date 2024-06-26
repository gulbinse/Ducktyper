package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

public final class PlayerStateNotification implements Message {

  private final double accuracy;
  private final int playerId ;
  private final double progress;
  private final double wpm;

  public PlayerStateNotification(double accuracy, int playerId, double progress, double wpm) {
    this.accuracy = accuracy;
    this.playerId = playerId;
    this.progress = progress;
    this.wpm = wpm;
  }

  public double getAccuracy() {
    return accuracy;
  }

  public int getPlayerId() {
    return playerId;
  }

  public double getProgress() {
    return progress;
  }

  public double getWpm() {
    return wpm;
  }
}
