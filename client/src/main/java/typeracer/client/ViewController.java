package typeracer.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import typeracer.client.view.GameResultsUi;
import typeracer.client.view.GameUi;
import typeracer.client.view.InitialPromptUi;
import typeracer.client.view.LobbyUi;
import typeracer.client.view.MainMenuUi;
import typeracer.client.view.PlayerStatsUi;
import typeracer.client.view.ProfileSettingsUi;

/** Manages the transition between different scenes and states in the TypeRacer game application. */
public class ViewController {

  /** The width of the application window. */
  private static final int WINDOW_WIDTH = 800;

  /** The height of the application window. */
  private static final int WINDOW_HEIGHT = 600;

  /** The width of the lobby window. */
  private static final int LOBBY_WIDTH = 600;

  /** The height of the lobby window. */
  private static final int LOBBY_HEIGHT = 400;

  /** A map of view names to their corresponding scenes. */
  private static Map<ViewName, Scene> views;

  /** The primary stage of the application. */
  private static Stage stage;

  /** The client handling the backend communication. */
  private static Client client;

  /** The ID of the current player. */
  private int currentPlayerId;

  /** The username of the current user. */
  private String username;

  /** A map of player IDs to their WPM properties. */
  private Map<Integer, DoubleProperty> playerWpms = new HashMap<>();

  /** A map of player IDs to their accuracy properties. */
  private Map<Integer, DoubleProperty> playerAccuracies = new HashMap<>();

  /** A map of player IDs to their progress properties. */
  private Map<Integer, DoubleProperty> playerProgresses = new HashMap<>();

  /** A map of player IDs to their error count properties. */
  private Map<Integer, IntegerProperty> playerErrors = new HashMap<>();

  /** A list property of the top players' usernames. */
  private ListProperty<String> topPlayers =
      new SimpleListProperty<>(FXCollections.observableArrayList());

  /** The game text property. */
  private static StringProperty gameText = new SimpleStringProperty();

  /** An observable list of player usernames. */
  private ObservableList<String> playerUsernames = FXCollections.observableArrayList();

  /**
   * Connects to the server with the given IP address and port number.
   *
   * @param ip The IP address of the server.
   * @param port The port number of the server.
   * @throws IOException If an I/O error occurs when attempting to connect to the server.
   */
  public void connectToServer(String ip, int port) throws IOException {
    // client.connect(ip, port);
    System.out.println("Connected to server at " + ip + ":" + port);
  }

  /**
   * Constructs a ViewController with a given stage and client. Initializes the view mappings and
   * sets up the initial views.
   *
   * @param stage The primary stage of the application.
   * @param client The client handling the backend communication.
   */
  public ViewController(Stage stage, Client client) {
    this.stage = stage;
    views = new HashMap<>();
    this.client = client;
    initializeViews();
    initializeTopPlayers();
  }

  /** Sends the current username to the server using the client. */
  public void sendUsernameToServer() {
    // client.sendUsername(username);
  }

  /** Enum representing the different views available in the TypeRacer game application. */
  public enum ViewName {
    /** The initial prompt view. */
    INITIAL_PROMPT,

    /** The main menu view. */
    MAIN_MENU,

    /** The game view. */
    GAME,

    /** The player statistics view. */
    STATS,

    /** The profile settings view. */
    PROFILE_SETTINGS,

    /** The game results view. */
    GAME_RESULTS,

    /** The lobby view. */
    LOBBY
  }

  /**
   * Sets the username for the current session.
   *
   * @param username The username of the player.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns the current username.
   *
   * @return The username of the player.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the current player's ID.
   *
   * @return The ID of the current player.
   */
  public int getCurrentPlayerId() {
    return currentPlayerId;
  }

  /**
   * Returns an observable list of player usernames.
   *
   * @return An observable list containing the usernames of the players.
   */
  public ObservableList<String> getPlayerUsernames() {
    return playerUsernames;
  }

  /** Initializes the different views used in the application. */
  private void initializeViews() {
    views.put(
        ViewName.INITIAL_PROMPT,
        new Scene(new InitialPromptUi(this, stage), WINDOW_WIDTH, WINDOW_HEIGHT));
    views.put(ViewName.MAIN_MENU, new Scene(new MainMenuUi(this), WINDOW_WIDTH, WINDOW_HEIGHT));
    views.put(ViewName.GAME, new Scene(new GameUi(this), WINDOW_WIDTH, WINDOW_HEIGHT));
    views.put(ViewName.STATS, new Scene(new PlayerStatsUi(this), WINDOW_WIDTH, WINDOW_HEIGHT));
    views.put(
        ViewName.PROFILE_SETTINGS,
        new Scene(new ProfileSettingsUi(this), WINDOW_WIDTH, WINDOW_HEIGHT));
    views.put(
        ViewName.GAME_RESULTS, new Scene(new GameResultsUi(this), WINDOW_WIDTH, WINDOW_HEIGHT));
    views.put(ViewName.LOBBY, new Scene(new LobbyUi(this), LOBBY_WIDTH, LOBBY_HEIGHT));
  }

  /** Starts a new game by fetching the game text and updating the UI accordingly. */
  public static void startNewGame() {
    String gameText = client.fetchNewGameText();
    updateGameText(gameText);
    handleResetStats();
    switchToGameUi();
  }

  /** Ends the current game, updates stats, and switches the UI to display game results. */
  public void endGame() {
    switchToGameResultUi();
  }

  public StringProperty gameTextProperty() {
    return gameText;
  }

  /**
   * Updates the game text with the specified new text.
   *
   * @param newText The new game text to set.
   */
  public static void updateGameText(String newText) {
    gameText.set(newText);
  }

