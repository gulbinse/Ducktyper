package typeracer.communication.messages.client;

import typeracer.communication.messages.Message;

public final class CharacterRequest implements Message {

  private final char character;

  public CharacterRequest(char character) {
    this.character = character;
  }

  public char getCharacter() {
    return character;
  }
}
