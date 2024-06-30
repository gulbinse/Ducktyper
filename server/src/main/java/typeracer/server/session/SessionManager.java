package typeracer.server.session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.utils.IdentifierGenerator;

/** This class is responsible for managing all sessions. */
public final class SessionManager {

  /** Status of a session operation. */
  public enum OperationStatus {
    /** Represents that the operation was executed successfully. */
    SUCCESS,
    /** Represents that the specified session does not exist. */
    SESSION_NOT_FOUND,
    /** Represents that the session's game has already started. */
    SESSION_GAME_ALREADY_STARTED,
    /** Represents that the session is full. */
    SESSION_FULL,
    /** Represents that an undefined or unknown error occurred. */
    FAIL
  }

  private static final Map<Integer, Session> sessionBySessionId = new ConcurrentHashMap<>();
  private static final Map<Integer, Integer> sessionIdByClientId = new ConcurrentHashMap<>();
  private static final IdentifierGenerator identifierGenerator = new IdentifierGenerator();

  /** The default constructor of this class. */
  public SessionManager() {}

  /**
   * Creates a new {@link Session} instance and assigns it a unique id.
   *
   * @return the id of the created session, -1 if creation failed
   */
  public synchronized int createNewSession() {
    int id = identifierGenerator.generateId();
    Session session = new Session();
    sessionBySessionId.put(id, session);
    return id;
  }

  /**
   * Closes an existing session by its id and disconnects all corresponding clients. If the session
   * does not exist, nothing happens.
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
        sessionIdByClientId.remove(clientId);
      }

      sessionBySessionId.remove(sessionId);
    }
  }

  /**
   * Allows a client to join a session by its id.
   *
   * @param clientId the id of the client attempting to join the session
   * @param sessionId the unique id of the session
   * @return the operation status of this method. {@link OperationStatus#SUCCESS} if the action was
   *     successful. Otherwise, a corresponding error status.
   */
  public synchronized OperationStatus joinSessionById(int clientId, int sessionId) {
    Session session = sessionBySessionId.getOrDefault(sessionId, null);
    if (session == null) {
      return OperationStatus.SESSION_NOT_FOUND;
    }
    if (session.hasGameStarted()) {
      return OperationStatus.SESSION_GAME_ALREADY_STARTED;
    }
    if (session.isFull()) {
      return OperationStatus.SESSION_FULL;
    }

    session.addPlayer(clientId);
    sessionIdByClientId.put(clientId, sessionId);
    return OperationStatus.SUCCESS;
  }

  /**
   * Allows a client to leave their current session. If the client is not in a session or the
   * session does not exist, nothing happens.
   *
   * @param clientId the id of the client attempting to leave the session
   */
  public synchronized void leaveSession(int clientId) {
    int sessionId = sessionIdByClientId.getOrDefault(clientId, null);
    Session session = sessionBySessionId.getOrDefault(sessionId, null);
    if (session != null) {
      session.removePlayer(clientId);
      sessionIdByClientId.remove(clientId);

      // Delete empty sessions
      if (session.isEmpty()) {
        sessionBySessionId.remove(sessionId);
      }
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
    int sessionId = sessionIdByClientId.getOrDefault(clientId, null);
    return sessionBySessionId.getOrDefault(sessionId, null);
  }
}
