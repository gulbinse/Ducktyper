package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Notification indicating that a player has left the game. This notification is sent from the
 * server to every client to inform them about the departure of a player.
 */
public final class PlayerLeftNotification implements Message {

  private final int numPlayers;
  private final int playerId;

  /**
   * Constructs a new PlayerLeftNotification with the specified arguments.
   *
   * @param numPlayers the new number of players in the game
   * @param playerId the id of the player that has left the game
   */
  public PlayerLeftNotification(int numPlayers, int playerId) {
    this.numPlayers = numPlayers;
    this.playerId = playerId;
  }

  /**
   * Returns the new number of players in the game.
   *
   * @return the current number of players in the game
   */
  public int getNumPlayers() {
    return numPlayers;
  }

  /**
   * Returns the id of the player that has left the game.
   *
   * @return the player's id
   */
  public int getPlayerId() {
    return playerId;
  }
}
