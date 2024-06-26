package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

public final class JoinGameResponse implements Message {

  private final String joinStatus;
  private final String reason;

  public JoinGameResponse(String joinStatus, String reason) {
    this.joinStatus = joinStatus;
    this.reason = reason;
  }

  public String getJoinStatus() {
    return joinStatus;
  }

  public String getReason() {
    return reason;
  }
}
