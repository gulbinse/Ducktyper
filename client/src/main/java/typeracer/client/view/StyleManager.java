package typeracer.client.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Manages and centralizes styling for the TypeRacer game UI. This class provides a single point of
 * reference for UI colors and fonts, ensuring consistency across different parts of the
 * application.
 */
public class StyleManager {

  /**
   * The color used for the start screen background. This color is defined as a web color with the
   * hex code "#2374AB".
   */

  /**
   * Color used for the start screen background.
   */

  public static final Color START_SCREEN = Color.web("#2374AB");
  /**
   * Color used for green buttons.
   */
  public static final Color GREEN_BUTTON = Color.web("#009900");

  /** Color for green buttons. */
  public static final Color STANDARD_BUTTON = Color.web("#08415C");

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

  /** Bold italic font for emphasized UI text. */
  public static final Font BOLD_ITALIC_FONT = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 18);

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
            + "; -fx-text-fill: white; -fx-border-width: "
            + "2; -fx-border-color: #52CCC1";

    button.setStyle(defaultStyle);
    button.setFont(font);
    button.setOnMouseEntered(
        e -> {
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
    button.setOnMouseClicked(
        e -> {
          ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), button);
          clickScale.setToX(0.9);
          clickScale.setToY(0.9);
          clickScale.setCycleCount(2);
          clickScale.setAutoReverse(true);
          clickScale.play();
        });
//    button.setMinSize(120, 40);
//    button.setMaxSize(120, 40);
    button.setPrefSize(160, 40);
    return button;
  }

    /**
     * Creates a button for the Main Menu.
     *
     * @param image of what the button should look like
     * @return an ImageView that acts like a button
     */
  public static ImageView createMainMenueButton(Image image) {
      ImageView submitImage =
              new ImageView(image);
      submitImage.setFitHeight(50);
      submitImage.setPreserveRatio(true);

      submitImage.setOnMouseEntered(
              e -> {
                  submitImage.setScaleX(1.1);
                  submitImage.setScaleY(1.1);
              });
      submitImage.setOnMouseExited(
              e -> {
                  submitImage.setScaleX(1.0);
                  submitImage.setScaleY(1.0);
              }
      );
      submitImage.setOnMouseReleased(
              e -> {
                  submitImage.setScaleX(1.0);
                  submitImage.setScaleY(1.0);
              });
      return submitImage;
  }

    /**
     * Creates a Region that acts like a spacer.
     *
     * @return Region as spacer
     */
  public static Region createBulletListSpacer(){
      Region spacerBulltepoints = new Region();
      spacerBulltepoints.setMinWidth(20);
      spacerBulltepoints.setPrefWidth(20);
      return spacerBulltepoints;
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
