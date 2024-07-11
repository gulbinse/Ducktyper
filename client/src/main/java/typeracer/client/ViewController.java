package typeracer.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import typeracer.client.view.GameResultsUi;
import typeracer.client.view.GameUi;
import typeracer.client.view.InitialPromptUi;
import typeracer.client.view.LobbyUi;
import typeracer.client.view.MainMenuUi;
import typeracer.client.view.PlayerStatsUi;
import typeracer.client.view.ProfileSettingsUi;
import typeracer.communication.messages.client.CreateSessionRequest;
import typeracer.communication.messages.client.JoinSessionRequest;
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

    /** The lobby view. */
    LOBBY
  }

  private static final String STYLESHEET_PATH = "/styles.css";
  private static final int DEFAULT_WINDOW_WIDTH = 800;
  private static final int DEFAULT_WINDOW_HEIGHT = 650;
  private static Map<SceneName, Scene> scenes;
  private Stage primaryStage;
  private Client client;
  private ClientSideSessionData playerData = new ClientSideSessionData();
  private LobbyUi lobbyUi;

  /**
   * Constructs a ViewController with a given stage and client. Initializes the view mappings and
   * sets up the initial views.
   */
  public ViewController() {
    scenes = new HashMap<>();
    this.client = new Client(this);
    addDummyPlayer();
  }

  /**
   * Starts the application by setting up the primary stage.
   *
   * @param primaryStage The primary stage for this application.
   */
  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Ducktyper");

    addScene(SceneName.INITIAL_PROMPT, new InitialPromptUi(this, primaryStage));
    addScene(SceneName.MAIN_MENU, new MainMenuUi(this));
    addScene(SceneName.GAME, new GameUi(this));
    addScene(SceneName.STATS, new PlayerStatsUi(this));
    addScene(SceneName.PROFILE_SETTINGS, new ProfileSettingsUi(this));
    addScene(SceneName.GAME_RESULTS, new GameResultsUi(this));
    addScene(SceneName.LOBBY, new LobbyUi(this));

    Platform.runLater(() -> showScene(SceneName.INITIAL_PROMPT));
    primaryStage.setResizable(true);
    primaryStage.setOnCloseRequest(event -> System.exit(0));
    primaryStage.show();
  }

  /** Helper method to add views to the map. */
  private void addScene(SceneName sceneName, Parent ui) {
    Scene scene = new Scene(ui, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    loadStylesheets(scene);
    scenes.put(sceneName, scene);
  }

  /**
   * Changes the current scene to the specified view.
   *
   * @param sceneName The name of the view to display.
   */
  public void showScene(SceneName sceneName) {
    Scene scene = scenes.get(sceneName);
    if (scene != null) {
      primaryStage.setScene(scene);
      primaryStage.show();
      if (scene.getRoot() instanceof LobbyUi) {
        ((LobbyUi) scene.getRoot()).onViewShown();
      } else if (scene.getRoot() instanceof GameUi) {
        ((GameUi) scene.getRoot()).onViewShown();
      }
    } else {
      System.err.println("View not found: " + sceneName);
    }
  }

  /** Switches the current scene to the main menu. */
  public void switchToMainMenu() {
    showScene(SceneName.MAIN_MENU);
  }

    /**
     * Switches the current scene to the lobby UI.
     */
    // Was ist der Unterschied zwischen der Methode und startGame(); ?
    public void switchToLobbyUi(String sessionId) {
        showScene(SceneName.LOBBY);
        LobbyUi lobbyUi = (LobbyUi) scenes.get(SceneName.LOBBY).getRoot();
        if (lobbyUi != null) {
          lobbyUi.setSessionId(sessionId);
          lobbyUi.onViewShown();
        }
    }

    /**
     * Switches the current scene to the game UI.
     */
    public void switchToGameUi() {
        showScene(SceneName.GAME);
        GameUi gameUi = (GameUi) scenes.get(SceneName.GAME).getRoot();
        if (gameUi != null) {
            gameUi.onViewShown();
        }
    }

    /**
     * Switches the current scene to the game results UI.
     */
    public void switchToGameResultUi() {
        showScene(SceneName.GAME_RESULTS);
    }

  /** Displays the statistics view. */
  public void viewStats() {
    showScene(SceneName.STATS);
  }

  /** Displays the profile settings view. */
  public void editProfile() {
    showScene(SceneName.PROFILE_SETTINGS);
  }

  /**
   * Saves user settings and switches back to the main menu.
   *
   * @param username The username to save.
   * @param wpmGoal The words per minute goal.
   * @param favoriteText The favorite text of the user.
   */
  public void saveUserSettings(String username, int wpmGoal, String favoriteText) {
    //        client.saveSettings(username, wpmGoal, favoriteText);
    showScene(SceneName.MAIN_MENU);
  }

  /** Cancels any changes made in the settings and returns to the main menu. */
  public void cancelSettings() {
    showScene(SceneName.MAIN_MENU);
  }

  /**
   * Loads the stylesheet for the given scene.
   *
   * @param scene The scene to which the stylesheet will be applied.
   */
  private void loadStylesheets(Scene scene) {
    try {
      scene.getStylesheets().add(STYLESHEET_PATH);
    } catch (Exception e) {
      System.err.println("Error loading stylesheets: " + e.getMessage());
    }
  }

  /**
   * Connects to the server with the given IP address and port number.
   *
   * @param ip The IP address of the server.
   * @param port The port number of the server.
   * @throws IOException If an I/O error occurs when attempting to connect to the server.
   */
  public void connectToServer(String ip, int port, String username) throws IOException {
    // TODO: add Logic, that makes Client connect to Server and transfer username
    client.connect(ip, port, username);
    System.out.println("Connected to server at " + ip + ":" + port);
    playerData.setUsername(username);
  }

  public void requestNewGameSession() {
    client.sendMessage(new CreateSessionRequest());
    showScene(SceneName.LOBBY);
  }

  public void joinExistingSession(int sessionId) {
    if (sessionId == 0) {
      System.out.println("Session ID cannot be empty");
      return;
    } else {
      client.sendMessage(new JoinSessionRequest(sessionId));
    }
    showScene(SceneName.LOBBY);
  }

  //TODO: Not sure if this method should be called in Client
  public void updateSessionId(int newSessionId) {
    Platform.runLater(() -> {
      playerData.setId(newSessionId);
      lobbyUi.setSessionId(newSessionId);
    });
  }

  public void handleCharacterTyped(char character) {
    System.out.println("Character typed: " + character);
    // TODO: add Logic, that makes Client send a CharacterRequest to Server
  }

  public void leaveLobbyOrGame() {
    System.out.println("Leaving lobby");
    // TODO: add Logic, that makes Client send a LeaveSessionRequest to Server
    showScene(SceneName.MAIN_MENU);
  }

  /** Starts a new game by fetching the game text and updating the UI accordingly. */
  // TODO: This method should be called by Client on receiving a GameStateNotification with
  // GameStatus == Running
  public void startNewGame() {
    boolean isReady = false;
    client.sendMessage(new ReadyRequest(isReady));
    playerData.getGameText();
    showScene(SceneName.GAME);
    GameUi gameUi = (GameUi) scenes.get(SceneName.GAME).getRoot();
    if (gameUi != null) {
      gameUi.onViewShown();
  }

  /**
   * Adds a player to the game on client side
   *
   * @param playerId of joined player
   * @param playerName of joined player
   */
  // TODO: this method should be called by Client on receiving a PlayerJoinedNotification
  public void addPlayerToGame(int playerId, String playerName) {
    playerData.addPlayer(playerId, playerName);
  }

  // TODO: this method should be called by Client on receiving a PlayerLeftNotification
  public void removePlayerFromGame(int playerId) {
    playerData.removePlayer(playerId);
  }

  /** Ends the current game, updates stats, and switches the UI to display game results. */
  // TODO: This method should be called by view on receiving a GameStateNotification with GameStatus
  // == Finished
  public void endGame() {
    showScene(SceneName.GAME_RESULTS);
  }

  // TODO: This method should be called by view on receiving a CharacterResponse
  public void handleCharacterAnswer(boolean isCorrect) {}

  /**
   * Updates the game text with the specified new text.
   *
   * @param newText The new game text to set.
   */
  // TODO: This method should be called by view on receiving a TextNotification. We have to make
  // sure, that we always have a text when the game starts
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
  // TODO: This method should be called by Client, when it receives a PlayerStateNotification
  public void updatePlayerStateInformation(
      int playerId, double playerWpm, double playerAccuracy, double playerProgress) {
    playerData.setPlayerWpms(playerId, playerWpm);
    playerData.setPlayerAccuracies(playerId, playerAccuracy);
    playerData.setPlayerProgresses(playerId, playerProgress);
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
    return playerData.getId();
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
    return FXCollections.observableArrayList(playerData.getPlayerNameById().values());
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

  // for testing purpose only
  private void addDummyPlayer() {
    addPlayerToGame(1, "dummyPlayer");
  }
}
