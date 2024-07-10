package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.*;
import typeracer.communication.messages.server.ReadyResponse;

/**
 * Handles ReadyResponse messages in a chain of responsibility pattern. If the message is not of the
 * specified type, it will be passed to the next handler in the chain, if any.
 */
public class ReadyResponseHandler implements MessageHandler {
  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public ReadyResponseHandler(final MessageHandler nextHandler, ViewController viewController) {
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
    if (message instanceof ReadyResponse readyResponse) {

      switch (readyResponse.getReadyStatus()) {
        case ACCEPTED:
          System.out.println("Player is ready.");
          break;
        case DENIED:
          System.out.println("Player is not ready.");
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
