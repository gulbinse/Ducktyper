package typeracer.client.messagehandling;

import typeracer.client.Client;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.LeaveSessionResponse;

/**
 * Handles LeaveSessionResponse messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class LeaveSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler the next handler in message handling chain
   */
  public LeaveSessionResponseHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   * @param client client associated with the message handling
   */
  @Override
  public void handleMessage(Message message, Client client) {
    if (message instanceof LeaveSessionResponse leaveSessionResponse) {
      switch (leaveSessionResponse.getLeaveStatus()) {
        case ACCEPTED:
          System.out.println("Player can leave the session.");
        case DENIED:
          System.out.println("Player can not leave the session.");
      }
    }
  }
}
