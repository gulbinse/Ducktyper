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
 * Represents the game results user interface for the TypeRacer game. This class sets up the GUI
 * elements that display the game results, including the player's stats and the leaderboard.
 */
public class GameResultsUi extends VBox {

  /** Constructs a GameResultsUi pane. Initializes the layout with specified spacing. */
  public GameResultsUi() {
    super(10);
    initializeUi();
  }

  /** Initializes the UI elements of the GameResultsUi pane. */
  private void initializeUi() {
    this.setAlignment(Pos.CENTER);
    this.setPadding(new Insets(20));
    this.setStyle(
        "-fx-background-color: " + StyleManager.colorToHex(StyleManager.BACKGROUND_COLOR) + ";");

    Label titleLabel = new Label("Game Results");
    titleLabel.setFont(StyleManager.BOLD_FONT);
    titleLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");

    VBox statsBox = createStatsBox();
    VBox leaderboardPanel = createLeaderboard();
    leaderboardPanel.setPadding(new Insets(20, 0, 20, 0));

    Button playAgainButton =
        StyleManager.createStyledButton(
            "play again", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    Button exitButton =
        StyleManager.createStyledButton(
            "exit", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);

    HBox buttonBox = new HBox(20, playAgainButton, exitButton);
    buttonBox.setAlignment(Pos.CENTER);

    this.getChildren().addAll(titleLabel, statsBox, leaderboardPanel, buttonBox);

    playAgainButton.setOnAction(
        e -> StyleManager.switchToGameUi((Stage) this.getScene().getWindow()));
    exitButton.setOnAction(e -> closeWindow());

    StyleManager.applyFadeInAnimation(titleLabel, 1000);
    StyleManager.applyFadeInAnimation(statsBox, 1200);
    StyleManager.applyFadeInAnimation(leaderboardPanel, 1400);
    StyleManager.applyFadeInAnimation(buttonBox, 1600);

    StyleManager.applyButtonHoverAnimation(playAgainButton, exitButton);
  }

  /**
   * Creates a VBox containing all the stat labels. Sets up the layout and styling for displaying
   * player statistics.
   *
   * @return A VBox containing the "Your Stats" label, WPM label, and Errors label.
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

    Label statsLabel = new Label("Your Stats:");
    statsLabel.setFont(StyleManager.ITALIC_FONT);

    Label wpmLabel = new Label("WPM: 0");
    wpmLabel.setFont(StyleManager.STANDARD_FONT);

    Label errorsLabel = new Label("Errors: 0");
    errorsLabel.setFont(StyleManager.STANDARD_FONT);

    statsBox.getChildren().addAll(statsLabel, wpmLabel, errorsLabel);
    VBox.setMargin(statsBox, new Insets(10, 200, 10, 200));
    VBox.setMargin(statsLabel, new Insets(0, 0, 10, 0));
    return statsBox;
  }

  /**
   * Creates the leaderboard panel displaying player rankings.
   *
   * @return A VBox containing the leaderboard.
   */
  private VBox createLeaderboard() {
    VBox leaderboardBox = new VBox(10);
    leaderboardBox.setAlignment(Pos.CENTER);
    leaderboardBox.setPadding(new Insets(10));
    leaderboardBox.setStyle(
        "-fx-background-color: "
            + StyleManager.colorToHex(StyleManager.GREY_BOX)
            + "; -fx-border-color: black; -fx-border-width: 1px;");

    Label leaderboardTitle = new Label("Leaderboard:");
    leaderboardTitle.setFont(StyleManager.ITALIC_FONT);

    VBox.setMargin(leaderboardTitle, new Insets(0, 0, 10, 0));

    Label playerA = new Label("1. Player A - WPM: 0, Errors: 0");
    Label playerB = new Label("2. Player B - WPM: 0, Errors: 0");
    Label playerC = new Label("3. Player C - WPM: 0, Errors: 0");

    playerA.setFont(StyleManager.STANDARD_FONT);
    playerB.setFont(StyleManager.STANDARD_FONT);
    playerC.setFont(StyleManager.STANDARD_FONT);

    leaderboardBox.getChildren().addAll(leaderboardTitle, playerA, playerB, playerC);
    VBox.setMargin(leaderboardBox, new Insets(10, 200, 50, 200));
    return leaderboardBox;
  }

  /** Closes the current window. */
  private void closeWindow() {
    Stage stage = (Stage) this.getScene().getWindow();
    stage.close();
  }
}
