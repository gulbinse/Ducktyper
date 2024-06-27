package typeracer.server;

import java.io.IOException;
import java.net.ServerSocket;

/** Main class for the game server. */
public class Server {

  private static final int DEFAULT_PORT = 4441;

  /** The default constructor of this class. */
  public Server() {}

  /**
   * Main method for the server.
   *
   * @param args CommandLine arguments
   */
  public static void main(String[] args) {}

  /**
   * The starting point of a Typeracer server. Listens for incoming connections and delegates
   * handling of new clients to {@link typeracer.server.connection.ConnectionManager}.
   *
   * @param socket the ServerSocket object listening for incoming connections
   * @throws IOException if an I/O error occurs while listening to the socket
   */
  public void start(ServerSocket socket) throws IOException {}
}
