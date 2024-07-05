package typeracer.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import typeracer.client.messagehandling.*;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.MoshiAdapter;
import typeracer.communication.messages.client.CharacterRequest;
import typeracer.communication.messages.server.GameStateNotification;
import typeracer.communication.messages.server.PlayerStateNotification;
import typeracer.communication.messages.server.TextNotification;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


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
  private String username;
  private int gamesPlayed;
  private int totalErrors;
  private int bestWpm;
  private int averageWpm;
  private double averageAccuracy;
  private List<String> topPlayers = new ArrayList<>();
  private int totalWpm;
  private double totalAccuracy;
  private String gameStatus;
  private String text;
  private int wpmGoal;
  private String favoriteText;
  private int playerId;
  private double accuracy;
  private double progress;
  private int wpm;

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

    InetAddress inetAddress = null;
    try {
      inetAddress = InetAddress.getByName(serverAddress);
    } catch (UnknownHostException e) {
      printErrorMessage("Invalid server address: " + serverAddress);
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
    // handles user input
    handleUserInput(socket);
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
  private void sendCharacterRequest(char character, Socket socket) throws IOException {
    CharacterRequest characterRequest = new CharacterRequest(character);
    String characterJson = moshiAdapter.toJson(characterRequest);

    sendMessage(characterJson, socket);
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

  /**
   * Handles the input of the player and sends it to the server.
   *
   * @param socket socket used for communication
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

  /**
   * Saves the settings of the player.
   *
   * @param username name of the player
   * @param wpmGoal words per minute goal
   * @param favoriteText customize text
   */
  public void saveSettings(String username, int wpmGoal, String favoriteText) {
    this.username = username;
    this.wpmGoal = wpmGoal;
    this.favoriteText = favoriteText;
  }

  /** Increments the amount of played games. */
  public void incrementGamesPlayed() {
    this.gamesPlayed++;
  }

  /**
   * Returns the amount of played games.
   *
   * @return amount of played games by the player
   */
  public int getGamesPlayed() {
    return gamesPlayed;
  }

  /** Resets all the stats from the player. */
  public void resetStats() {
    this.gamesPlayed = 0;
    this.totalErrors = 0;
    this.bestWpm = 0;
    this.averageWpm = 0;
    this.averageAccuracy = 0.0;
    this.topPlayers.clear();
    this.totalWpm = 0;
  }

  /**
   * Sets the current game state based on the game state notification.
   *
   * @param gameStateNotification object containing the state of the game
   */
  public void setCurrentGameState(GameStateNotification gameStateNotification) {
    this.gameStatus = gameStateNotification.getGameStatus();
  }

  /**
   * Gets the current game state of the game.
   *
   * @return the current game state
   */
  public String getCurrentGameState() {
    return gameStatus;
  }

  /**
   * Sets the new game text for the type racer game.
   *
   * @param textNotification object containing the text
   */
  public void setNewGameText(TextNotification textNotification) {
    this.text = textNotification.getText();
  }

  /**
   * Gets the new game text got the type racer game.
   *
   * @return the current game text
   */
  public String fetchNewGameText() {
    return text;
  }

  public void setAverageWpm() {
    this.averageWpm = gamesPlayed == 0 ? 0 : totalWpm / gamesPlayed;
  }

  public int getAverageWpm() {
    return averageWpm;
  }

  public void setTotalWpm () {
    this.totalWpm = totalWpm + wpm;
  }

  public int getTotalWpm() {
    return totalWpm;
  }

  public void updatePlayerState(PlayerStateNotification playerStateNotification) {
    this.playerId = playerStateNotification.getPlayerId();
    this.accuracy = playerStateNotification.getAccuracy();
    this.progress = playerStateNotification.getProgress();
    this.wpm = (int) playerStateNotification.getWpm();
  }

  public void setBestWpm() {
    this.bestWpm = Math.max(bestWpm, wpm);
  }

  public int getBestWpm() {
    return bestWpm;
  }

  public void setAverageAccuracy() {
    this.averageAccuracy = gamesPlayed == 0 ? 0 : totalAccuracy / gamesPlayed;
  }

  public double getAverageAccuracy() {
    return averageAccuracy;
  }

  public void setTotalAccuracy() {
    this.totalAccuracy = totalAccuracy + accuracy;
  }

  public double getTotalAccuracy() {
    return totalAccuracy;
  }

  public void onPlayerStatsReceived(Client client) {
    Platform.runLater(() -> {
      this.gamesPlayed = client.getGamesPlayed();
      this.averageWpm = client.getAverageWpm();
      this.bestWpm = client.getBestWpm();
      this.averageAccuracy = client.getAverageAccuracy();
    });
  }

  public List<String> getTopPlayers() {
    return topPlayers;
  }

  public void onTopPlayersReceived(List<PlayerStateNotification> topPlayers) {
    Platform.runLater(() -> {
      ObservableList<String> formattedPlayers = topPlayers.stream()
          .map(p -> p.getPlayerId() + " - WPM: " + p.getWpm())
          .collect(Collectors.toCollection(FXCollections::observableArrayList));
      this.topPlayers.clear();
      this.topPlayers.addAll(formattedPlayers);
    });
  }

  /** Updates the player statistics properties. */
  public void updatePlayerStats() {
    setTotalWpm();
    setTotalAccuracy();
    incrementGamesPlayed();
    setAverageWpm();
    setBestWpm();
    setAverageAccuracy();
  }
}