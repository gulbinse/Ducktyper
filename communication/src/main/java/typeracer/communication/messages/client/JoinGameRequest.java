package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

public final class JoinGameRequest implements Message {

  private final String playerName;

  public JoinGameRequest(String playerName) {
    this.playerName = playerName;
  }

  public String getPlayerName() {
    return playerName;
  }
}
