package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Notification indicating that a player has updated. This notification is sent from the server to
 * every client to inform them about the player's update.
 */
public final class PlayerUpdateNotification implements Message {

  private final int numPlayers;
  private final int playerId;
  private final String playerName;
  private final boolean ready;

  /**
   * Constructs a new PlayerUpdateNotification with the specified arguments.
   *
   * @param numPlayers the current number of players in the game
   * @param playerId the id of the updated player
   * @param playerName the name of the updated player
   * @param ready <code>true</code> if the player is ready, <code>false</code> otherwise
   */
  public PlayerUpdateNotification(int numPlayers, int playerId, String playerName, boolean ready) {
    this.numPlayers = numPlayers;
    this.playerId = playerId;
    this.playerName = playerName;
    this.ready = ready;
  }

  /**
   * Returns the current number of players in the game.
   *
   * @return the number of players in the game
   */
  public int getNumPlayers() {
    return numPlayers;
  }

  /**
   * Returns the id of the updated player.
   *
   * @return the player's id
   */
  public int getPlayerId() {
    return playerId;
  }

  /**
   * Returns the name of the updated player.
   *
   * @return the player's name
   */
  public String getPlayerName() {
    return playerName;
  }

  /**
   * Returns whether the player is ready or not.
   *
   * @return <code>true</code> if the player is ready, <code>false</code> otherwise
   */
  public boolean isReady() {
    return ready;
  }
}
