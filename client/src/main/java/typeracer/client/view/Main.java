package typeracer.client.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import typeracer.client.Client;
import typeracer.client.ViewController;

/**
 * The Main class serves as the entry point for the Ducktyper application. It extends the JavaFX
 * Application class and overrides the start method to set up the primary stage and initialize the
 * view controller.
 */
public class Main extends Application {
  /** The width of the application window. */
  private static final int WIDTH = 800;

  /** The height of the application window. */
  private static final int HEIGHT = 650;

  /** The path to the application's stylesheet. */
  private static final String STYLESHEET_PATH = "/styles.css";

  /** The controller managing views and handling interactions. */
  private ViewController viewController;

  /** Constructs a new Main instance. */
  public Main() {
    // Default constructor
  }

  /**
   * The main entry point for the application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Starts the application by setting up the primary stage and initializing the view controller.
   *
   * @param primaryStage The primary stage for this application.
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Ducktyper");

    Client client = new Client();
    viewController = new ViewController(primaryStage, client);

    initializeScenes(viewController, primaryStage);

    Platform.runLater(() -> viewController.showView(ViewController.ViewName.INITIAL_PROMPT));
    primaryStage.setResizable(true);
    primaryStage.setOnCloseRequest(event -> System.exit(0));
    primaryStage.show();
  }

  /**
   * Initializes the scenes for different views in the application.
   *
   * @param viewController The controller to manage views and handle interactions.
   * @param primaryStage The primary stage for this application.
   */
  private void initializeScenes(ViewController viewController, Stage primaryStage) {
    InitialPromptUi initialPromptUi = new InitialPromptUi(viewController, primaryStage);
    Scene initialScene = new Scene(initialPromptUi, WIDTH, HEIGHT);
    loadStylesheets(initialScene);
    viewController.addScene(ViewController.ViewName.INITIAL_PROMPT, initialScene);

    MainMenuUi mainMenuUi = new MainMenuUi(viewController);
    Scene mainMenuScene = new Scene(mainMenuUi, WIDTH, HEIGHT);
    loadStylesheets(mainMenuScene);
    viewController.addScene(ViewController.ViewName.MAIN_MENU, mainMenuScene);

    GameUi gameUi = new GameUi(viewController);
    Scene gameScene = new Scene(gameUi, WIDTH, HEIGHT);
    loadStylesheets(gameScene);
    viewController.addScene(ViewController.ViewName.GAME, gameScene);

    PlayerStatsUi playerStatsUi = new PlayerStatsUi(viewController);
    Scene statsScene = new Scene(playerStatsUi, WIDTH, HEIGHT);
    loadStylesheets(statsScene);
    viewController.addScene(ViewController.ViewName.STATS, statsScene);

    ProfileSettingsUi profileSettingsUi = new ProfileSettingsUi(viewController);
    Scene profileSettingsScene = new Scene(profileSettingsUi, WIDTH, HEIGHT);
    loadStylesheets(profileSettingsScene);
    viewController.addScene(ViewController.ViewName.PROFILE_SETTINGS, profileSettingsScene);

    GameResultsUi gameResultsUi = new GameResultsUi(viewController);
    Scene gameResultsScene = new Scene(gameResultsUi, WIDTH, HEIGHT);
    loadStylesheets(gameResultsScene);
    viewController.addScene(ViewController.ViewName.GAME_RESULTS, gameResultsScene);
  }

  /**
   * Loads the stylesheet for the given scene.
   *
   * @param scene The scene to which the stylesheet will be applied.
   */
  private void loadStylesheets(Scene scene) {
    try {
      scene.getStylesheets().add(getClass().getResource(STYLESHEET_PATH).toExternalForm());
    } catch (Exception e) {
      System.err.println("Error loading stylesheets: " + e.getMessage());
    }
  }
}
