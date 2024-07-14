package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.*;
import typeracer.communication.messages.server.ReadyResponse;
import typeracer.communication.statuscodes.PermissionStatus;

/**
 * Handles ReadyResponse messages in a chain of responsibility pattern. If the message is not of the
 * specified type, it will be passed to the next handler in the chain, if any.
 */
public class ReadyResponseHandler implements MessageHandler {
  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructs a ReadyResponseHandler.
   * Initializes the handler with the specified next handler and view controller.
   *
   * @param nextHandler the next handler in the chain of responsibility.
   * @param viewController the view controller used to update the view.
   */
  ReadyResponseHandler(final MessageHandler nextHandler, ViewController viewController) {
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
    if (message instanceof ReadyResponse readyResponse && readyResponse.getReadyStatus() == PermissionStatus.DENIED) {
      System.out.println(readyResponse.getReason().getString());

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
