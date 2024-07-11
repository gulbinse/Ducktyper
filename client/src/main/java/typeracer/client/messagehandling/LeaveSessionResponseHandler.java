package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.LeaveSessionResponse;

/**
 * Handles LeaveSessionResponse messages in a chain of responsibility pattern. If the message is not
 * of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class LeaveSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public LeaveSessionResponseHandler(MessageHandler nextHandler, ViewController viewController) {
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
    if (message instanceof LeaveSessionResponse leaveSessionResponse) {
      switch (leaveSessionResponse.getLeaveStatus()) {
        case ACCEPTED:
          System.out.println("Player can leave the session.");
          viewController.showScene(ViewController.SceneName.LOBBY);
        case DENIED:
          System.out.println("Player can not leave the session.");
        default:
          nextHandler.handleMessage(message);
      }
    }
  }
}