  /**
   * Returns the WPM property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The WPM property for the player.
   */
  public DoubleProperty getPlayerWpmProperty(int playerId) {
    return playerWpms.computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
  }

  /**
   * Returns the accuracy property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The accuracy property for the player.
   */
  public DoubleProperty getPlayerAccuracyProperty(int playerId) {
    return playerAccuracies.computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
  }

  /**
   * Returns the progress property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The progress property for the player.
   */
  public DoubleProperty getPlayerProgressProperty(int playerId) {
    return playerProgresses.computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
  }

  /**
   * Sets the WPM value for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @param wpm The WPM value to set.
   */
  public void setPlayerWpm(int playerId, double wpm) {
    getPlayerWpmProperty(playerId).set(wpm);
  }

  /**
   * Sets the accuracy value for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @param accuracy The accuracy value to set.
   */
  public void setPlayerAccuracy(int playerId, double accuracy) {
    getPlayerAccuracyProperty(playerId).set(accuracy);
  }

  /**
   * Sets the progress value for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @param progress The progress value to set.
   */
  public void setPlayerProgress(int playerId, double progress) {
    getPlayerProgressProperty(playerId).set(progress);
  }

  /**
   * Updates the WPM value for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @param wpm The new WPM value to set.
   */
  public void updatePlayerWpm(int playerId, double wpm) {
    Platform.runLater(
        () -> {
          playerWpms.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(wpm);
        });
  }

  /**
   * Updates the accuracy value for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @param accuracy The new accuracy value to set.
   */
  public void updatePlayerAccuracy(int playerId, double accuracy) {
    Platform.runLater(
        () -> {
          playerAccuracies.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(accuracy);
        });
  }

  /**
   * Updates the progress value for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @param progress The new progress value to set.
   */
  public void updatePlayerProgress(int playerId, double progress) {
    Platform.runLater(
        () -> {
          playerProgresses.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(progress);
        });
  }

  /**
   * Returns the error count property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The error count property for the player.
   */
  public IntegerProperty getPlayerErrorsProperty(int playerId) {
    return playerErrors.computeIfAbsent(playerId, k -> new SimpleIntegerProperty());
  }

  /**
   * Updates the error count for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @param errors The new error count to set.
   */
  public void updatePlayerErrors(int playerId, int errors) {
    Platform.runLater(
        () -> {
          playerErrors.computeIfAbsent(playerId, k -> new SimpleIntegerProperty()).set(errors);
        });
  }

  /** Initializes the list of top players. */
  private void initializeTopPlayers() {
    List<String> players = client.getTopPlayers();
    topPlayers.set(FXCollections.observableArrayList(players));
  }

  public ListProperty<String> topPlayersProperty() {
    return topPlayers;
  }

  /**
   * Changes the current scene to the specified view.
   *
   * @param viewName The name of the view to display.
   */
  public static void showView(ViewName viewName) {
    Scene scene = views.get(viewName);
    if (scene != null) {
      stage.setScene(scene);
      stage.show();
      if (scene.getRoot() instanceof LobbyUi) {
        ((LobbyUi) scene.getRoot()).onViewShown();
      } else if (scene.getRoot() instanceof GameUi) {
        ((GameUi) scene.getRoot()).onViewShown();
      }
    } else {
      System.err.println("View not found: " + viewName);
    }
  }

  /**
   * Adds a scene to the views map with a specified name.
   *
   * @param name The name to assign to the scene.
   * @param scene The scene to add.
   */
  public void addScene(ViewName name, Scene scene) {
    views.put(name, scene);
  }

  /** Displays the lobby view. */
  public void startGame() {
    showView(ViewName.LOBBY);
  }

  /** Displays the statistics view. */
  public void viewStats() {
    showView(ViewName.STATS);
  }

  /** Displays the profile settings view. */
  public void editProfile() {
    showView(ViewName.PROFILE_SETTINGS);
  }

  /** Switches the current scene to the main menu. */
  public static void switchToMainMenu() {
    showView(ViewName.MAIN_MENU);
  }

  /** Switches the current scene to the lobby UI. */
  public static void switchToLobbyUi() {
    showView(ViewName.LOBBY);
    LobbyUi lobbyUi = (LobbyUi) views.get(ViewName.LOBBY).getRoot();
    if (lobbyUi != null) {
      lobbyUi.onViewShown();
    }
  }

  /** Switches the current scene to the game UI. */
  public static void switchToGameUi() {
    showView(ViewName.GAME);
    GameUi gameUi = (GameUi) views.get(ViewName.GAME).getRoot();
    if (gameUi != null) {
      gameUi.onViewShown();
    }
  }

  /** Switches the current scene to the game results UI. */
  public static void switchToGameResultUi() {
    showView(ViewName.GAME_RESULTS);
  }

  /**
   * Saves user settings and switches back to the main menu.
   *
   * @param username The username to save.
   * @param wpmGoal The words per minute goal.
   * @param favoriteText The favorite text of the user.
   */
  public void saveUserSettings(String username, int wpmGoal, String favoriteText) {
    client.saveSettings(username, wpmGoal, favoriteText);
    showView(ViewName.MAIN_MENU);
  }

  /** Cancels any changes made in the settings and returns to the main menu. */
  public void cancelSettings() {
    showView(ViewName.MAIN_MENU);
  }

  /** Resets the user's game statistics. */
  public static void handleResetStats() {
    client.resetStats();
    PlayerStatsUi statsUi = getPlayerStatsUi();
    statsUi.clearDisplayedStats();
  }

  /**
   * Gets the PlayerStatsUi from the views map.
   *
   * @return PlayerStatsUi instance if available.
   */
  private static PlayerStatsUi getPlayerStatsUi() {
    return (PlayerStatsUi) views.get(ViewName.STATS).getRoot();
  }
}
