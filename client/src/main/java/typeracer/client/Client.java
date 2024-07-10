package typeracer.client;

import typeracer.client.messagehandling.*;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.MoshiAdapter;
import typeracer.communication.messages.client.HandshakeRequest;
import typeracer.communication.messages.server.*;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;


/**
 * Network client to play a Typeracer game.
 * Client connects to a server to play the game.
 * Users can join with a username, get notifications and type.
 */

public class Client {
  private static final int DEFAULT_PORT = 4441;
  private static final String DEFAULT_USERNAME = "";
  private static final String DEFAULT_ADDRESS = "";
  private MessageHandler messageHandlerChain;
  private final MoshiAdapter moshiAdapter = new MoshiAdapter();
  private Socket socket = null;

  /**
   * Constructor for the client.
   */
  public Client() {}

  /**
   * Entry to <code>Client</code>.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    String username = DEFAULT_USERNAME;
    String serverAddress = DEFAULT_ADDRESS;
    int port = DEFAULT_PORT;
    for (int i = 0; i < args.length; ++i) {
      switch (args[i]) {
        case "--username": {
          if (isLastArgument(i, args)) {
            printErrorMessage("Please specify the username.");
            return;
          }
          ++i;
          username = args[i];
          break;
        }
        case "--address": {
          if (isLastArgument(i, args)) {
            printErrorMessage("Please specify the server address.");
            return;
          }
          ++i;
          serverAddress = args[i];
          break;
        }
        case "--port": {
          if (isLastArgument(i, args)) {
            printErrorMessage("Please specify the port number.");
            return;
          }
          try {
            ++i;
            port = Integer.parseInt(args[i]);
          } catch (NumberFormatException e) {
            printErrorMessage("Invalid port number: " + args[i]);
            return;
          }
          break;
        }
        case "--help":
        default: {
          printHelpMessage();
          return;
        }
      }
    }

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

    // start a client
    Client client = new Client();
  }

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

  /**
   * Prints the help message for the server.
   */
  public static void printHelpMessage() {
    System.out.println(
        "java Client [--username <String>] [--address <String>] [--port <int>] [--help]");
  }

  /**
   * Prints error message with given string.
   * @param str error message string
   */
  private static void printErrorMessage(String str) {
    System.out.println("Error! " + str);
  }

  /**
   * Starts the client and listens to the server.
   *
   * @param username name of the user
   * @param socket socket used to connect with the server
   */
  public void start(String username, Socket socket) {
    messageHandlerChain = createMessageHandlerChain();

    // new Thread to receive messages from the server
    new Thread(() -> receiveMessage(socket)).start();
    sendMessage(new HandshakeRequest(username));
  }

  public void connect(String ip, int port, String username) {
    InetSocketAddress address = new InetSocketAddress(ip, port);
    try (Socket socket = new Socket(address.getAddress(), address.getPort())) {
      this.socket = socket;
      start(username, socket);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private MessageHandler createMessageHandlerChain() {
    MessageHandler characterResponseHandler = new CharacterResponseHandler(null);
    MessageHandler handShakeResponseHandler = new HandShakeResponseHandler(characterResponseHandler);
    MessageHandler createSessionResponseHandler = new CreateSessionResponseHandler(handShakeResponseHandler);
    MessageHandler readyResponseHandler = new ReadyResponseHandler(createSessionResponseHandler);
    MessageHandler textNotificationHandler = new TextNotificationHandler(readyResponseHandler);
    MessageHandler playerStateNotificationHandler = new PlayerStateNotificationHandler(textNotificationHandler);
    MessageHandler gameStateNotificationHandler = new GameStateNotificationHandler(playerStateNotificationHandler);
    MessageHandler playerLeftNotificationHandler = new PlayerLeftNotificationHandler(gameStateNotificationHandler);
    MessageHandler playerJoinedNotificationHandler = new PlayerJoinedNotificationHandler(playerLeftNotificationHandler);
    return new JoinSessionResponseHandler(playerJoinedNotificationHandler);
  }

  public void handleMessage(Message message, Client client) throws IOException {
    System.out.println("Received message: " + message);
    messageHandlerChain.handleMessage(message, client);
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
      PrintWriter writer = new PrintWriter(output, true);
      writer.println(json);
      System.out.println("Sent message: " + json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Receives the messages from the server through given socket.
   *
   * @param socket socket used for communication
   */
  private void receiveMessage(Socket socket) {
    try {
      Client client = new Client();
      InputStream Input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(Input));
      String serverMessage;
      while ((serverMessage = reader.readLine()) != null) {
        Message message = moshiAdapter.fromJson(serverMessage);
        handleMessage(message, client);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}