package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.CreateSessionResponse;

/**
 * Handles CreateSessionResponse messages in a chain of responsibility pattern. If the message is
 * not of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class CreateSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructs a CreateSessionResponseHandler.
   * <p>
   * Initializes the handler with the specified next handler and view controller.
   *
   * @param nextHandler the next handler in the chain of responsibility.
   * @param viewController the view controller used to update the view.
   */
  CreateSessionResponseHandler(MessageHandler nextHandler, ViewController viewController) {
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
    if (message instanceof CreateSessionResponse createSessionResponse
        && createSessionResponse.getSessionId() != 0) {
      // switches to the session scene if the message contains an ID for the session
      viewController.setSessionId(createSessionResponse.getSessionId());
      viewController.showScene(ViewController.SceneName.SESSION);
      viewController.updatePlayer(viewController.getPlayerId(), viewController.getUsername(), viewController.getPlayerReadyProperty(viewController.getPlayerId()).getValue());

    } else if (message instanceof CreateSessionResponse createSessionResponse
        && createSessionResponse.getReason() != null) {
      // reason why the session couldn't be created
      viewController.showAlert(createSessionResponse.getReason().getString());

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
