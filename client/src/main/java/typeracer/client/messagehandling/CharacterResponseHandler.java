package typeracer.client.messagehandling;


import typeracer.client.Client;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.CharacterResponse;


public class CharacterResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final Client client;

  public CharacterResponseHandler(MessageHandler nextHandler, Client client) {
    this.nextHandler = nextHandler;
    this.client = client;
  }

  @Override
  public void handleMessage(Message message) {
    if (message instanceof CharacterResponse characterResponse) {
      System.out.println("The typed character is " + characterResponse.isCorrect() + ".");
      handleCharacterResponse((CharacterResponse) message);

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }

  private void handleCharacterResponse(CharacterResponse characterResponse) {
    boolean correct = characterResponse.isCorrect();
    if (!correct) {
      client.incrementTotalErrors();
    }
  }
}
