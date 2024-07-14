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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import typeracer.client.view.GameResultsUi;
import typeracer.client.view.GameUi;
import typeracer.client.view.InitialPromptUi;
import typeracer.client.view.MainMenuUi;
import typeracer.client.view.PlayerStatsUi;
import typeracer.client.view.ProfileSettingsUi;
import typeracer.client.view.SessionUi;
import typeracer.communication.messages.client.CharacterRequest;
import typeracer.communication.messages.client.CreateSessionRequest;
import typeracer.communication.messages.client.JoinSessionRequest;
import typeracer.communication.messages.client.LeaveSessionRequest;
import typeracer.communication.messages.client.ReadyRequest;

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

  private final Stage stage = new Stage();
  private SceneName currentScene;
  private final Client client;
  private ClientSideSessionData playerData = new ClientSideSessionData();
  private String username;
  private int playerId;

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

    stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
    stage.setMinWidth(MINIMUM_WINDOW_WIDTH);

    stage.setWidth(DEFAULT_WINDOW_WIDTH);
    stage.setHeight(DEFAULT_WINDOW_HEIGHT);

    stage.setTitle("Ducktyper");

    addAllScenes();

    Platform.runLater(() -> showScene(SceneName.INITIAL_PROMPT));
    stage.setResizable(true);
    stage.setOnCloseRequest(event -> System.exit(0));
    stage.show();
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
            stage.setScene(scene);
            stage.show();
            if (scene.getRoot() instanceof SessionUi sessionUi) {
              sessionUi.onViewShown();
            } else if (scene.getRoot() instanceof GameUi gameUi) {
              gameUi.onViewShown();
            } else if (scene.getRoot() instanceof MainMenuUi) {
              scenes.clear();
              addAllScenes();
              playerData = new ClientSideSessionData();
            }
          } else {
            showAlert("Scene not found: " + sceneName);
          }
        });
  }

  /**
   * Adds all the scenes to the application.
   */
  private void addAllScenes() {
    addScene(SceneName.INITIAL_PROMPT, new InitialPromptUi(this));
    addScene(SceneName.MAIN_MENU, new MainMenuUi(this));
    addScene(SceneName.GAME, new GameUi(this));
    addScene(SceneName.STATS, new PlayerStatsUi(this));
    addScene(SceneName.PROFILE_SETTINGS, new ProfileSettingsUi(this));
    addScene(SceneName.GAME_RESULTS, new GameResultsUi(this));
    addScene(SceneName.SESSION, new SessionUi(this));
  }

  /**
   * Sets the player ID.
   *
   * @param playerId the player's unique identifier.
   */
  public void setPlayerId(int playerId) {
    this.playerId = playerId;
  }

  /**
   * Sets the session ID and updates the session UI.
   *
   * @param sessionId the session's unique identifier.
   */
  public void setSessionId(int sessionId) {
    Platform.runLater(
        () -> {
          playerData.setSessionId(sessionId);
          SessionUi sessionUi = (SessionUi) scenes.get(SceneName.SESSION).getRoot();
          sessionUi.onViewShown();
        });
  }

  /**
   * Retrieves the current session ID.
   *
   * @return the session's unique identifier.
   */
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
   * Connects to the server using the specified IP address, port, and username.
   * This method establishes a connection to the server and sets the username for the client.
   *
   * @param ip the IP address of the server.
   * @param port the port number of the server.
   * @param username the username to be used for the connection.
   * @throws IOException if an I/O error occurs when attempting to connect.
   */
  public void connectToServer(String ip, int port, String username) throws IOException {
    client.connect(ip, port, username);
    System.out.println("Connected to server at " + ip + ":" + port);
    this.username = username;
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
    boolean isReady = !playerData.getPlayerReadyProperty(playerId).get();
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
   * Updates the player's information and adds a player label if the player is new.
   * This method updates the player's name and ready status. If the player is new, it schedules
   * the addition of the player's label to the session UI on the JavaFX application thread.
   *
   * @param playerId the ID of the player to update.
   * @param playerName the name of the player.
   * @param ready the ready status of the player.
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
    Platform.runLater(
        () -> {
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

  /** Sets the icon image of the Stage to a typewriter image. */
  public void setIconImage() {
    Image img = new Image(getClass().getResourceAsStream("/images/duck.png"));
    stage.getIcons().add(img);
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
    Platform.runLater(
        () -> {
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
    return username;
  }

  /**
   * Returns the current player's ID.
   *
   * @return The ID of the current player.
   */
  public int getPlayerId() {
    return playerId;
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

  /**
   * Retrieves the username associated with a specific player ID.
   *
   * @param id the player's ID.
   * @return the username, or null if no player is found with the provided ID.
   */
  public String getUsernameById(int id) {
    return playerData.getPlayerNamesById().get(id);
  }

  /**
   * Retrieves or creates a property to track whether a player is ready.
   *
   * @param playerId the ID of the player.
   * @return a BooleanProperty reflecting the player's ready status.
   */
  public BooleanProperty getPlayerReadyProperty(int playerId) {
    return playerData.getPlayerReadyProperty(playerId);
  }

  /**
   * Returns the WPM property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The WPM property for the player.
   */
  public DoubleProperty getPlayerWpmProperty(int playerId) {
    return playerData.getPlayerWpmProperty(playerId);
  }

  /**
   * Returns the accuracy property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The accuracy property for the player.
   */
  public DoubleProperty getPlayerAccuracyProperty(int playerId) {
    return playerData.getPlayerAccuracyProperty(playerId);
  }

  /**
   * Gets the progress property of the player with the specified ID.
   * If the player's progress property does not exist, it is created and initialized to 0.0.
   *
   * @param playerId the ID of the player.
   * @return the DoubleProperty representing the player's progress.
   */
  public DoubleProperty getPlayerProgressProperty(int playerId) {
    return playerData.getPlayerProgressProperty(playerId);
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
    Platform.runLater(
        () -> {
          Alert alert = new Alert(Alert.AlertType.ERROR, message);
          alert.showAndWait();
        });
  }
}
