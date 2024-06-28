package typeracer.client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Represents the player statistics user interface for the TypeRacer game. This class sets up the
 * GUI elements that display the player's statistics, including total games played, average WPM,
 * total errors, best WPM, and average accuracy.
 */
public class PlayerStatsUi extends VBox {

  /** Constructs a new PlayerStatsUi and initializes its user interface. */
  public PlayerStatsUi() {
    initializeUi();
  }

  /**
   * Initializes the user interface components for the player stats UI. Sets up the layout, styling,
   * and button actions.
   */
  private void initializeUi() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(10);
    this.setPadding(new Insets(30));
    this.setStyle("-fx-background-color: white;");

    Label titleLabel = new Label("Player Statistics");
    titleLabel.setFont(StyleManager.BOLD_FONT);
    this.getChildren().add(titleLabel);

    final VBox statsBox = createStatsBox();

    Button backButton =
        StyleManager.createStyledButton(
            "back", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);
    backButton.setOnAction(e -> StyleManager.switchToMainMenu((Stage) this.getScene().getWindow()));

    Button resetButton =
        StyleManager.createStyledButton(
            "reset stats", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);
    resetButton.setOnAction(e -> resetStats());

    HBox buttonBar = new HBox(10, backButton, resetButton);
    buttonBar.setAlignment(Pos.CENTER);

    this.getChildren().addAll(statsBox, buttonBar);

    StyleManager.applyFadeInAnimation(statsBox, 1000);
    StyleManager.applyButtonHoverAnimation(backButton, resetButton);
  }

  /**
   * Creates a VBox containing all the stat labels.
   *
   * @return A VBox containing all the stat labels.
   */
  private VBox createStatsBox() {
    VBox statsBox = new VBox(10);
    statsBox.setAlignment(Pos.CENTER);
    statsBox.setBackground(
        new Background(new BackgroundFill(StyleManager.GREY_BOX, CornerRadii.EMPTY, Insets.EMPTY)));
    statsBox.setPadding(new Insets(15));
    statsBox.setBorder(
        new Border(
            new BorderStroke(
                Paint.valueOf("black"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1))));

    statsBox.getChildren().add(createStatLabel("Total games played: ", "0"));
    statsBox.getChildren().add(createStatLabel("Average WPM: ", "0"));
    statsBox.getChildren().add(createStatLabel("Total errors: ", "0"));
    statsBox.getChildren().add(createStatLabel("Best WPM: ", "0"));
    statsBox.getChildren().add(createStatLabel("Average accuracy: ", "0%"));

    VBox.setMargin(statsBox, new Insets(10, 75, 30, 75));
    return statsBox;
  }

  /** Resets the player's statistics. This method should contain the logic to reset the stats. */
  private void resetStats() {
    // Logic to reset stats goes here
  }

  /**
   * Creates a label with the specified text and value, laid out in a horizontal box.
   *
   * @param labelText The text for the label.
   * @param valueText The label displaying the value.
   * @return A HBox containing the label and value.
   */
  private HBox createStatLabel(String labelText, String valueText) {
    Label label = new Label(labelText);
    label.setFont(StyleManager.STANDARD_FONT);

    Label valueLabel = new Label(valueText);
    valueLabel.setFont(StyleManager.STANDARD_FONT);

    HBox hbox = new HBox();
    hbox.setSpacing(5);
    hbox.getChildren().addAll(label, valueLabel);
    return hbox;
  }
}
