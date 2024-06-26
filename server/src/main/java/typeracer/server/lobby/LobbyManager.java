package typeracer.server.lobby;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import typeracer.server.connection.ClientHandler;
import typeracer.server.utils.IdentifierGenerator;

/** This class is responsible for managing all lobbies. */
public final class LobbyManager {

  private static final Map<Integer, Lobby> lobbyById = new ConcurrentHashMap<>();
  private static final IdentifierGenerator identifierGenerator = new IdentifierGenerator();

  /** The default constructor of this class. */
  public LobbyManager() {}

  /** Creates a new {@link Lobby} instance and assigns it a unique id. */
  public synchronized void createNewLobby(boolean private) {}

  /**
   * Removes an existing lobby by its id.
   *
   * @param id the unique id of the lobby
   */
  public synchronized void removeLobby(int id) {}

  /**
   * Allows a client to join a lobby by its id.
   *
   * @param clientHandler the handler for the client attempting to join the lobby
   * @param id the unique id of the lobby
   */
  public synchronized void joinLobbyById(ClientHandler clientHandler, int id) {}

  /**
   * Allows a client to join a random lobby.
   *
   * @param clientHandler the handler for the client attempting to join the lobby
   */
  public synchronized void joinRandomLobby(ClientHandler clientHandler) {}

  /**
   * Allows a client to leave their current lobby.
   *
   * @param clientHandler the handler for the client attempting to leave the lobby
   */
  public synchronized void leaveLobby(ClientHandler clientHandler) {}

  /**
   * Returns a lobby by its id.
   *
   * @param id the unique id of the lobby
   * @return the lobby associated with the given id, or null if no such lobby exists
   */
  public synchronized Lobby getLobby(int id) {
    return null;
  }
}
