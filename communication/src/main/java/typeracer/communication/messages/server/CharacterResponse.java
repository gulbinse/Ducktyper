package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Response indicating the correctness of a player's typed character. This response is sent from the
 * server to a specific client after receiving a {@link
 * typeracer.communication.messages.client.CharacterRequest} from that client. It informs the client
 * whether the typed character is correct.
 */
public final class CharacterResponse implements Message {

  private final boolean correct;

  /**
   * Constructs a new CharacterResponse with the specified arguments.
   *
   * @param correct <code>true</code> if the character is correct, <code>false</code> otherwise
   */
  public CharacterResponse(boolean correct) {
    this.correct = correct;
  }

  /**
   * Returns whether the typed character is correct.
   *
   * @return <code>true</code> if the character is correct, <code>false</code> otherwise
   */
  public boolean isCorrect() {
    return correct;
  }
}
