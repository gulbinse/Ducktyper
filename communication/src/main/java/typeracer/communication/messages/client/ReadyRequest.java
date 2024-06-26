package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

public final class ReadyRequest implements Message {

  private final boolean ready;

  public ReadyRequest(boolean ready) {
    this.ready = ready;
  }

  public boolean isReady() {
    return ready;
  }
}
