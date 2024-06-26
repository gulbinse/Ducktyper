package typeracer.client.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Manages and centralizes styling for the TypeRacer game UI. This class provides a single point of
 * reference for UI colors and fonts, ensuring consistency across different parts of the
 * application.
 */
public class StyleManager {
  /** Background color for the UI. */
  public static final Color BACKGROUND_COLOR = Color.web("#F5F5F5");

  /** Color for green buttons. */
  public static final Color GREEN_BUTTON = Color.web("#009900");

  /** Color for blue buttons. */
  public static final Color BLUE_BUTTON = Color.web("#007FFF");

  /** Color for orange buttons. */
  public static final Color ORANGE_BUTTON = Color.web("#CC6600");

  /** Color for red buttons. */
  public static final Color RED_BUTTON = Color.web("#FF0000");

  /** Color for grey buttons. */
  public static final Color GREY_BOX = Color.web("#E6E6E6");

  /** Standard font for UI text. */
  public static final Font STANDARD_FONT = Font.font("Arial", 14);

  /** Bold font for emphasized UI text. */
  public static final Font BOLD_FONT = Font.font("Arial", FontWeight.BOLD, 18);

  /** Italic font for UI text. */
  public static final Font ITALIC_FONT = Font.font("Arial", FontPosture.ITALIC, 16);

  /** Default constructor for the StyleManager class. Initializes a new instance of StyleManager. */
  public StyleManager() {}

  /**
   * Creates a styled button with specified text, background color, and font. The button will have a
   * hover effect applied.
   *
   * @param text The text to display on the button.
   * @param backgroundColor The background color of the button.
   * @param font The font to use for the button text.
   * @return The styled button.
   */
  public static Button createStyledButton(String text, Color backgroundColor, Font font) {
    Button button = new Button(text);
    String hexColor = StyleManager.colorToHex(backgroundColor);
    String defaultStyle =
        "-fx-background-color: "
            + hexColor
            + "; -fx-text-fill: white; "
            + "-fx-border-width: 0; -fx-border-color: transparent;";
    String hoverStyle =
        "-fx-background-color: "
            + hexColor
            + "; -fx-text-fill: white; "
            + "-fx-border-color: white; -fx-border-width: 2px;";
    button.setStyle(defaultStyle);
    button.setFont(font);
    button.setOnMouseEntered(
        e -> {
          button.setStyle(hoverStyle);
          ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
          scaleUp.setToX(1.1);
          scaleUp.setToY(1.1);
          scaleUp.play();
        });
    button.setOnMouseExited(
        e -> {
          button.setStyle(defaultStyle);
          ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
          scaleDown.setToX(1.0);
          scaleDown.setToY(1.0);
          scaleDown.play();
        });
    button.setMinSize(120, 40);
    button.setMaxSize(120, 40);
    button.setPrefSize(120, 40);
    return button;
  }

  /**
   * Converts a Color object to its hexadecimal string representation.
   *
   * @param color The Color object to convert.
   * @return The hexadecimal string representation of the color.
   */
  public static String colorToHex(Color color) {
    return String.format(
        "#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

  /**
   * Switches the scene of the given stage.
   *
   * @param stage The stage on which to set the scene.
   * @param scene The scene to display on the stage.
   */
  public static void switchToScene(Stage stage, Scene scene) {
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Switches to the main menu scene.
   *
   * @param stage The stage on which to set the main menu scene.
   */
  public static void switchToMainMenu(Stage stage) {
    MainMenuUi mainMenuUi = new MainMenuUi();
    Scene scene = new Scene(mainMenuUi, 400, 450);
    switchToScene(stage, scene);
  }

  /**
   * Switches to the game UI scene.
   *
   * @param stage The stage on which to set the game UI scene.
   */
  public static void switchToGameUi(Stage stage) {
    GameUi gameUi = new GameUi();
    Scene scene = new Scene(gameUi, 800, 600);
    switchToScene(stage, scene);
  }

  /**
   * Switches to the game results UI scene.
   *
   * @param stage The stage on which to set the game results UI scene.
   */
  public static void switchToGameResultUi(Stage stage) {
    GameResultsUi gameResultUi = new GameResultsUi();
    Scene scene = new Scene(gameResultUi, stage.getWidth(), stage.getHeight());
    switchToScene(stage, scene);
  }

  /**
   * Switches to the player statistics UI scene.
   *
   * @param stage The stage on which to set the player statistics UI scene.
   */
  public static void switchToStatsUi(Stage stage) {
    PlayerStatsUi statsUi = new PlayerStatsUi();
    Scene scene = new Scene(statsUi, 400.0, 450.0);
    switchToScene(stage, scene);
  }

  /**
   * Switches to the profile settings UI scene.
   *
   * @param stage The stage on which to set the profile settings UI scene.
   */
  public static void switchToProfileSettingsUi(Stage stage) {
    ProfileSettingsUi profileSettingsUi = new ProfileSettingsUi();
    Scene scene = new Scene(profileSettingsUi, 400.0, 450.0);
    switchToScene(stage, scene);
  }

  /**
   * Applies a fade-in animation to the specified node.
   *
   * @param node The node to apply the animation to.
   * @param duration The duration of the fade-in animation in milliseconds.
   */
  public static void applyFadeInAnimation(Node node, int duration) {
    FadeTransition fadeIn = new FadeTransition(Duration.millis(duration), node);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  /**
   * Applies a hover animation to the specified buttons.
   *
   * @param buttons The buttons to apply the animation to.
   */
  public static void applyButtonHoverAnimation(Button... buttons) {
    for (Button button : buttons) {
      button.setOnMouseEntered(
          e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.play();
          });
      button.setOnMouseExited(
          e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
          });
    }
  }
}
