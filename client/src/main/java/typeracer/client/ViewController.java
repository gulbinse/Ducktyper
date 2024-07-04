package typeracer.client;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import typeracer.game.GameState;


/** Manages the transition between different scenes and states in the TypeRacer game application. */
public class ViewController {
  /** A map of view names to their corresponding scenes. */
  private static Map<String, Scene> views;

  /** The primary stage of the application. */
  private static Stage stage;

  /** The client handling the backend communication. */
  private static Client client;

  /** The username of the current user. */
  private String username;
  private int sessionId;

  /** A scheduled executor service for periodic updates. */
  private static ScheduledExecutorService executorService;

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

  /** Initializes the different views used in the application. */
  private void initializeViews() {
    views.put("initial prompt", new Scene(new InitialPromptUi(this, stage), 800, 600));
    views.put("main menu", new Scene(new MainMenuUi(this), 800, 600));
    views.put("game", new Scene(new GameUi(this), 800, 600));
    views.put("stats", new Scene(new PlayerStatsUi(this), 800, 600));
    views.put("profile settings", new Scene(new ProfileSettingsUi(this), 800, 600));
    views.put("game results", new Scene(new GameResultsUi(this), 800, 600));
    views.put("lobby", new Scene(new LobbyUi(this), 600, 400));
  }

  /** Starts a new game by fetching the game text and updating the UI accordingly. */
  public static void startNewGame() {
    String gameText = client.fetchNewGameText();
    updateGameText(gameText);
    handleResetStats();
    switchToGameUi();
    startPeriodicUpdates();
  }

  /** Schedules periodic updates at a fixed rate to handle the game state. */
  private static void startPeriodicUpdates() {
    stopPeriodicUpdates();
    executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.scheduleAtFixedRate(
        () -> {
          GameState stats = client.getCurrentGameState();
          Platform.runLater(() -> onPeriodicUpdate(stats));
        },
        0,
        1,
        TimeUnit.SECONDS);
  }

