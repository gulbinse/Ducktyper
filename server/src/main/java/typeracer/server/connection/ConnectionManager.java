package typeracer.server.connection;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import typeracer.server.utils.IdentifierGenerator;

/** This class is responsible for managing clients connecting to the server. */
public final class ConnectionManager {

  private static final Map<Integer, ClientHandler> clientHandlerById = new HashMap<>();
  private static final IdentifierGenerator identifierGenerator = new IdentifierGenerator();

  /** The default constructor of this class. */
  public ConnectionManager() {}

  /**
   * Handles a client connection by assigning a unique id to the client and delegating it to another
   * thread.
   *
   * @param socket the socket representing the client's connection to the server
   */
  public synchronized void handleClient(Socket socket) {}

  /**
   * Removes an existing clientHandler by its id.
   *
   * @param id the unique id of the clientHandler
   */
  public synchronized void removeClient(int id) {}

  /**
   * Returns a clientHandler by its id.
   *
   * @param id the unique id of the clientHandler
   * @return the clientHandler associated with the given id, or null if no such handler exists
   */
  public synchronized ClientHandler getClientHandler(int id) {
    return null;
  }
}
