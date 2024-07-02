package typeracer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import typeracer.server.connection.ConnectionManager;

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
  public static void main(String[] args) {
    int port = DEFAULT_PORT;
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--port":
          if (isLastArgument(i, args)) {
            printErrorMessage("Please specify the port number.");
            return;
          }
          try {
            i++;
            port = Integer.parseInt(args[i]);
          } catch (NumberFormatException e) {
            printErrorMessage("Invalid port number: " + args[i]);
            return;
          }
          if (!isValidPort(port)) {
            printErrorMessage("The port number should be in the range of 1024~65535.");
            return;
          }
          break;
        case "--help":
        default:
          printHelpMessage();
          return;
      }
    }

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      Server server = new Server();
      server.start(serverSocket);
    } catch (IOException e) {
      System.out.println("Connection lost. Shutting down: " + e.getMessage());
    }
  }

  private static boolean isLastArgument(int i, final String[] args) {
    return i == args.length - 1;
  }

  private static boolean isValidPort(int port) {
    return port >= 1024 && port <= 65535;
  }

  private static void printHelpMessage() {
    System.out.println("java server [--port <int>] [--help]");
  }

  private static void printErrorMessage(String error) {
    System.out.println("Error! " + error);
  }

  /**
   * The starting point of a Typeracer server. Listens for incoming connections and delegates
   * handling of new clients to {@link typeracer.server.connection.ConnectionManager}.
   *
   * @param socket the ServerSocket object listening for incoming connections
   * @throws IOException if an I/O error occurs while listening to the socket
   */
  public void start(ServerSocket socket) throws IOException {
    while (!socket.isClosed()) {
      Socket client = socket.accept();
      ConnectionManager.getInstance().handleClient(client);
    }
  }
}
