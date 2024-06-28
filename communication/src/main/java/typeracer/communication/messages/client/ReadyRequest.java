package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a player's readiness status has been updated. This request is sent from a
 * client to the server to inform it about the player's updated readiness status.
 */
public final class ReadyRequest implements Message {

  private final boolean ready;

  /**
   * Constructs a new ReadyRequest with the specified arguments.
   *
   * @param ready <code>true</code> if the player is ready, <code>false</code> otherwise
   */
  public ReadyRequest(boolean ready) {
    this.ready = ready;
  }

  /**
   * Returns whether the player is ready or not.
   *
   * @return <code>true</code> if the player is ready, <code>false</code> otherwise
   */
  public boolean isReady() {
    return ready;
  }
}
