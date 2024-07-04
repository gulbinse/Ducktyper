package typeracer.client.messagehandling;

import typeracer.communication.messages.*;
import typeracer.communication.messages.server.ReadyResponse;

import java.io.IOException;

public class ReadyResponseHandler implements MessageHandler {
  private final MessageHandler nextHandler;

  public ReadyResponseHandler(final MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message) {
    if (message instanceof ReadyResponse readyResponse) {

      switch (readyResponse.getReadyStatus()) {
        case ACCEPTED:
          System.out.println("Player is ready.");
          break;
        case DENIED:
          System.out.println("Player is not ready.");
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
