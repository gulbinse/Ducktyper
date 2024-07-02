package typeracer.client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import typeracer.client.ViewController;

/**
 * Represents the game user interface for the TypeRacer game. This class sets up the GUI elements
 * that display the game UI, including the typing area, statistics, and top players.
 */
public class GameUi extends VBox {

  private ViewController viewController;

  /** The text area where the typing text is displayed. */
  private TextArea displayText;

  /** The text field where the user inputs their typing. */
  private TextField inputText;

  /** The label displaying the current words per minute (WPM). */
  private Label wpmLabel;

  /** The label displaying the current number of errors. */
  private Label errorsLabel;

  /** The label displaying the top players. */
  private Label topPlayersLabel;

  /** The label that displays the username of the current user. */
  private Label usernameLabel;

  /**
   * Constructs a new GameUi and initializes its user interface.
   *
   * @param viewController The controller to manage views and handle interactions.
   */
  public GameUi(ViewController viewController) {
    this.viewController = viewController;
    initUi();
  }

  /** Initializes the UI elements of the GameUi pane. */
  private void initUi() {
    setSpacing(10);
    setAlignment(Pos.TOP_CENTER);
    setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));
    addHeaderImage();
    addUserLabel();
    addDisplayPanel();
    addInputPanel();
    addStatsPanel();
    addTopPlayersPanel();
    addButtonPanel();
  }

  /** Adds a label to display the username. The label is styled and positioned within the UI. */
  private void addUserLabel() {
    usernameLabel = new Label("Default User");
    usernameLabel.setFont(StyleManager.BOLD_FONT);
    usernameLabel.setPadding(new Insets(5, 10, 5, 10));
    usernameLabel.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));
    usernameLabel.setAlignment(Pos.CENTER);
    getChildren().add(1, usernameLabel);
  }

  /** Adds an image to the header of the UI. The image is loaded and styled appropriately. */
  private void addHeaderImage() {
    VBox headerPanel = new VBox();
    headerPanel.setAlignment(Pos.CENTER);
    headerPanel.setPadding(new Insets(20, 0, 10, 0));
    Image titleImage = new Image(getClass().getResourceAsStream("/images/title.png"));
    ImageView titleImageView = new ImageView(titleImage);
    titleImageView.setFitWidth(300);
    titleImageView.setPreserveRatio(true);
    headerPanel.getChildren().add(titleImageView);
    getChildren().add(headerPanel);
  }

  /** Adds a display panel that contains a non-editable TextArea for displaying typing text. */
  private void addDisplayPanel() {
    VBox panel = new VBox();
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(10, 50, 10, 50));
    displayText = new TextArea("Typing text will appear here");
    displayText.setEditable(false);
    displayText.setWrapText(true);
    displayText.setPrefHeight(150);
    displayText.setMaxWidth(Double.MAX_VALUE);
    panel.getChildren().add(displayText);
    getChildren().add(panel);
  }

  /**
   * Adds an input panel that contains a TextField for user input. The panel is styled and
   * positioned within the UI.
   */
  private void addInputPanel() {
    VBox panel = new VBox();
    panel.setMaxHeight(350);
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(10, 50, 10, 50));
    inputText = new TextField();
    inputText.setPrefHeight(150);
    inputText.setMaxWidth(Double.MAX_VALUE);
    inputText.setPadding(new Insets(10));
    inputText.setStyle("-fx-alignment: top-left;");
    panel.getChildren().add(inputText);
    getChildren().add(panel);
  }

  /**
   * Adds a stats panel that displays the user's WPM and error count. The panel is styled and
   * positioned within the UI.
   */
  private void addStatsPanel() {
    HBox statsPanel = new HBox(10);
    statsPanel.setAlignment(Pos.CENTER);
    statsPanel.setPadding(new Insets(10, 50, 10, 50));
    statsPanel.setBackground(
        new Background(
            new BackgroundFill(StyleManager.GREY_BOX, new CornerRadii(5), Insets.EMPTY)));
    statsPanel.setBorder(
        new Border(
            new BorderStroke(
                Paint.valueOf("black"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1))));
    wpmLabel = new Label("WPM: 0");
    wpmLabel.setAlignment(Pos.CENTER_LEFT);
    errorsLabel = new Label("Errors: 0");
    errorsLabel.setAlignment(Pos.CENTER_RIGHT);
    HBox leftContainer = new HBox(wpmLabel);
    leftContainer.setAlignment(Pos.CENTER_LEFT);
    leftContainer.setPadding(new Insets(0, 0, 0, 10));
    HBox.setHgrow(leftContainer, Priority.ALWAYS);
    HBox rightContainer = new HBox(errorsLabel);
    rightContainer.setAlignment(Pos.CENTER_RIGHT);
    rightContainer.setPadding(new Insets(0, 10, 0, 0));
    HBox.setHgrow(rightContainer, Priority.ALWAYS);
    statsPanel.getChildren().addAll(leftContainer, rightContainer);
    VBox.setMargin(statsPanel, new Insets(10, 50, 10, 50));
    getChildren().add(statsPanel);
  }

  /** Adds a panel to display the top players. The panel is styled and positioned within the UI. */
  private void addTopPlayersPanel() {
    VBox topPlayersPanel = new VBox();
    topPlayersPanel.setAlignment(Pos.CENTER);
    topPlayersPanel.setPadding(new Insets(10, 50, 10, 50));
    topPlayersPanel.setBackground(
        new Background(
            new BackgroundFill(StyleManager.GREY_BOX, new CornerRadii(5), Insets.EMPTY)));
    topPlayersPanel.setBorder(
        new Border(
            new BorderStroke(
                Paint.valueOf("black"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT)));
    topPlayersLabel = new Label("Top players: 1. A, 2. B, 3. C");
    topPlayersLabel.setAlignment(Pos.CENTER);
    topPlayersLabel.setFont(StyleManager.STANDARD_FONT);
    topPlayersPanel.getChildren().add(topPlayersLabel);
    getChildren().add(topPlayersPanel);
    VBox.setMargin(topPlayersPanel, new Insets(10, 50, 10, 50));
  }

  /**
   * Adds a panel containing a button to exit the game. The panel is styled and positioned within
   * the UI. The exit button uses a custom style and triggers the attemptToExitGame() method when
   * clicked.
   */
  private void addButtonPanel() {
    FlowPane buttonPanel = new FlowPane(10, 10);
    buttonPanel.setAlignment(Pos.CENTER);
    buttonPanel.setPadding(new Insets(10, 10, 30, 10));
    Button exitButton =
        StyleManager.createStyledButton(
            "exit", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);
    exitButton.setOnAction(e -> attemptToExitGame());
    buttonPanel.getChildren().add(exitButton);
    getChildren().add(buttonPanel);
  }

  /**
   * Attempts to exit the game by showing a confirmation dialog. If the user confirms, switches to
   * the game results UI.
   */
  private void attemptToExitGame() {
    Alert alert =
        new Alert(
            AlertType.CONFIRMATION,
            "Are you sure you want to leave the game?",
            ButtonType.YES,
            ButtonType.NO);
    alert.setTitle("Confirm Exit");
    alert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.YES) {
                viewController.endGame();
                viewController.switchToGameResultUi();
              }
            });
  }

  /**
   * Updates the display text area with the given text.
   *
   * @param text The text to display.
   */
  public void updateText(String text) {
    displayText.setText(text);
  }

  /**
   * Updates the WPM (words per minute) label with the given WPM value.
   *
   * @param wpm The new WPM value to display.
   */
  public void updateWpm(double wpm) {
    wpmLabel.setText(String.format("WPM: %.2f", wpm));
  }

  /**
   * Updates the errors label with the given number of errors.
   *
   * @param errors The new error count to display.
   */
  public void updateErrors(int errors) {
    errorsLabel.setText(String.format("Errors: %d", errors));
  }

  /**
   * Updates the top players label with the given list of players.
   *
   * @param players The formatted string of top players to display.
   */
  public void updateTopPlayers(String players) {
    topPlayersLabel.setText("Top players: " + players);
  }

  /**
   * Called when the view is shown. Updates the username label with the current username from the
   * view controller.
   */
  public void onViewShown() {
    if (usernameLabel != null) {
      usernameLabel.setText(viewController.getUsername());
    }
  }
}
