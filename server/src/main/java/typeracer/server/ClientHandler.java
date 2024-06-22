package typeracer.server;

import java.net.Socket;
import typeracer.communication.messages.Message;
import typeracer.server.message.MessageHandler;

/**
 * This class represents a client connected to the server. It handles incoming and outgoing messages
 * and implements the Runnable interface to be scheduled in another thread.
 */
public class ClientHandler implements Runnable {

  private final Lobby lobby;
  private final Socket socket;
  private final int id;

  private final MessageHandler messageHandler = MessageHandler.createChain();

  private ClientHandler(Lobby lobby, Socket socket, int id) {
    this.lobby = lobby;
    this.socket = socket;
    this.id = id;
  }

  /**
   * Creates a new ClientHandler instance with the specified arguments and a unique id.
   *
   * @param lobby the lobby this ClientHandler belongs to
   * @param socket the socket to which this handler should be bound
   * @param id the unique id of the client/player
   * @return a new ClientHandler instance
   */
  public static ClientHandler create(Lobby lobby, Socket socket, int id) {
    return new ClientHandler(lobby, socket, id);
  }

  @Override
  public void run() {}

  private void handleMessage(Message message) {}

  private void sendMessage(Message message) {}

  private void close() {}
}
