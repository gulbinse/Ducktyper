package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Notification indicating that the game is played with a new text. This notification is sent from
 * the server to every client to inform them about the new text.
 */
public final class TextNotification implements Message {

  private final String text;

  /**
   * Constructs a new TextNotification with the specified arguments.
   *
   * @param text the current text in the game
   */
  public TextNotification(String text) {
    this.text = text;
  }

  /**
   * Returns the current text in the game.
   *
   * @return the current text in the game
   */
  public String getText() {
    return text;
  }
}
