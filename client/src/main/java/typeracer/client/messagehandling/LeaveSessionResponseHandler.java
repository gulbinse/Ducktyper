package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.LeaveSessionResponse;
import typeracer.communication.statuscodes.PermissionStatus;

/**
 * Handles LeaveSessionResponse messages in a chain of responsibility pattern. If the message is not
 * of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class LeaveSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructs a LeaveSessionResponseHandler.
   * Initializes the handler with the specified next handler and view controller.
   *
   * @param nextHandler the next handler in the chain of responsibility.
   * @param viewController the view controller used to update the view.
   */
  LeaveSessionResponseHandler(MessageHandler nextHandler, ViewController viewController) {
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
    // ACCEPT response
    if (message instanceof LeaveSessionResponse leaveSessionResponse &&
        leaveSessionResponse.getLeaveStatus() == PermissionStatus.ACCEPTED) {
      System.out.println("Player can leave the session.");
      viewController.leaveSession();
      // DENIED response
    } else if (message instanceof LeaveSessionResponse leaveSessionResponse &&
      leaveSessionResponse.getLeaveStatus() == PermissionStatus.DENIED) {
      System.out.println("Player cannot leave the session.");
      viewController.showAlert("Error: Player cannot leave the session.");

    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
