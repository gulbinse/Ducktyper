package typeracer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import typeracer.client.messagehandling.CharacterResponseHandler;
import typeracer.client.messagehandling.CreateSessionResponseHandler;
import typeracer.client.messagehandling.GameStateNotificationHandler;
import typeracer.client.messagehandling.HandShakeResponseHandler;
import typeracer.client.messagehandling.JoinSessionResponseHandler;
import typeracer.client.messagehandling.LeaveSessionResponseHandler;
import typeracer.client.messagehandling.MessageHandler;
import typeracer.client.messagehandling.PlayerLeftNotificationHandler;
import typeracer.client.messagehandling.PlayerStateNotificationHandler;
import typeracer.client.messagehandling.PlayerUpdateNotificationHandler;
import typeracer.client.messagehandling.ReadyResponseHandler;
import typeracer.client.messagehandling.TextNotificationHandler;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.MoshiAdapter;
import typeracer.communication.messages.client.HandshakeRequest;

/**
 * Network client to play a Typeracer game. Client connects to a server to play the game. Users can
 * join with a username, get notifications and type.
 */
final class Client {
  private static int DEFAULT_PORT = 4441;
  private static final String DEFAULT_USERNAME = "alina";
  private static final String DEFAULT_ADDRESS = "localhost";
  private MessageHandler messageHandlerChain;
  private final MoshiAdapter moshiAdapter = new MoshiAdapter();
  private Socket socket;
  private final ViewController viewController;
  private BufferedReader reader;

  /** Constructor for the client. */
  public Client(ViewController viewController) {
    this.viewController = viewController;
    // ViewController.launch();

    String username = DEFAULT_USERNAME;
    String serverAddress = DEFAULT_ADDRESS;
    int port = DEFAULT_PORT;
    // check validity
    if (!isValidName(username)) {
      printErrorMessage("Invalid username: " + username);
      return;
    }

    String ip = null;
    try {
      InetAddress inetAddress = InetAddress.getByName(serverAddress);
      ip = inetAddress.getHostAddress();
    } catch (UnknownHostException e) {
      printErrorMessage("Invalid server address: " + serverAddress);
      return;
    }
    assert ip != null;

    if (!isValidPort(port)) {
      printErrorMessage("The port number should be in the range of 1024~65535.");
      return;
    }
  }

  /**
   * Entry to <code>Client</code>.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {}

  /**
   * Check if the given index is the last argument in the array.
   *
   * @param i index to check
   * @param args array of arguments
   * @return true if index is last argument
   */
  private static boolean isLastArgument(int i, final String[] args) {
    return i == args.length - 1;
  }

  /**
   * Check if the given port number is valid.
   *
   * @param port number of port to check
   * @return number true if port number is within the valid range
   */
  private static boolean isValidPort(int port) {
    return port >= 1024 && port <= 65535;
  }

  /**
   * Check if the given username is valid.
   *
   * @param username name of the user to check
   * @return true if the username is not null and not empty
   */
  private static boolean isValidName(String username) {
    return username != null && !username.isBlank();
  }

  /** Prints the help message for the server. */
  public static void printHelpMessage() {
    System.out.println(
        "java Client [--username <String>] [--address <String>] [--port <int>] [--help]");
  }

  /**
   * Prints error message with given string.
   *
   * @param str error message string
   */
  private static void printErrorMessage(String str) {
    System.out.println("Error! " + str);
  }

  /**
   * Starts the client and listens to the server.
   *
   * @param username name of the user
   */
  public void start(String username) {
    messageHandlerChain = createMessageHandlerChain();
    sendMessage(new HandshakeRequest(username));

    try {
      reader =
          new BufferedReader(
              new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.err.println("Client start not possible: " + e.getMessage());
    }
    // new Thread to receive messages from the server
    new Thread(() -> receiveMessage(socket)).start();
  }

  /**
   * Creates a connection to the server using the given IP adress and port, starts the session with
   * the given username.
   *
   * @param ip ip address of the server
   * @param port port number of the server
   * @param username username used for the session
   */
  public void connect(String ip, int port, String username) {
    InetSocketAddress address = new InetSocketAddress(ip, port);
    try {
      Socket socket = new Socket(address.getAddress(), address.getPort());
      this.socket = socket;
      start(username);
    } catch (IOException e) {
      viewController.showAlert("Connection refused: Unable to connect to server");
    }
  }

  /**
   * Chain of message handlers for processing the different types of messages.
   *
   * @return the first handler in the chain of message handlers
   */
  private MessageHandler createMessageHandlerChain() {
    MessageHandler characterResponseHandler = MessageHandler.create(CharacterResponseHandler.class, null, viewController);
    MessageHandler handShakeResponseHandler =
        MessageHandler.create(HandShakeResponseHandler.class, characterResponseHandler, viewController);
    MessageHandler createSessionResponseHandler =
        MessageHandler.create(CreateSessionResponseHandler.class, handShakeResponseHandler, viewController);
    MessageHandler readyResponseHandler =
        MessageHandler.create(ReadyResponseHandler.class, createSessionResponseHandler, viewController);
    MessageHandler textNotificationHandler =
        MessageHandler.create(TextNotificationHandler.class, readyResponseHandler, viewController);
    MessageHandler playerStateNotificationHandler =
        MessageHandler.create(PlayerStateNotificationHandler.class, textNotificationHandler, viewController);
    MessageHandler gameStateNotificationHandler =
        MessageHandler.create(GameStateNotificationHandler.class, playerStateNotificationHandler, viewController);
    MessageHandler playerLeftNotificationHandler =
        MessageHandler.create(PlayerLeftNotificationHandler.class, gameStateNotificationHandler, viewController);
    MessageHandler playerJoinedNotificationHandler =
        MessageHandler.create(PlayerUpdateNotificationHandler.class, playerLeftNotificationHandler, viewController);
    MessageHandler joinSessionResponseHandler =
        MessageHandler.create(JoinSessionResponseHandler.class, playerJoinedNotificationHandler, viewController);
    return MessageHandler.create(LeaveSessionResponseHandler.class, joinSessionResponseHandler, viewController);
  }

  /**
   * Handles the incoming message by passing it to the message handler chain.
   *
   * @param message the message to be handled.
   * @throws IOException if an I/O error occurs during message handling.
   */
  public void handleMessage(Message message) throws IOException {
    messageHandlerChain.handleMessage(message);
  }

  /**
   * Sends messages to the server.
   *
   * @param message message that gets sent as a JSON
   */
  public void sendMessage(Message message) {
    try {
      String json = moshiAdapter.toJson(message);
      OutputStream output = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(output, true, StandardCharsets.UTF_8);
      writer.println(json);
      System.out.println("Sent message: " + json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Receives the messages from the server through given socket. */
  private void receiveMessage(Socket socket) {
    try {
      String serverMessage;
      while (socket.isConnected()
          && !socket.isClosed()
          && (serverMessage = reader.readLine()) != null) {
        Message message = moshiAdapter.fromJson(serverMessage);
        handleMessage(message);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      viewController.showServerCrashMessage("Server got bonked by a goose. " +
              "We already called NASA and FBI for help. Please try again later");
      viewController.showScene(ViewController.SceneName.INITIAL_PROMPT);
    }
  }
}
