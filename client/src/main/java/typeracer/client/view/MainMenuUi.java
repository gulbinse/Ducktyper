package typeracer.client.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import typeracer.client.ViewController;

import java.util.Objects;

/**
 * Represents the main menu user interface for the TypeRacer game. This class sets up the GUI
 * elements that allow the user to navigate to different parts of the game, including starting a new
 * game, accessing profile settings, viewing stats, and exiting the game.
 */
public class MainMenuUi extends VBox {

  private ViewController viewController;

  /** Button to start a new game. */
  private Button startGameButton;

  /** Button to view player statistics. */
  private Button statsButton;

  /** Button to exit the game. */
  private Button exitButton;

  private Button joinSessionButton;

  private TextField sessionIdField;

  private MainMenuUi(ViewController viewController) {
    this.viewController = viewController;
  }

  /**
   * Creates a new MainMenuUi and initializes its user interface.
   *
   * @param viewController The controller to manage views and handle interactions.
   * @return a new instance of MainMenuUi with its UI initialized.
   */
  public static MainMenuUi create(ViewController viewController) {
    MainMenuUi mainMenuUi = new MainMenuUi(viewController);
    mainMenuUi.initializeUi();
    return mainMenuUi;
  }

  /**
   * Initializes the user interface components for the main menu. Sets up the layout, styling, and
   * button actions.
   */
  private void initializeUi() {
    Background background = new Background(new BackgroundImage(
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/"
                    + "Menu_screen_noText.png"))),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false,
                    false, true)));
    this.setBackground(background);
    this.setAlignment(Pos.CENTER);
    this.setSpacing(15);

    VBox titlePanel = createTitlePanel();
    this.getChildren().add(titlePanel);

    Region spacer = new Region();
    spacer.setPrefHeight(30);
    this.getChildren().add(spacer);

    Image newGameButtonGraphic = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/startGameButton.png")));
    Image joinSessionButtonGraphic = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/joinGameButton.png")));
    Image statsButtonGraphic = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/statsButton.png")));
    Image exitButtonGraphic = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/leaveGameButton.png")));

    ImageView startGameButton = StyleManager.createMainMenueButton(newGameButtonGraphic);
    ImageView statsButton = StyleManager.createMainMenueButton(statsButtonGraphic);
    ImageView exitButton = StyleManager.createMainMenueButton(exitButtonGraphic);
    ImageView joinSessionButton = StyleManager.createMainMenueButton(joinSessionButtonGraphic);

    startGameButton.setOnMouseClicked(event -> viewController.createSession());

    sessionIdField = new TextField();
    sessionIdField.setPromptText("Enter Session ID");
    sessionIdField.setMaxWidth(200);
    sessionIdField.setStyle("-fx-alignment: center;");
    sessionIdField.getStyleClass().add("startScreen-input-field");

    joinSessionButton.setOnMouseClicked(
        event -> {
          try {
            viewController.joinSession(Integer.parseInt(sessionIdField.getText()));
          } catch (NumberFormatException e) {
            viewController.showAlert("Please enter a valid session number.");
          }
        });

    statsButton.setOnMouseClicked(e -> viewController.showScene(ViewController.SceneName.STATS));
    exitButton.setOnMouseClicked(e -> exitApplication());

    StyleManager.applyFadeInAnimation(startGameButton, 1500);
    StyleManager.applyFadeInAnimation(joinSessionButton, 1500);
    StyleManager.applyFadeInAnimation(statsButton, 1500);
    StyleManager.applyFadeInAnimation(exitButton, 1500);

    HBox startGameBox = new HBox(10, StyleManager.createBulletListSpacer(),  startGameButton);
    HBox sessionBox = new HBox(10, StyleManager.createBulletListSpacer(),  joinSessionButton, sessionIdField);
    HBox statsBox = new HBox(10, StyleManager.createBulletListSpacer(),  statsButton);
    HBox exitBox = new HBox(10, StyleManager.createBulletListSpacer(),  exitButton);

    startGameBox.setAlignment(Pos.TOP_LEFT);
    sessionBox.setAlignment(Pos.TOP_LEFT);
    statsBox.setAlignment(Pos.TOP_LEFT);
    exitBox.setAlignment(Pos.TOP_LEFT);

    this.getChildren()
        .addAll(startGameBox, sessionBox, statsBox, exitBox);
  }

  /** Creates and returns a title panel with an image. */
  private VBox createTitlePanel() {
    VBox imagePanel = new VBox();
    imagePanel.setAlignment(Pos.TOP_CENTER);
    Image image = new Image(getClass().getResourceAsStream("/images/title.png"));
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(400);
    imageView.setPreserveRatio(true);
    imagePanel.getChildren().add(imageView);
    return imagePanel;
  }

  /** Safely exits the application, ensuring all resources are released. */
  private void exitApplication() {
    System.exit(0);
  }
}
