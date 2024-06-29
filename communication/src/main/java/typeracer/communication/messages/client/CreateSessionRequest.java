package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a player is attempting to create a session. This request is sent from a
 * client to the server to inform it about the player's attempt to create.
 */
public final class CreateSessionRequest implements Message {

  /** Constructs a new CreateSessionRequest. */
  public CreateSessionRequest() {}
}
