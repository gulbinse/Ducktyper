package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.JoinSessionResponse;

/**
 * Handles JoinSessionResponse messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class JoinSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public JoinSessionResponseHandler(MessageHandler nextHandler, ViewController viewController) {
    this.nextHandler = nextHandler;
    this.viewController = viewController;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   */
  @Override
  public void handleMessage(Message message) {
    System.out.println(message);
    if (message instanceof JoinSessionResponse joinSessionResponse) {

      switch ((joinSessionResponse.getJoinStatus())) {
        case ACCEPTED:
          System.out.println("Player can join.");
          viewController.switchToLobbyUi();
          break;
        case DENIED:
          System.out.println("Player can't join because " + joinSessionResponse.getReason().getString() + ".");
          break;
        default:
          if (nextHandler != null) {
            nextHandler.handleMessage(message);
          }
          break;
      }

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }

  }
}

