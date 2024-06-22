package typeracer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import typeracer.server.utils.IdentifierGenerator;

/** Main class for the game server. */
public class Server {

  private static final int DEFAULT_PORT = 4441;
  private static final Collection<Lobby> lobbies = new ArrayList<>();

  private final IdentifierGenerator identifierGenerator = new IdentifierGenerator();

  /** The default constructor of this class. */
  public Server() {}

  /**
   * Main method for the server.
   *
   * @param args CommandLine arguments
   */
  public static void main(String[] args) {}

  /** Creates a new {@link Lobby} instance and adds it to the collection of lobbies. */
  public static void createNewLobby() {}

  /**
   * Removes the specified lobby from the collection of lobbies.
   *
   * @param lobby the lobby to be removed
   */
  public static void closeLobby(Lobby lobby) {}

  /**
   * The starting point of a Typeracer server. Listens for incoming connections and delegates
   * handling of new clients to separate ClientHandler threads.
   *
   * @param socket the ServerSocket object listening for incoming connections
   * @throws IOException if an I/O error occurs while listening to the socket
   */
  public void start(ServerSocket socket) throws IOException {}

  private Lobby getJoinableLobby() {
    return null;
  }
}
