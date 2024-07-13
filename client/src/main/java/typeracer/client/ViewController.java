package typeracer.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import typeracer.client.view.GameResultsUi;
import typeracer.client.view.GameUi;
import typeracer.client.view.InitialPromptUi;
import typeracer.client.view.MainMenuUi;
import typeracer.client.view.PlayerStatsUi;
import typeracer.client.view.ProfileSettingsUi;
import typeracer.client.view.SessionUi;
import typeracer.communication.messages.client.*;

/** Manages the transition between different scenes and states in the TypeRacer game application. */
public class ViewController extends Application {

  /** Enum representing the different views available in the TypeRacer game application. */
  public enum SceneName {
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

    /** The session view. */
    SESSION
  }

  private static final String STYLESHEET_PATH = "/styles.css";
  private static final int DEFAULT_WINDOW_WIDTH = 1920 / 2 - 200;
  private static final int DEFAULT_WINDOW_HEIGHT = 1080 / 2 + 30;

  private static final int MINIMUM_WINDOW_WIDTH = 400;
  private static final int MINIMUM_WINDOW_HEIGHT = 600;

  private final Map<SceneName, Scene> scenes;

  private Stage primaryStage;
  private SceneName currentScene;
  private final Client client;
  private final ClientSideSessionData playerData = new ClientSideSessionData();

  /**
   * Constructs a ViewController with a given stage and client. Initializes the view mappings and
   * sets up the initial views.
   */
  public ViewController() {
    scenes = new HashMap<>();
    this.client = new Client(this);
  }

  /**
   * Starts the application by setting up the primary stage.
   *
   * @param primaryStage The primary stage for this application.
   */
  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;

    primaryStage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
    primaryStage.setMinWidth(MINIMUM_WINDOW_WIDTH);

    primaryStage.setWidth(DEFAULT_WINDOW_WIDTH);
    primaryStage.setHeight(DEFAULT_WINDOW_HEIGHT);

    primaryStage.setTitle("Ducktyper");

    addScene(SceneName.INITIAL_PROMPT, new InitialPromptUi(this, primaryStage));
    addScene(SceneName.MAIN_MENU, new MainMenuUi(this));
    addScene(SceneName.GAME, new GameUi(this));
    addScene(SceneName.STATS, new PlayerStatsUi(this));
    addScene(SceneName.PROFILE_SETTINGS, new ProfileSettingsUi(this));
    addScene(SceneName.GAME_RESULTS, new GameResultsUi(this));
    addScene(SceneName.SESSION, new SessionUi(this));

