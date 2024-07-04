package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.CreateSessionResponse;
import typeracer.communication.statuscodes.Reason;

public class CreateSessionResponseHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private ViewController viewController;

  public CreateSessionResponseHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message) {
    if (message instanceof CreateSessionResponse createSessionResponse && createSessionResponse.getSessionId() != 0) {
      ViewController.switchToLobbyUi();
      viewController.setSessionID(createSessionResponse.getSessionId());
      } else if (message instanceof CreateSessionResponse createSessionResponse
        && createSessionResponse.getReason() != null) {
      viewController.showReason("Player can't create session because " + createSessionResponse.getReason().toString()
          + ".");
    }
  }
}
