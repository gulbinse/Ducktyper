package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a player is attempting to leave its session. This request is sent from a
 * client to the server to inform it about the player's attempt to leave.
 */
public final class LeaveSessionRequest implements Message {

  /** Constructs a new LeaveSessionRequest. */
  public LeaveSessionRequest() {}
}
