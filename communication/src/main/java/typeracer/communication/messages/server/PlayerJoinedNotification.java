package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Notification indicating that a new player has joined the game. This notification is sent from the
 * server to every client to inform them about the new player's arrival.
 */
public final class PlayerJoinedNotification implements Message {

  private final int numPlayers;
  private final int playerId;
  private final String playerName;

  /**
   * Constructs a new PlayerJoinedNotification with the specified arguments.
   *
   * @param numPlayers the current number of players in the game
   * @param playerId the id of the joined player
   * @param playerName the name of the joined player
   */
  public PlayerJoinedNotification(int numPlayers, int playerId, String playerName) {
    this.numPlayers = numPlayers;
    this.playerId = playerId;
    this.playerName = playerName;
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
   * Returns the id of the joined player.
   *
   * @return the player's id
   */
  public int getPlayerId() {
    return playerId;
  }

  /**
   * Returns the name of the joined player.
   *
   * @return the player's name
   */
  public String getPlayerName() {
    return playerName;
  }
}