  /** Stops any ongoing periodic updates. */
  private static void stopPeriodicUpdates() {
    if (executorService != null && !executorService.isShutdown()) {
      try {
        executorService.shutdownNow();
        if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
          System.err.println("Executor did not terminate in the specified time.");
          List<Runnable> droppedTasks = executorService.shutdownNow(); // Try again to shutdown
          System.err.println(
              "Executor was abruptly shut down. "
                  + droppedTasks.size()
                  + " tasks will not be executed.");
        }
      } catch (InterruptedException e) {
        System.err.println("Error stopping the executor service: " + e.getMessage());
        Thread.currentThread().interrupt();
      }
    }
  }

  /** Ends the current game, updates stats, and switches the UI to display game results. */
  public void endGame() {
    try {
      stopPeriodicUpdates();
      int gamesPlayed = client.getGamesPlayed();
      double averageWpm = client.getAverageWpm();
      int totalErrors = client.getTotalErrors();
      double bestWpm = client.getBestWpm();
      double averageAccuracy = client.getAverageAccuracy();

      updatePlayerStats(gamesPlayed, averageWpm, totalErrors, bestWpm, averageAccuracy);
      switchToGameResultUi();
    } finally {
      if (executorService != null && !executorService.isShutdown()) {
        executorService.shutdownNow();
      }
    }
  }

  /**
   * Updates the statistics displayed in the player stats UI.
   *
   * @param gamesPlayed Total number of games played.
   * @param averageWpm Average words per minute.
   * @param totalErrors Total number of errors made.
   * @param bestWpm Best words per minute achieved.
   * @param averageAccuracy Average accuracy percentage.
   */
  private void updatePlayerStats(
      int gamesPlayed, double averageWpm, int totalErrors, double bestWpm, double averageAccuracy) {
    PlayerStatsUi statsUi = (PlayerStatsUi) views.get("stats").getRoot();
    statsUi.updateStats(gamesPlayed, averageWpm, totalErrors, bestWpm, averageAccuracy);
  }

  /**
   * Updates the game text displayed in the game UI.
   *
   * @param text The new text for the game.
   */
  private static void updateGameText(String text) {
    GameUi gameUi = (GameUi) views.get("game").getRoot();
    gameUi.updateText(text);
  }

  /**
   * Updates the words per minute in the game UI.
   *
   * @param wpm The new words per minute to display.
   */
  public static void updateGameWpm(double wpm) {
    GameUi gameUi = (GameUi) views.get("game").getRoot();
    gameUi.updateWpm(wpm);
  }

  /**
   * Updates the number of errors displayed in the game UI.
   *
   * @param errors The new error count.
   */
  public static void updateGameErrors(int errors) {
    GameUi gameUi = (GameUi) views.get("game").getRoot();
    gameUi.updateErrors(errors);
  }

  /**
   * Updates the display of top players in the game UI.
   *
   * @param players Formatted string of top players.
   */
  public static void updateGameTopPlayers(String players) {
    GameUi gameUi = (GameUi) views.get("game").getRoot();
    gameUi.updateTopPlayers(players);
  }

  /**
   * Changes the current scene to the specified view.
   *
   * @param viewName The name of the view to display.
   */
  public static void showView(String viewName) {
    Scene scene = views.get(viewName.toLowerCase());
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
  public void addScene(String name, Scene scene) {
    views.put(name, scene);
  }

  /** Displays the lobby view. */
  public void startGame() {
    showView("lobby");
  }

  /** Displays the statistics view. */
  public void viewStats() {
    showView("stats");
  }

  /** Displays the profile settings view. */
  public void editProfile() {
    showView("profile settings");
  }

  /** Switches the current scene to the main menu. */
  public static void switchToMainMenu() {
    showView("main menu");
  }

  /** Switches the current scene to the lobby UI. */
  public static void switchToLobbyUi() {
    showView("lobby");
    LobbyUi lobbyUi = (LobbyUi) views.get("lobby").getRoot();
    if (lobbyUi != null) {
      lobbyUi.onViewShown();
    }
  }

  /** Switches the current scene to the game UI. */
  public static void switchToGameUi() {
    showView("game");
    GameUi gameUi = (GameUi) views.get("game").getRoot();
    if (gameUi != null) {
      gameUi.onViewShown();
    }
  }

  /** Switches the current scene to the game results UI. */
  public static void switchToGameResultUi() {
    showView("game results");
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
    showView("main menu");
  }

  /** Cancels any changes made in the settings and returns to the main menu. */
  public void cancelSettings() {
    showView("main menu");
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
    return (PlayerStatsUi) views.get("stats").getRoot();
  }

  /**
   * Performs periodic updates based on game state, updating WPM, errors, and top players.
   *
   * @param stats The current game state to use for updates.
   */
  public static void onPeriodicUpdate(GameState stats) {
    updateGameWpm(stats.getWpm());
    updateGameErrors(stats.getErrors());
    updateGameTopPlayers(getTopPlayersFormatted());
  }
  /**
   * Formats a list of top players into a displayable string.
   *
   * @return A formatted string representing the top players.
   */
  private static String getTopPlayersFormatted() {
    List<String> topPlayers = client.getTopPlayers();
    StringBuilder formattedPlayers = new StringBuilder("Top players: ");
    for (int i = 0; i < topPlayers.size(); i++) {
      formattedPlayers.append(i + 1).append(". ").append(topPlayers.get(i));
      if (i < topPlayers.size() - 1) {
        formattedPlayers.append(", ");
      }
    }
    return formattedPlayers.toString();
  }

  public void setSessionID (int sessionID) {
    this.sessionId = sessionID;
  }

  public int getSessionID(int sessionID) {
    return sessionID;
  }

  public void showReason (String reason) {
    System.out.println(reason);
  }
}