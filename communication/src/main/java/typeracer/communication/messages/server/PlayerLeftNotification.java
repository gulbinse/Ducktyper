package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

public final class PlayerLeftNotification implements Message {

  private final int numPlayers;
  private final int playerId;

  public PlayerLeftNotification(int numPlayers, int playerId) {
    this.numPlayers = numPlayers;
    this.playerId = playerId;
  }

  public int getNumPlayers() {
    return numPlayers;
  }

  public int getPlayerId() {
    return playerId;
  }
}
