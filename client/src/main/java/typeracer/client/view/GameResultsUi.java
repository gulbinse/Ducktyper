package typeracer.client.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import typeracer.client.ViewController;

/**
 * Represents the game results user interface for the TypeRacer game. This class sets up the GUI
 * elements that display the game results, including the player's stats and the leaderboard.
 */
public class GameResultsUi extends VBox {

  /** The controller managing views and handling interactions. */
  private final ViewController viewController;

  private VBox gameResults;
  /**
   * Constructs a new GameResultsUi and initializes its user interface.
   *
   * @param viewController The controller to manage views and handle interactions.
   */
  public GameResultsUi(ViewController viewController) {
    super(10);
    this.viewController = viewController;
    initializeUi();
  }

  /** Initializes the UI elements of the GameResultsUi pane. */
  private void initializeUi() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(20);
    this.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    Label titleLabel = new Label("Game Results");
    titleLabel.setFont(StyleManager.BOLD_FONT);
    titleLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");

//    VBox statsBox = createStatsBox();
//    gameResults = statsBox;
    // VBox leaderboardPanel = createLeaderboard();
    // leaderboardPanel.setPadding(new Insets(20, 0, 20, 0));

    Button mainMenuButton =
        StyleManager.createStyledButton(
            "main menu", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);

    this.getChildren().addAll(titleLabel, mainMenuButton);

    mainMenuButton.setOnAction(e -> viewController.showScene(ViewController.SceneName.MAIN_MENU));

    StyleManager.applyFadeInAnimation(titleLabel, 1000);
    // StyleManager.applyFadeInAnimation(leaderboardPanel, 1400);
    StyleManager.applyFadeInAnimation(mainMenuButton, 1600);

    StyleManager.applyButtonHoverAnimation(mainMenuButton);
  }

  /**
   * Creates a VBox containing all the stat labels. Sets up the layout and styling for displaying
   * player statistics.
   *
   * @return A VBox containing the "Your Stats" label, WPM label, and Errors label.
   */
//  private VBox createStatsBox() {
//
//    return statsBox;
//  }

  public void onViewShown(){
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
    VBox.setMargin(statsLabel, new Insets(0, 0, 10, 0));

    int playerId = viewController.getPlayerId();

    Label wpmLabel = new Label();
    DoubleProperty wpmProperty = viewController.getPlayerWpmProperty(playerId);
    StringBinding wpmBinding = Bindings.createStringBinding(() -> String.format("%.2f", wpmProperty.getValue()) + " WPM", wpmProperty);
    wpmLabel.textProperty().bind(wpmBinding);
    wpmLabel.setFont(StyleManager.STANDARD_FONT);

    Label accuracyLabel = new Label();
    DoubleProperty accuracyProperty = viewController.getPlayerAccuracyProperty(playerId);
    StringBinding accuracyBinding = Bindings.createStringBinding(() -> String.format("%.2f", accuracyProperty.getValue() * 100) + "% Accuracy", accuracyProperty);
    accuracyLabel.textProperty().bind(accuracyBinding);
    accuracyLabel.setFont(StyleManager.STANDARD_FONT);

    statsBox.getChildren().addAll(statsLabel, wpmLabel, accuracyLabel);
    VBox.setMargin(statsBox, new Insets(10, 50, 10, 50));

    getChildren().add(getChildren().size() - 1, statsBox);
  }
  /*
   * Creates the leaderboard panel displaying player rankings.
   *
   * @return A VBox containing the leaderboard.
   */

  /*
   * private VBox createLeaderboard() { VBox leaderboardBox = new VBox(10);
   * leaderboardBox.setAlignment(Pos.CENTER); leaderboardBox.setPadding(new Insets(10));
   * leaderboardBox.setStyle( "-fx-background-color: " +
   * StyleManager.colorToHex(StyleManager.GREY_BOX) + "; -fx-border-color: black; -fx-border-width:
   * 1px;");
   *
   * <p>Label leaderboardTitle = new Label("Leaderboard:");
   * leaderboardTitle.setFont(StyleManager.ITALIC_FONT);
   *
   * <p>VBox.setMargin(leaderboardTitle, new Insets(0, 0, 10, 0));
   *
   * <p>ListView<String> leaderboardListView = new ListView<>();
   * leaderboardListView.setItems(viewController.topPlayersProperty());
   *
   * <p>leaderboardBox.getChildren().addAll(leaderboardTitle, leaderboardListView);
   * VBox.setMargin(leaderboardBox, new Insets(10, 200, 50, 200)); return leaderboardBox; }*
   */
}
