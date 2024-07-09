package typeracer.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
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
  private static final int WINDOW_WIDTH = 2200;

  /** The height of the application window. */
  private static final int WINDOW_HEIGHT = 2000;

  /** The width of the lobby window. */
  private static final int LOBBY_WIDTH = 800;

  /** The height of the lobby window. */
  private static final int LOBBY_HEIGHT = 600;

  /** A map of view names to their corresponding scenes. */
  private static Map<ViewName, Scene> views = new HashMap<>();

  /** The primary stage of the application. */
  private static Stage stage;

  /** The client handling the backend communication. */
  private static Client client;

  private GameUi gameUi;

  private ClientSideSessionData sessionData = new ClientSideSessionData();

  /**
   * Constructs a ViewController with a given stage and client. Initializes the view mappings and
   * sets up the initial views.
   *
   * @param stage The primary stage of the application.
   * @param client The client handling the backend communication.
   */
  public ViewController(Stage stage, Client client) {
    this.stage = stage;
    this.gameUi = new GameUi(this);
    views = new HashMap<>();
    this.client = client;
    initializeViews();
  }

  public static void setPlayerReady(boolean isReady) {
    if (client != null) {
      client.sendReadyState(isReady);
    } else {
      System.out.println("Client is not initialized.");
    }
  }

  /**
   * Connects to the server with the given IP address and port number.
   *
   * @param ip The IP address of the server.
   * @param port The port number of the server.
   * @throws IOException If an I/O error occurs when attempting to connect to the server.
   */
  public void connectToServer(String username, String ip, int port) throws IOException {
    // TODO: add Logic, that makes Client connect to Server and transfer username
    // client.sendUsername(username);
    // client.connect(ip, port);
    System.out.println("Connected to server at " + ip + ":" + port);
  }

  public void handleCharacterTyped(char character) {
    // TODO: add Logic, that makes Client send a CharacterRequest to Server
    if (client != null) {
      client.sendCharacter(character);
    } else {
      System.out.println("Error: Client is not initialized.");
    }
  }

  /**
   * Handles the response from the server about the character typed.
   *
   * @param isCorrect whether the character typed was correct
   */
  public void handleCharacterAnswer(boolean isCorrect) {
    // TODO: add Logic to display in GameUi whether the typed character was correct or not
    Platform.runLater(
        () -> { // Update the Game UI with feedback
          gameUi.displayTypingFeedback(isCorrect);
        });
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

  /** Ends the current game, updates stats, and switches the UI to display game results. */
  public void endGame() {
    switchToGameResultUi();
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
