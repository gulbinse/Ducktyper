package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

public final class CharacterResponse implements Message {

  private final boolean correct;

  public CharacterResponse(boolean correct) {
    this.correct = correct;
  }

  public boolean isCorrect() {
    return correct;
  }
}
