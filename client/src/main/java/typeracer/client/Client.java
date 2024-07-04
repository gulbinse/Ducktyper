package typeracer.client;

import typeracer.client.messagehandling.CharacterResponseHandler;
import typeracer.client.messagehandling.GameStateNotificationHandler;
import typeracer.client.messagehandling.MessageHandler;
import typeracer.client.messagehandling.PlayerLeftNotificationHandler;
import typeracer.client.messagehandling.PlayerStateNotificationHandler;
import typeracer.client.messagehandling.ReadyResponseHandler;
import typeracer.client.messagehandling.PlayerJoinedNotificationHandler;
import typeracer.client.messagehandling.JoinSessionResponseHandler;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.MoshiAdapter;
import typeracer.communication.messages.client.CharacterRequest;
import typeracer.communication.messages.server.GameStateNotification;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Network client to play a Typeracer game.
 * Client connects to a server to play the game.
 * Users can join with a username, get notifications and type.
 */

public class Client {
  private static final int DEFAULT_PORT = 4441;
  private static final String DEFAULT_USERNAME = "Alina";
  private static final String DEFAULT_ADDRESS = "localhost";
  private MessageHandler messageHandlerChain;
  private final MoshiAdapter moshiAdapter = new MoshiAdapter();
  private String username;
  private int gamesPlayed;
  private int totalErrors;
  private double bestWpm;
  private double averageWpm;
  private double averageAccuracy;
  private List<String> topPlayers = new ArrayList<>();
  private double totalWpm;
  private double totalAccuracy;

  /**
   * Constructor for the client.
   */
  public Client(String username) {
    this.username = username;
  }


  public static void main(String[] args) {
    String username = DEFAULT_USERNAME;
    String serverAddress = DEFAULT_ADDRESS;
    int port = DEFAULT_PORT;

    InetAddress inetAddress = null;
    try {
      inetAddress = InetAddress.getByName(serverAddress);
    } catch (UnknownHostException e) {
      return;
    }
    assert inetAddress != null;

    if (!isValidPort(port)) {
      printErrorMessage("The port number should be in the range of 1024~65535.");
      return;
    }

    // start a client
    InetSocketAddress address = new InetSocketAddress(inetAddress, port);

    Client client = new Client();

    try (Socket socket = new Socket(address.getAddress(), address.getPort())) {
      client.start(username, socket);
    } catch (IOException e) {
      System.out.println("Connection lost. Shutting down: " + e.getMessage());
    }


  }
  private static boolean isValidPort(int port) {
    return port >= 1024 && port <= 65535;
  }

  private static void printErrorMessage(String str) {
    System.out.println("Error! " + str);
  }


  /**
   * Starts the client and listens to the server.
   *
   * @param username       name of the user
   * @param socket         given socket to start the client
   */
  public void start(String username, Socket socket) throws IOException {

    messageHandlerChain = createMessageHandlerChain();

    new Thread(() -> receiveMessage(socket)).start();
    handleUserInput(socket);
  }

  private MessageHandler createMessageHandlerChain() {
    MessageHandler readyResponseHandler = new ReadyResponseHandler(null);
    MessageHandler playerStateNotificationHandler = new PlayerStateNotificationHandler(readyResponseHandler);
    MessageHandler characterResponseHandler = new CharacterResponseHandler(playerStateNotificationHandler);
    MessageHandler gameStateNotificationHandler = new GameStateNotificationHandler(characterResponseHandler);
    MessageHandler playerLeftNotificationHandler = new PlayerLeftNotificationHandler(gameStateNotificationHandler);
    MessageHandler playerJoinedNotificationHandler = new PlayerJoinedNotificationHandler(playerLeftNotificationHandler);
    return new JoinSessionResponseHandler(playerJoinedNotificationHandler);
  }

  public void handleMessage(Message message) throws IOException {
    System.out.println("Received message: " + message);
    messageHandlerChain.handleMessage(message);
  }


  /**
   * Sends messages to the server.
   *
   * @param jsonMessage message that gets sent as a JSON
   * @param socket socket that receives the message
   */
  private void sendMessage(String jsonMessage, Socket socket) {
    try {
      OutputStream output = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(output, true);
      writer.println(jsonMessage);
      System.out.println("Sent message: " + jsonMessage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends a typed character to the server.
   * @param character the typed character
   * @throws IOException if an I/O error happens
   */
  public void sendCharacterRequest(char character, Socket socket) throws IOException {
    CharacterRequest characterRequest = new CharacterRequest(character);
    String characterJson = moshiAdapter.toJson(characterRequest);

    sendMessage(characterJson, socket);
  }

  private void receiveMessage(Socket socket) {
    try {
      InputStream Input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(Input));
      String serverMessage;
      while ((serverMessage = reader.readLine()) != null) {
        Message message = moshiAdapter.fromJson(serverMessage);
        handleMessage(message);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Handles the input of the player and sends it to the server.
   * @param socket socket from the server
   */
  private void handleUserInput(Socket socket) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();
      if (!input.isEmpty()) {
        try {
          sendCharacterRequest(input.charAt(0), socket);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


  public void saveSettings(String username, int wpmGoal, String favoriteText) {
    this.username = username;
  }
  public int getGamesPlayed() {
    return 0;
  }

  public void resetStats() {
    gamesPlayed = 0;
    totalErrors = 0;
    bestWpm = 0.0;
    averageWpm = 0.0;
    averageAccuracy = 0.0;
    topPlayers.clear();
    totalWpm = 0;
  }

  public String getCurrentGameState(GameStateNotification gameStateNotification) {
    return gameStateNotification.getGameStatus();
  }

  public List<String> getTopPlayers() {
    return topPlayers;
  }

  public String fetchNewGameText() {
    return "Es war einmal...";
  }

  public double getAverageWpm() {
    averageWpm = gamesPlayed == 0 ? 0 : totalWpm / gamesPlayed;
    return averageWpm;
  }

  public void updatePlayerState(int playerID, double progress, double wpm, double accuracy) {
    System.out.println("Player " + playerID + ":");
    System.out.println("Progress: " + progress + "%");
    System.out.println("WPM: " + wpm);
    System.out.println("Accuracy: " + accuracy + "%");
  }

  public void incrementTotalErrors() {
    totalErrors++;
  }

  public int getTotalErrors() {
    return totalErrors;

  }

  public double getBestWpm(double wpm) {
    bestWpm = Math.max(bestWpm, wpm);
    return bestWpm;
  }


  public double getAverageAccuracy() {
    averageAccuracy = gamesPlayed == 0 ? 0 : totalAccuracy / gamesPlayed;
    return averageAccuracy;
  }

  public double getTotalWpm(double wpm) {
    totalWpm = totalWpm + wpm;
    return totalWpm;
  }

  public double getTotalAccuracy(double accuracy) {
    totalAccuracy = totalAccuracy + accuracy;
    return totalAccuracy;
  }
}