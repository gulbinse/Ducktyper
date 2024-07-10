package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.CreateSessionResponse;


/**
 * Handles CreateSessionResponse messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class CreateSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public CreateSessionResponseHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   */
  @Override
  public void handleMessage(Message message) {
    if (message instanceof CreateSessionResponse createSessionResponse && createSessionResponse.getSessionId() != 0) {
      // switches to the lobby scene if the message contains an ID for the session
      viewController.switchToLobbyUi();
      } else if (message instanceof CreateSessionResponse createSessionResponse
        && createSessionResponse.getReason() != null) {
      // reason why the session couldn't be created
      System.out.println("Player can't create session because " + createSessionResponse.getReason().getString()
          + ".");
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
