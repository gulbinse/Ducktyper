package typeracer.server;

import java.net.Socket;
import typeracer.communication.messages.Message;
import typeracer.server.messagehandling.CharacterRequestHandler;
import typeracer.server.messagehandling.JoinGameRequestHandler;
import typeracer.server.messagehandling.MessageHandler;

/**
 * This class represents a client connected to the server. It handles incoming and outgoing messages
 * and implements the Runnable interface to be scheduled in another thread.
 */
public class ClientHandler implements Runnable {

  private final Socket socket;
  private final GameController gameController;

  private final MessageHandler characterHandler = new CharacterRequestHandler(null);
  private final MessageHandler joinGameHandler = new JoinGameRequestHandler(characterHandler);

  private ClientHandler(Socket socket, GameController gameController) {
    this.socket = socket;
    this.gameController = gameController;
  }

  /**
   * Creates a new ClientHandler instance with the specified arguments.
   *
   * @param socket the socket to which this handler should be bound
   * @param gameController the controller responsible for managing the game
   * @return a new ClientHandler instance
   */
  public static ClientHandler create(Socket socket, GameController gameController) {
    return new ClientHandler(socket, gameController);
  }

  @Override
  public void run() {}

  private void handleMessage(Message message) {}

  private void sendMessage(Message message) {}

  private void close() {}
}
