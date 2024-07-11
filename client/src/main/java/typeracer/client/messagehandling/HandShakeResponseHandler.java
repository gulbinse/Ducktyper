package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.HandshakeResponse;

/**
 * Handles HandShakeResponse messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class HandShakeResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public HandShakeResponseHandler(MessageHandler nextHandler, ViewController viewController) {
    this.nextHandler = nextHandler;
    this.viewController = viewController;
  }

  /**
   * Handles incoming messages.
   *
   * @param message the message to handle
   */
  @Override
  public void handleMessage(Message message) {
    try {
      System.out.println(message);
      if (message instanceof HandshakeResponse handShakeResponse) {
        switch (handShakeResponse.getConnectionStatus()) {
          case ACCEPTED:
            System.out.println("Accepted connection");
            viewController.showScene(ViewController.SceneName.MAIN_MENU);
            break;
          case DENIED:
            System.out.println(
                "Denied connection because :" + handShakeResponse.getReason().getString());
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
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
