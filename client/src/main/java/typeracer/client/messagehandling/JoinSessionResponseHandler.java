package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.JoinSessionResponse;


public class JoinSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  public JoinSessionResponseHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message) {
    System.out.println(message);
    if (message instanceof JoinSessionResponse joinSessionResponse) {

      switch ((joinSessionResponse.getJoinStatus())) {
        case ACCEPTED:
          System.out.println("Player can join.");
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

