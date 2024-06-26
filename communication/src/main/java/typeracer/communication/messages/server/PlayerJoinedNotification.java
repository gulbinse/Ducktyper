package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

public final class PlayerJoinedNotification implements Message {

  private final int numPlayers;
  private final int playerId;
  private final String playerName;

  public PlayerJoinedNotification(int numPlayers, int playerId, String playerName) {
    this.numPlayers = numPlayers;
    this.playerId = playerId;
    this.playerName = playerName;
  }

  public int getNumPlayers() {
    return numPlayers;
  }

  public int getPlayerId() {
    return playerId;
  }

  public String getPlayerName() {
    return playerName;
  }
}
