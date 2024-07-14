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

  /** Button to access profile settings. */
  private Button profileSettingButton;

  /** Button to view player statistics. */
  private Button statsButton;

  /** Button to exit the game. */
  private Button exitButton;

  private Button joinSessionButton;

  private TextField sessionIdField;

  /**
   * Constructs a new MainMenuUi and initializes its user interface.
   *
   * @param viewController The controller to manage views and handle interactions.
   */
  public MainMenuUi(ViewController viewController) {
    this.viewController = viewController;
    initializeUi();
  }

  /**
   * Initializes the user interface components for the main menu. Sets up the layout, styling, and
   * button actions.
   */
  private void initializeUi() {
    Background background = new Background(new BackgroundImage(
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Menu_screen_noText.png"))),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)));
    this.setBackground(background);
    this.setAlignment(Pos.CENTER);
    this.setSpacing(15);

    VBox titlePanel = createTitlePanel();
    this.getChildren().add(titlePanel);

    Region spacer = new Region();
    spacer.setPrefHeight(30);
    this.getChildren().add(spacer);

    startGameButton =
        StyleManager.createStyledButton(
            "New Session", StyleManager.STANDARD_BUTTON, StyleManager.STANDARD_FONT);
    joinSessionButton =
        StyleManager.createStyledButton(
            "Join Session", StyleManager.STANDARD_BUTTON, StyleManager.STANDARD_FONT);
    profileSettingButton =
        StyleManager.createStyledButton(
            "Profile", StyleManager.STANDARD_BUTTON, StyleManager.STANDARD_FONT);
    statsButton =
        StyleManager.createStyledButton(
            "Stats", StyleManager.STANDARD_BUTTON, StyleManager.STANDARD_FONT);
    exitButton =
        StyleManager.createStyledButton(
            "Exit", StyleManager.STANDARD_BUTTON, StyleManager.STANDARD_FONT);

    startGameButton.setOnAction(e -> viewController.createSession());
    sessionIdField = new TextField();
    sessionIdField.setPromptText("Enter Session ID");
    sessionIdField.setMaxWidth(200);
    sessionIdField.getStyleClass().add("startScreen-input-field");
    joinSessionButton.setOnAction(
        event -> {
          try {
            viewController.joinSession(Integer.parseInt(sessionIdField.getText()));
          } catch (NumberFormatException e) {
            viewController.showAlert("Please enter a valid session number.");
          }
        });
    VBox sessionBox = new VBox(10, sessionIdField, joinSessionButton);
    sessionBox.setAlignment(Pos.CENTER);

    profileSettingButton.setOnAction(
        e -> viewController.showScene(ViewController.SceneName.PROFILE_SETTINGS));
    statsButton.setOnAction(e -> viewController.showScene(ViewController.SceneName.STATS));
    exitButton.setOnAction(e -> exitApplication());

    StyleManager.applyFadeInAnimation(startGameButton, 1500);
    StyleManager.applyFadeInAnimation(joinSessionButton, 1500);
    StyleManager.applyFadeInAnimation(profileSettingButton, 1500);
    StyleManager.applyFadeInAnimation(statsButton, 1500);
    StyleManager.applyFadeInAnimation(exitButton, 1500);

    StyleManager.applyButtonHoverAnimation(
        startGameButton,joinSessionButton, profileSettingButton, statsButton, exitButton);

    this.getChildren()
        .addAll(startGameButton, sessionBox, profileSettingButton, statsButton, exitButton);
  }

  /** Creates and returns a title panel with an image. */
  private VBox createTitlePanel() {
    VBox imagePanel = new VBox();
    imagePanel.setAlignment(Pos.TOP_CENTER);
    Image image = new Image(getClass().getResourceAsStream("/images/title.png"));
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(350);
    imageView.setPreserveRatio(true);
    imagePanel.getChildren().add(imageView);
    return imagePanel;
  }

  /** Safely exits the application, ensuring all resources are released. */
  private void exitApplication() {
    System.exit(0);
  }
}
