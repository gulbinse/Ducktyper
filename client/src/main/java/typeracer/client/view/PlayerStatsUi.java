package typeracer.client.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import typeracer.client.ViewController;

/**
 * Represents the player statistics user interface for the TypeRacer game. This class sets up the
 * GUI elements that display the player's statistics, including total games played, average WPM,
 * total errors, the best WPM, and average accuracy.
 */
public class PlayerStatsUi extends VBox {
  /** The view controller to manage views and handle interactions. */
  private final ViewController viewController;

  /** Label for displaying the number of games played. */
  private Label gamesPlayedLabel;

  /** Label for displaying the average words per minute (WPM). */
  private Label averageWpmLabel;

  /** Label for displaying the total number of errors. */
  private Label totalErrorsLabel;

  /** Label for displaying the best words per minute (WPM) achieved. */
  private Label bestWpmLabel;

  /** Label for displaying the average accuracy. */
  private Label averageAccuracyLabel;

  private PlayerStatsUi(ViewController viewController) {
    this.viewController = viewController;
  }

  /**
   * Creates a new PlayerStatsUi and initializes its user interface.
   *
   * @param viewController the controller to manage views and handle interactions.
   */
  public static PlayerStatsUi create(ViewController viewController) {
    PlayerStatsUi playerStatsUi = new PlayerStatsUi(viewController);
    playerStatsUi.initializeUi();
    return playerStatsUi;
  }

  /**
   * Initializes the user interface components for the player stats UI. Sets up the layout, styling,
   * and button actions.
   */
  private void initializeUi() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(10);
    this.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    Label titleLabel = new Label("Player Statistics");
    titleLabel.setFont(StyleManager.BOLD_FONT);
    this.getChildren().add(titleLabel);

    final VBox statsBox = createStatsBox();

    Button backButton =
        StyleManager.createStyledButton(
            "back", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);
    backButton.setOnAction(e -> viewController.showScene(ViewController.SceneName.MAIN_MENU));

    this.getChildren().addAll(statsBox, backButton);

    StyleManager.applyFadeInAnimation(statsBox, 1000);
    StyleManager.applyButtonHoverAnimation(backButton);
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

    Label roundsLabel = new Label();
    roundsLabel.setText("Rounds played: " + viewController.getRoundsPlayed());
    roundsLabel.setAlignment(Pos.CENTER_LEFT);

    Label wpmLabel = new Label();
    wpmLabel.setText(String.format("Average WPM: %.2f", viewController.getAverageWPM()));
    wpmLabel.setAlignment(Pos.CENTER_LEFT);

    Label accuracyLabel = new Label();
    accuracyLabel.setText(String.format("Average accuracy: %.2f%%", viewController.getAverageAccuracy() * 100));
    accuracyLabel.setAlignment(Pos.CENTER_LEFT);

    VBox.setMargin(roundsLabel, new Insets(10, 50, 10, 50));
    VBox.setMargin(wpmLabel, new Insets(10, 50, 10, 50));
    VBox.setMargin(accuracyLabel, new Insets(10, 50, 10, 50));
    VBox.setMargin(statsBox, new Insets(10, 50, 10, 50));

    statsBox.getChildren().addAll(roundsLabel, wpmLabel, accuracyLabel);
    return statsBox;
  }

  /** Clears the displayed statistics, resetting all values to zero. */
  public void clearDisplayedStats() {
    Platform.runLater(
        () -> {
          gamesPlayedLabel.setText("Total games played: 0");
          averageWpmLabel.setText("Average WPM: 0");
          totalErrorsLabel.setText("Total errors: 0");
          bestWpmLabel.setText("Best WPM: 0");
          averageAccuracyLabel.setText("Average accuracy: 0%");
        });
  }
}
