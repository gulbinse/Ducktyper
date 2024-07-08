package typeracer.server.session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import typeracer.server.connection.ConnectionManager;
import typeracer.server.utils.IdentifierGenerator;

/** This singleton class is responsible for managing all sessions. */
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

  private static final SessionManager INSTANCE = new SessionManager();

  private final Map<Integer, Session> sessionBySessionId = new ConcurrentHashMap<>();
  private final Map<Integer, Integer> sessionIdByClientId = new ConcurrentHashMap<>();
  private final IdentifierGenerator identifierGenerator = new IdentifierGenerator();

  private SessionManager() {}

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
   * FOR TESTING PURPOSE ONLY. Creates a new {@link Session} instance and assigns it the specified
   * id.
   *
   * @param id the id of the session
   */
  public synchronized void createNewSession(int id) {
    Session session = new Session();
    sessionBySessionId.put(id, session);
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
      Set<Integer> clientIds = session.getPlayerIds();
      for (int clientId : clientIds) {
        ConnectionManager.getInstance().disconnectClient(clientId);
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

    session.handlePlayer(clientId);
    sessionIdByClientId.put(clientId, sessionId);
    return OperationStatus.SUCCESS;
  }

  /**
   * Allows a client to leave their current session. If the client is not in a session or the
   * session does not exist, nothing happens.
   *
   * @param clientId the id of the client attempting to leave the session
   * @return <code>true</code> if leaving was successful, <code>false</code> otherwise
   */
  public synchronized boolean leaveSession(int clientId) {
    int sessionId = sessionIdByClientId.getOrDefault(clientId, -1);
    Session session = sessionBySessionId.getOrDefault(sessionId, null);
    if (session != null) {
      session.unhandlePlayer(clientId);
      sessionIdByClientId.remove(clientId);

      // Delete empty sessions
      if (session.isEmpty()) {
        closeSession(sessionId);
      }

      return true;
    }
    return false;
  }

  /**
   * Returns the session the given client is connected to.
   *
   * @param clientId the unique id of the client
   * @return the session associated with the given client, or null if no such session exists
   */
  public synchronized Session getSessionByClientId(int clientId) {
    int sessionId = sessionIdByClientId.getOrDefault(clientId, -1);
    return sessionBySessionId.getOrDefault(sessionId, null);
  }

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class
   */
  public static synchronized SessionManager getInstance() {
    return INSTANCE;
  }
}
