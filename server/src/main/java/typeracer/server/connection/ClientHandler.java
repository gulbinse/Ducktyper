package typeracer.server.connection;

import java.net.Socket;
import typeracer.communication.messages.Message;

/**
 * This class represents a client connected to the server. It handles incoming and outgoing messages
 * and implements the Runnable interface to be scheduled in another thread.
 */
public class ClientHandler implements Runnable {

  private final Socket socket;
  private final int id;

  private ClientHandler(Socket socket, int id) {
    this.socket = socket;
    this.id = id;
  }

  /**
   * Creates a new ClientHandler instance with the specified arguments and a unique id.
   *
   * @param socket the socket to which this handler should be bound
   * @param id the unique id of the client/player
   * @return a new ClientHandler instance
   */
  public static ClientHandler create(Socket socket, int id) {
    return new ClientHandler(socket, id);
  }

  @Override
  public void run() {}

  private void handleMessage(Message message) {}

  private void sendMessage(Message message) {}

  private void close() {}
}
