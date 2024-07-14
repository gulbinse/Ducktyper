package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.JoinSessionResponse;

/**
 * Handles JoinSessionResponse messages in a chain of responsibility pattern. If the message is not
 * of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class JoinSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructs a JoinSessionResponseHandler.
   * Initializes the handler with the specified next handler and view controller.
   *
   * @param nextHandler the next handler in the chain of responsibility.
   * @param viewController the view controller used to update the view.
   */
  JoinSessionResponseHandler(MessageHandler nextHandler, ViewController viewController) {
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
    if (message instanceof JoinSessionResponse joinSessionResponse) {
      switch ((joinSessionResponse.getJoinStatus())) {
        case ACCEPTED:
          viewController.showScene(ViewController.SceneName.SESSION);
          break;
          // response contains DENIED
        default:
          viewController.showAlert(joinSessionResponse.getReason().getString());
          break;
      }

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