    Platform.runLater(() -> showScene(SceneName.INITIAL_PROMPT));
    primaryStage.setResizable(true);
    primaryStage.setOnCloseRequest(event -> System.exit(0));
    primaryStage.show();
  }

  /** Helper method to add views to the map. */
  private void addScene(SceneName sceneName, Parent ui) {
    Scene scene = new Scene(ui, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    try {
      scene.getStylesheets().add(STYLESHEET_PATH);
    } catch (Exception e) {
      showAlert("Error loading stylesheets: " + e.getMessage());
    }
    scenes.put(sceneName, scene);
  }

  /**
   * Changes the current scene to the specified view.
   *
   * @param sceneName The name of the view to display.
   */
  public void showScene(SceneName sceneName) {
    Platform.runLater(
        () -> {
          Scene scene = scenes.get(sceneName);
          currentScene = sceneName;
          if (scene != null) {
            primaryStage.setScene(scene);
            primaryStage.show();
            if (scene.getRoot() instanceof SessionUi sessionUi) {
              sessionUi.onViewShown();
            } else if (scene.getRoot() instanceof GameUi gameUi) {
              gameUi.onViewShown();
            }
          } else {
            showAlert("View not found: " + sceneName);
          }
        });
  }

  public void setPlayerId(int playerId) {
    playerData.setPlayerId(playerId);
  }

  public void setSessionId(int sessionId) {
    Platform.runLater(
        () -> {
          playerData.setSessionId(sessionId);
          SessionUi sessionUi = (SessionUi) scenes.get(SceneName.SESSION).getRoot();
          sessionUi.onViewShown();
        });
  }

  public int getSessionId() {
    return playerData.getSessionId();
  }

  /**
   * Saves user settings and switches back to the main menu.
   *
   * @param username The username to save.
   * @param wpmGoal The words per minute goal.
   * @param favoriteText The favorite text of the user.
   */
  public void saveUserSettings(String username, int wpmGoal, String favoriteText) {
    // client.saveSettings(username, wpmGoal, favoriteText);
    showScene(SceneName.MAIN_MENU);
  }

  /** Cancels any changes made in the settings and returns to the main menu. */
  public void cancelSettings() {
    showScene(SceneName.MAIN_MENU);
  }

  /**
   * Connects to the server with the given IP address and port number.
   *
   * @param ip The IP address of the server.
   * @param port The port number of the server.
   * @throws IOException If an I/O error occurs when attempting to connect to the server.
   */
  public void connectToServer(String ip, int port, String username) throws IOException {
    client.connect(ip, port, username);
    System.out.println("Connected to server at " + ip + ":" + port);
    playerData.setUsername(username);
  }

  /**
   * Called when User tries to join a session by pressing button in GUI.
   *
   * @param sessionId of session the player wants to join
   */
  public void joinSession(int sessionId) {
    client.sendMessage(new JoinSessionRequest(sessionId));
    setSessionId(sessionId);
    System.out.println("Request to join session " + sessionId);
  }

  /** Called when User tries to create a session by pressing button in GUI. */
  public void createSession() {
    client.sendMessage(new CreateSessionRequest());
    System.out.println("Request to create session");
  }

  /** Requests to set the player ready. */
  public void setPlayerReady() {
    boolean isReady = !playerData.getPlayerReady().get(playerData.getPlayerId()).get();
    client.sendMessage(new ReadyRequest(isReady));
    System.out.println("Player wants to update his readyStatus to: " + isReady);
  }

  /**
   * Passes the User input from the GUI to the Client.
   *
   * @param character which the client typed
   */
  public void handleCharacterTyped(char character) {
    client.sendMessage(new CharacterRequest(character));
    System.out.println("Character typed: " + character);
  }

  /** Signals User intention to leave the game. */
  public void leaveSessionOrGame() {
    client.sendMessage(new LeaveSessionRequest());
    System.out.println("Leaving game");
  }

  /** Starts a new game by fetching the game text and updating the UI accordingly. */
  public void startNewGame() {
    Platform.runLater(
        () -> {
          GameUi gameUi = (GameUi) scenes.get(SceneName.GAME).getRoot();
          for (int playerId : playerData.getPlayerNamesById().keySet()) {
            gameUi.addPlayer(playerId);
          }
          showScene(SceneName.GAME);
        });

  }

  /**
   * Adds a player to the game on client side.
   *
   * @param playerId of joined player
   * @param playerName of joined player
   */
  public void updatePlayer(int playerId, String playerName, boolean ready) {
    boolean isNew = playerData.updatePlayer(playerId, playerName, ready);
    if (isNew) {
      Platform.runLater(
          () -> {
            SessionUi sessionUi = (SessionUi) scenes.get(SceneName.SESSION).getRoot();
            sessionUi.addPlayerLabel(playerId);
          });

    }
  }

  /**
   * Removes a player who left from the game GUI.
   *
   * @param playerId of the player who left
   */
  public void removePlayerFromSession(int playerId) {
    playerData.removePlayer(playerId);
    Platform.runLater(
        () -> {
          SessionUi sessionUi = (SessionUi) scenes.get(SceneName.SESSION).getRoot();
          sessionUi.removePlayerLabel(playerId);

          GameUi gameUi = (GameUi) scenes.get(SceneName.GAME).getRoot();
          gameUi.removePlayer(playerId);
        });
  }

  /** Ends the current game, updates stats, and switches the UI to display game results. */
  // == Finished
  public void leaveSession() {
    Platform.runLater(() -> {
      switch (currentScene) {
        case GAME -> {
          showScene(SceneName.GAME_RESULTS);
          GameResultsUi gameUi = (GameResultsUi) scenes.get(SceneName.GAME_RESULTS).getRoot();
          gameUi.onViewShown();
        }
        case SESSION -> showScene(SceneName.MAIN_MENU);
      }

    });
  }

  /**
   * Passes the correctness of a typed character to the GUI.
   *
   * @param isCorrect boolean if the typed character is correct
   */
  public void handleCharacterAnswer(boolean isCorrect) {
    GameUi gameUi = (GameUi) scenes.get(SceneName.GAME).getRoot();
    gameUi.updateDisplayText(isCorrect);
  }

  /**
   * Updates the game text with the specified new text.
   *
   * @param newText The new game text to set.
   */
  public void setGameText(String newText) {
    playerData.setGameText(newText);
  }

  /**
   * Updates the player Information of a specific player.
   *
   * @param playerId of the player
   * @param playerWpm of the player
   * @param playerAccuracy of the player
   * @param playerProgress of the player
   */
  public void updatePlayerStateInformation(
      int playerId, double playerWpm, double playerAccuracy, double playerProgress) {
    Platform.runLater(() -> {
      playerData.setPlayerWpms(playerId, playerWpm);
      playerData.setPlayerAccuracies(playerId, playerAccuracy);
      playerData.setPlayerProgresses(playerId, playerProgress);
    });
  }

  /**
   * Returns the current username.
   *
   * @return The username of the player.
   */
  public String getUsername() {
    return playerData.getUsername();
  }

  /**
   * Returns the current player's ID.
   *
   * @return The ID of the current player.
   */
  public int getPlayerId() {
    return playerData.getPlayerId();
  }

  /**
   * Returns the current game text.
   *
   * @return text to be copied in game.
   */
  public String getGameText() {
    return playerData.getGameText();
  }

  /**
   * Returns an observable list of player usernames.
   *
   * @return An observable list containing the usernames of the players.
   */
  public ObservableList<String> getPlayerUsernames() {
    return FXCollections.observableArrayList(playerData.getPlayerNamesById().values());
  }

  public String getUsernameById(int id) {
    return playerData.getPlayerNamesById().get(id);
  }

  public BooleanProperty getPlayerReadyProperty(int playerId) {
    return playerData.getPlayerReady().computeIfAbsent(playerId, k -> new SimpleBooleanProperty());
  }

  /**
   * Returns the WPM property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The WPM property for the player.
   */
  public DoubleProperty getPlayerWpmProperty(int playerId) {
    return playerData.getPlayerWpms().computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
  }

  /**
   * Returns the accuracy property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The accuracy property for the player.
   */
  public DoubleProperty getPlayerAccuracyProperty(int playerId) {
    return playerData
        .getPlayerAccuracies()
        .computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
  }

  /**
   * Returns the progress property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The progress property for the player.
   */
  public DoubleProperty getPlayerProgressProperty(int playerId) {
    return playerData
        .getPlayerProgresses()
        .computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
  }

  public ListProperty<String> getTopPlayersProperty() {
    return new SimpleListProperty<>(playerData.getTopPlayers());
  }

  /**
   * Gets the PlayerStatsUi from the views map.
   *
   * @return PlayerStatsUi instance if available.
   */
  private PlayerStatsUi getPlayerStatsUi() {
    return (PlayerStatsUi) scenes.get(SceneName.STATS).getRoot();
  }

  /**
   * Displays an error alert with the specified message.
   *
   * @param message The message to display in the alert.
   */
  public void showAlert(String message) {
    Platform.runLater(() -> {
      Alert alert = new Alert(Alert.AlertType.ERROR, message);
      alert.showAndWait();
    });

  }
}
