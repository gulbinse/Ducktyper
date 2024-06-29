package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

/**
 * Request indicating that a new character was typed. This request is sent from a client to the
 * server to inform it about the player's typed character.
 */
public final class CharacterRequest implements Message {

  private final char character;

  /**
   * Constructs a new CharacterRequest with the specified arguments.
   *
   * @param character the player's typed character
   */
  public CharacterRequest(char character) {
    this.character = character;
  }

  /**
   * Returns the typed character.
   *
   * @return the typed character
   */
  public char getCharacter() {
    return character;
  }
}
