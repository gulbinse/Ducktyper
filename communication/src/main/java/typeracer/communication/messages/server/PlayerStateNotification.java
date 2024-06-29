package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Notification indicating a change in the state of a player. This notification is sent from the
 * server to every client to update them about the player state.
 */
public final class PlayerStateNotification implements Message {

  private final double accuracy;
  private final int playerId;
  private final double progress;
  private final double wpm;

  /**
   * Constructs a new PlayerStateNotification with the specified arguments.
   *
   * @param accuracy the accuracy of the player
   * @param playerId the id of the player
   * @param progress the progress of the player
   * @param wpm the words per minute of the player
   */
  public PlayerStateNotification(double accuracy, int playerId, double progress, double wpm) {
    this.accuracy = accuracy;
    this.playerId = playerId;
    this.progress = progress;
    this.wpm = wpm;
  }

  /**
   * Returns the accuracy of the player.
   *
   * @return the player's accuracy
   */
  public double getAccuracy() {
    return accuracy;
  }

  /**
   * Returns the id of the player.
   *
   * @return the player's id
   */
  public int getPlayerId() {
    return playerId;
  }

  /**
   * Returns the progress of the player.
   *
   * @return the player's progress
   */
  public double getProgress() {
    return progress;
  }

  /**
   * Returns the words per minute of the player.
   *
   * @return the player's word per minute
   */
  public double getWpm() {
    return wpm;
  }
}
