package typeracer.server.connection;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerLeftNotification;
import typeracer.server.session.Session;
import typeracer.server.session.SessionManager;
import typeracer.server.utils.IdentifierGenerator;

/** This singleton class is responsible for managing clients connecting to the server. */
public final class ConnectionManager {

  /** Status of a client->server operation. */
  public enum OperationStatus {
    /** Represents that the operation was executed successfully. */
    SUCCESS,
    /** Represents that the specified username ist not allowed. */
    INVALID_USERNAME,
    /** Represents that an undefined or unknown error occurred. */
    FAIL
  }

  private static final ConnectionManager INSTANCE = new ConnectionManager();

  private final Map<Integer, ClientHandler> clientHandlerById = new ConcurrentHashMap<>();
  private final Map<Integer, String> playerNameById = new ConcurrentHashMap<>();
  private final IdentifierGenerator identifierGenerator = new IdentifierGenerator();

  private ConnectionManager() {}

  /**
   * Handles a client connection by assigning a unique id to the client and delegating it to another
   * thread.
   *
   * @param socket the socket representing the client's connection to the server
   */
  public synchronized void handleClient(Socket socket) {
    int id = identifierGenerator.generateId();
    ClientHandler clientHandler = ClientHandler.create(socket, id);
    clientHandlerById.put(id, clientHandler);
    Thread thread = new Thread(clientHandler);
    thread.start();
  }

  /**
   * Removes an existing ClientHandler by its id. For whatever reason, SpotBugs does not like the
   * name removeClient, so now it's just called unhandleClient.
   *
   * @param clientId the unique id of the client
   */
  public synchronized void unhandleClient(int clientId) {
    clientHandlerById.remove(clientId);
    playerNameById.remove(clientId);

    Session session = SessionManager.getInstance().getSessionByClientId(clientId);
    if (session != null) {
      session.broadcastMessage(
          new PlayerLeftNotification(session.numberOfConnectedClients(), clientId));
      SessionManager.getInstance().leaveSession(clientId);
    }
  }

  /**
   * Closes the connection to the client. If the client does not exist, nothing happens.
   *
   * @param clientId the unique id of the client
   */
  public synchronized void disconnectClient(int clientId) {
    ClientHandler clientHandler = clientHandlerById.getOrDefault(clientId, null);
    if (clientHandler != null) {
      clientHandler.close();
    }
  }

  /**
   * Sends a message to the specified client. If the client does not exist, nothing happens.
   *
   * @param message the message to be sent
   * @param clientId the unique id of the client
   */
  public void sendMessage(Message message, int clientId) {
    ClientHandler clientHandler = clientHandlerById.getOrDefault(clientId, null);
    if (clientHandler != null) {
      clientHandler.sendMessage(message);
    }
  }

  /**
   * Returns a ClientHandler by its id.
   *
   * @param clientId the unique id of the client
   * @return the clientHandler associated with the given id, or null if no such handler exists
   */
  public synchronized ClientHandler getClientHandler(int clientId) {
    return clientHandlerById.getOrDefault(clientId, null);
  }

  /**
   * Returns the name of the specified player, or 'Guest' if no such player exists.
   *
   * @param clientId the unique id of the client
   * @return the player's name
   */
  public synchronized String getPlayerName(int clientId) {
    return playerNameById.getOrDefault(clientId, "Guest");
  }

  /**
   * Handles the name of the connected client's player.
   *
   * @param clientId the unique id of the client
   * @param playerName the name of the player
   * @return the operation status of this method. {@link OperationStatus#SUCCESS} if the action was
   *     successful. Otherwise, a corresponding error status.
   */
  public synchronized OperationStatus handlePlayerName(int clientId, String playerName) {
    if (playerName == null || playerName.isBlank() || playerName.isEmpty()) {
      return OperationStatus.INVALID_USERNAME;
    }
    playerNameById.put(clientId, playerName);
    return OperationStatus.SUCCESS;
  }

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class
   */
  public static synchronized ConnectionManager getInstance() {
    return INSTANCE;
  }
}
