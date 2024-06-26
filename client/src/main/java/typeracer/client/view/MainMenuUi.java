package typeracer.client.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Represents the main menu user interface for the TypeRacer game. This class sets up the GUI
 * elements that allow the user to navigate to different parts of the game, including starting a new
 * game, accessing profile settings, viewing stats, and exiting the game.
 */
public class MainMenuUi extends VBox {

  /** Button to start a new game. */
  private Button startGameButton;

  /** Button to access profile settings. */
  private Button profileSettingButton;

  /** Button to view player statistics. */
  private Button statsButton;

  /** Button to exit the game. */
  private Button exitButton;

  /** Constructs a new MainMenuUi and initializes its user interface. */
  public MainMenuUi() {
    initializeUi();
  }

  /**
   * Initializes the user interface components for the main menu. Sets up the layout, styling, and
   * button actions.
   */
  private void initializeUi() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(15);
    this.setStyle(
        "-fx-background-color: #" + StyleManager.BACKGROUND_COLOR.toString().substring(2) + ";");

    Label titleLabel = new Label("Typeracer");
    titleLabel.setFont(StyleManager.BOLD_FONT);
    titleLabel.setAlignment(Pos.CENTER);

    startGameButton =
        StyleManager.createStyledButton(
            "start game", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    profileSettingButton =
        StyleManager.createStyledButton(
            "profile settings", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);
    statsButton =
        StyleManager.createStyledButton(
            "stats", StyleManager.ORANGE_BUTTON, StyleManager.STANDARD_FONT);
    exitButton =
        StyleManager.createStyledButton(
            "exit", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);

    startGameButton.setOnAction(
        e -> StyleManager.switchToGameUi((Stage) this.getScene().getWindow()));
    profileSettingButton.setOnAction(
        e -> StyleManager.switchToProfileSettingsUi((Stage) this.getScene().getWindow()));
    statsButton.setOnAction(e -> StyleManager.switchToStatsUi((Stage) this.getScene().getWindow()));
    exitButton.setOnAction(e -> System.exit(0));

    StyleManager.applyFadeInAnimation(titleLabel, 1500);
    StyleManager.applyFadeInAnimation(startGameButton, 1500);
    StyleManager.applyFadeInAnimation(profileSettingButton, 1500);
    StyleManager.applyFadeInAnimation(statsButton, 1500);
    StyleManager.applyFadeInAnimation(exitButton, 1500);

    StyleManager.applyButtonHoverAnimation(
        startGameButton, profileSettingButton, statsButton, exitButton);

    this.getChildren()
        .addAll(titleLabel, startGameButton, profileSettingButton, statsButton, exitButton);
  }
}
