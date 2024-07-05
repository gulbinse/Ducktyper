package typeracer.client.messagehandling;

import typeracer.client.Client;
import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.CharacterResponse;

/**
 * Handles CharacterResponse messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class CharacterResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public CharacterResponseHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   * @param client client associated with the message handling
   */
  @Override
  public void handleMessage(Message message, Client client) {
    if (message instanceof CharacterResponse characterResponse) {
      System.out.println("The typed character is " + characterResponse.isCorrect() + ".");
      handleCharacterResponse((CharacterResponse) message);

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, client);
    }
  }

  private void handleCharacterResponse(CharacterResponse characterResponse) {
    boolean correct = characterResponse.isCorrect();
    if (!correct) {
      System.out.println("The character is not correct.");
    } else {
      System.out.println("The character is correct.");
    }
  }
}
