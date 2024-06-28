package typeracer.client.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Entry point of the JavaFX application. */
public class Main extends Application {
  /** Default constructor for the Main class. Initializes a new instance of Main. */
  public Main() {
    // Default constructor
  }

  /**
   * The main() method is ignored in correctly deployed JavaFX application. main() serves only as
   * fallback in case the application can not be launched through deployment artifacts, e.g., in
   * IDEs with limited FX support. NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Starts the primary stage of the application, sets up the user interface.
   *
   * @param primaryStage the main stage for this application, onto which the application scene can
   *     be set.
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Typeracer Game");

    InitialPromptUi initialPromptUi = new InitialPromptUi(primaryStage);
    Scene scene = new Scene(initialPromptUi, 400, 450);
    primaryStage.setScene(scene);

    scene.setFill(StyleManager.BACKGROUND_COLOR);
    primaryStage.setResizable(true);
    primaryStage.setOnCloseRequest(event -> System.exit(0));
    primaryStage.show();
  }
}
