package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

public final class ReadyResponse implements Message {

  private final String readyStatus;

  public ReadyResponse(String readyStatus) {
    this.readyStatus = readyStatus;
  }

  public String getReadyStatus() {
    return readyStatus;
  }
}
