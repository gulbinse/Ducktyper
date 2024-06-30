package typeracer.server.message.handlers;

import typeracer.communication.messages.Message;
import typeracer.communication.messages.client.JoinGameRequest;
import typeracer.communication.messages.server.JoinGameResponse;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.message.MessageHandler;

/**
 * Handles JoinGameRequest messages in a chain of responsibility pattern. If the message is not of
 * the specified type, it will be passed to the next handler in the chain, if any.
 */
public class JoinGameRequestHandler implements MessageHandler {

  private final MessageHandler nextHandler;

  /** The default constructor of this class. */
  public JoinGameRequestHandler() {
    this.nextHandler = null;
  }

  private JoinGameRequestHandler(MessageHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handleMessage(Message message, int clientId) {
    if (message instanceof JoinGameRequest joinGameRequest) {
      ConnectionManager connectionManager = new ConnectionManager();
      connectionManager.setPlayerName(clientId, joinGameRequest.getPlayerName());
      connectionManager.sendMessage(new JoinGameResponse("ACCEPTED", null), clientId);
      // TODO
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message, clientId);
    }
  }

  @Override
  public JoinGameRequestHandler setNext(MessageHandler handler) {
    return new JoinGameRequestHandler(handler);
  }
}
