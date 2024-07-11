package typeracer.client.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import typeracer.client.ViewController;

/**
 * Represents the main menu user interface for the TypeRacer game. This class sets up the GUI
 * elements that allow the user to navigate to different parts of the game, including starting a new
 * game, accessing profile settings, viewing stats, and exiting the game.
 */
public class MainMenuUi extends VBox {

  // For testing purpose only. Must be replayed later!!!
  private int defaultLobby = 1;

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
    this.setAlignment(Pos.CENTER);
    this.setSpacing(15);
    this.setBackground(
        new Background(new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, null)));

    VBox titlePanel = createTitlePanel();
    this.getChildren().add(titlePanel);

    Region spacer = new Region();
    spacer.setPrefHeight(30);
    this.getChildren().add(spacer);

    startGameButton =
        StyleManager.createStyledButton(
            "start new game", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    joinSessionButton =
            StyleManager.createStyledButton("Join Session", StyleManager.ORANGE_BUTTON,
                    StyleManager.STANDARD_FONT);
    profileSettingButton =
        StyleManager.createStyledButton(
            "profile settings", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);
    statsButton =
        StyleManager.createStyledButton(
            "view stats", StyleManager.ORANGE_BUTTON, StyleManager.STANDARD_FONT);
    exitButton =
        StyleManager.createStyledButton(
            "exit", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);

    startGameButton.setOnAction(e -> viewController.createLobby());
    sessionIdField = new TextField();
    sessionIdField.setPromptText("Enter Session ID");
    sessionIdField.setMaxWidth(200);
    joinSessionButton.setOnAction(event -> {
      try {
        viewController.joinLobby(Integer.parseInt(sessionIdField.getText()));
      } catch (NumberFormatException e) {
        viewController.showAlert("Please enter a valid session number: " + e.getMessage());
      }
    });
    VBox sessionBox = new VBox(10, sessionIdField, joinSessionButton);
    sessionBox.setAlignment(Pos.CENTER);

    profileSettingButton.setOnAction(e -> viewController.editProfile());
    statsButton.setOnAction(e -> viewController.viewStats());
    exitButton.setOnAction(e -> exitApplication());

    StyleManager.applyFadeInAnimation(startGameButton, 1500);
    StyleManager.applyFadeInAnimation(joinSessionButton, 1500);
    StyleManager.applyFadeInAnimation(profileSettingButton, 1500);
    StyleManager.applyFadeInAnimation(statsButton, 1500);
    StyleManager.applyFadeInAnimation(exitButton, 1500);

    StyleManager.applyButtonHoverAnimation(
        startGameButton, profileSettingButton, statsButton, exitButton);

    this.getChildren().addAll(startGameButton, sessionBox, profileSettingButton, statsButton, exitButton);
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
