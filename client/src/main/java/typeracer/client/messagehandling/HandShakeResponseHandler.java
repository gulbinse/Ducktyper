package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.HandshakeResponse;

import java.io.IOException;


public class HandShakeResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  public HandShakeResponseHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message) {
    try {
      System.out.println(message);
      if (message instanceof HandshakeResponse handShakeResponse) {
        switch (handShakeResponse.getConnectionStatus()) {

          case ACCEPTED:
            System.out.println("Accepted connection");
            ViewController.switchToMainMenu();
            break;
          case DENIED:
            System.out.println("Denied connection because :" + handShakeResponse.getReason().getString());
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
