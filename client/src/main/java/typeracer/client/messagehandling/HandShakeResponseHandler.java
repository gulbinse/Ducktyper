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
   * Constructs a HandShakeResponseHandler.
   * Initializes the handler with the specified next handler and view controller.
   *
   * @param nextHandler the next handler in the chain of responsibility.
   * @param viewController the view controller used to update the view.
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
      if (message instanceof HandshakeResponse handShakeResponse) {
        switch (handShakeResponse.getConnectionStatus()) {
          case ACCEPTED:
            System.out.println("Accepted connection");
            viewController.setPlayerId(handShakeResponse.getPlayerId());
            viewController.showScene(ViewController.SceneName.MAIN_MENU);
            break;
          case DENIED:
            System.out.println(
                "Denied connection because: " + handShakeResponse.getReason().getString());
            viewController.showAlert("Denied connection because: " + handShakeResponse.getReason().getString());
            break;
          default:
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
