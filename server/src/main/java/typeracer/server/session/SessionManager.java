package typeracer.server.session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.utils.IdentifierGenerator;

/** This class is responsible for managing all sessions. */
public final class SessionManager {

  private static final Map<Integer, Session> sessionBySessionId = new ConcurrentHashMap<>();
  private static final Map<Integer, Session> sessionByClientId = new ConcurrentHashMap<>();
  private static final IdentifierGenerator identifierGenerator = new IdentifierGenerator();

  /** The default constructor of this class. */
  public SessionManager() {}

  /**
   * Creates a new {@link Session} instance and assigns it a unique id.
   *
   * @return the id of the created session
   */
  public synchronized int createNewSession() {
    int id = identifierGenerator.generateId();
    Session session = new Session();
    sessionBySessionId.put(id, session);
    return id;
  }

  /**
   * Closes an existing session by its id.
   *
   * @param sessionId the unique id of the session
   */
  public synchronized void closeSession(int sessionId) {
    Session session = sessionBySessionId.getOrDefault(sessionId, null);
    if (session != null) {
      // Disconnect all clients
      ConnectionManager connectionManager = new ConnectionManager();
      Set<Integer> clientIds = session.getPlayerIds();
      for (int clientId : clientIds) {
        connectionManager.disconnectClient(clientId);
        sessionByClientId.remove(clientId);
      }

      sessionBySessionId.remove(sessionId);
    }
  }

  /**
   * Allows a client to join a session by its id.
   *
   * @param clientId the id of the client attempting to join the session
   * @param sessionId the unique id of the session
   */
  public synchronized void joinSessionById(int clientId, int sessionId) {
    Session session = sessionBySessionId.getOrDefault(sessionId, null);
    if (session != null) {
      session.addPlayer(clientId);
      sessionByClientId.put(clientId, session);
    }
    // TODO operationstatus returnen
  }

  /**
   * Allows a client to leave their current session.
   *
   * @param clientId the id of the client attempting to leave the session
   */
  public synchronized void leaveSession(int clientId) {
    Session session = sessionByClientId.getOrDefault(clientId, null);
    if (session != null) {
      session.removePlayer(clientId);
      sessionByClientId.remove(clientId);
    }
  }

  /**
   * Returns a session by its id.
   *
   * @param sessionId the unique id of the session
   * @return the session associated with the given id, or null if no such session exists
   */
  public synchronized Session getSessionBySessionId(int sessionId) {
    return sessionBySessionId.getOrDefault(sessionId, null);
  }

  /**
   * Returns the session the given client is connected to.
   *
   * @param clientId the unique id of the client
   * @return the session associated with the given client, or null if no such session exists
   */
  public synchronized Session getSessionByClientId(int clientId) {
    return sessionByClientId.getOrDefault(clientId, null);
  }
}
