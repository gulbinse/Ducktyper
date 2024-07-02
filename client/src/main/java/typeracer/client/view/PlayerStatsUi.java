package typeracer.client.view;

import javafx.application.Platform;
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
import typeracer.client.ViewController;

/**
 * Represents the player statistics user interface for the TypeRacer game. This class sets up the
 * GUI elements that display the player's statistics, including total games played, average WPM,
 * total errors, the best WPM, and average accuracy.
 */
public class PlayerStatsUi extends VBox {
  private ViewController viewController;
  private Label gamesPlayedLabel;
  private Label averageWpmLabel;
  private Label totalErrorsLabel;
  private Label bestWpmLabel;
  private Label averageAccuracyLabel;

  /**
   * Constructs a new PlayerStatsUi and initializes its user interface.
   *
   * @param viewController the controller to manage views and handle interactions.
   */
  public PlayerStatsUi(ViewController viewController) {
    this.viewController = viewController;
    initializeUi();
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
    backButton.setOnAction(e -> ViewController.switchToMainMenu());

    Button resetButton =
        StyleManager.createStyledButton(
            "reset stats", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);
    resetButton.setOnAction(e -> viewController.handleResetStats());

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

    gamesPlayedLabel = new Label("Total games played: 0");
    averageWpmLabel = new Label("Average WPM: 0");
    totalErrorsLabel = new Label("Total errors: 0");
    bestWpmLabel = new Label("Best WPM: 0");
    averageAccuracyLabel = new Label("Average accuracy: 0%");

    statsBox
        .getChildren()
        .addAll(
            gamesPlayedLabel,
            averageWpmLabel,
            totalErrorsLabel,
            bestWpmLabel,
            averageAccuracyLabel);

    VBox.setMargin(statsBox, new Insets(10, 75, 30, 75));
    return statsBox;
  }

  /**
   * Updates the displayed statistics with the provided values.
   *
   * @param gamesPlayed The total number of games played.
   * @param averageWpm The average words per minute (WPM).
   * @param totalErrors The total number of errors.
   * @param bestWpm The best WPM achieved.
   * @param averageAccuracy The average accuracy percentage.
   */
  public void updateStats(
      int gamesPlayed, double averageWpm, int totalErrors, double bestWpm, double averageAccuracy) {
    Platform.runLater(
        () -> {
          gamesPlayedLabel.setText(String.format("Total games played: %d", gamesPlayed));
          averageWpmLabel.setText(String.format("Average WPM: %.2f", averageWpm));
          totalErrorsLabel.setText(String.format("Total errors: %d", totalErrors));
          bestWpmLabel.setText(String.format("Best WPM: %.2f", bestWpm));
          averageAccuracyLabel.setText(String.format("Average accuracy: %.2f%%", averageAccuracy));
        });
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
