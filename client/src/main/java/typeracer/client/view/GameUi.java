package typeracer.client.view;

import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import typeracer.client.ViewController;

/**
 * Represents the game user interface for the TypeRacer game. This class sets up the GUI elements
 * that display the game UI, including the typing area, statistics, and top players.
 */
public class GameUi extends VBox {

  /** The controller to manage views and handle interactions. */
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

  private int currentCharIndex = 0;

  private String gameText;

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
    displayText = new TextArea();
    displayText.setEditable(false);
    displayText.setWrapText(true);
    displayText.setPrefHeight(150);
    displayText.setMaxWidth(Double.MAX_VALUE);
    displayText.textProperty().bind(viewController.gameTextProperty());
    VBox panel = new VBox();
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(10, 50, 10, 50));
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
    inputText.setOnKeyTyped(event -> handleTyping(event.getCharacter()));
    panel.getChildren().add(inputText);
    getChildren().add(panel);
  }

  private void handleTyping(String typedCharacter) {
    int currentPlayerId = viewController.getCurrentPlayerId();
    char expectedCharacter = getCurrentExpectedCharacter();

    if (typedCharacter.isEmpty() || typedCharacter.charAt(0) != expectedCharacter) {
      IntegerProperty errorsProperty = viewController.getPlayerErrorsProperty(currentPlayerId);
      errorsProperty.set(errorsProperty.get() + 1);
    }
  }

  private char getCurrentExpectedCharacter() {
    if (gameText == null || gameText.isEmpty() || currentCharIndex >= gameText.length()) {
      return '\0';
    }
    return gameText.charAt(currentCharIndex);
  }

  /**
   * Adds a stats panel that displays the user's WPM and error count. The panel is styled and
   * positioned within the UI.
   */
  private void addStatsPanel() {
    HBox statsPanel = new HBox(30);
    statsPanel.setAlignment(Pos.CENTER);
    statsPanel.setPadding(new Insets(10, 50, 10, 50));
    statsPanel.setBackground(
        new Background(new BackgroundFill(StyleManager.GREY_BOX, CornerRadii.EMPTY, Insets.EMPTY)));
    statsPanel.setBorder(
        new Border(
            new BorderStroke(
                Paint.valueOf("black"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1))));

    Label wpmLabel = new Label();
    DoubleProperty wpmProperty =
        viewController.getPlayerWpmProperty(viewController.getCurrentPlayerId());
    wpmLabel.textProperty().bind(Bindings.format("%.2f WPM", wpmProperty));
    wpmLabel.setAlignment(Pos.CENTER_LEFT);

    Label accuracyLabel = new Label();
    accuracyLabel
        .textProperty()
        .bind(
            viewController
                .getPlayerAccuracyProperty(viewController.getCurrentPlayerId())
                .multiply(100)
                .asString("%.2f%% Accuracy"));
    accuracyLabel.setAlignment(Pos.CENTER);

    Label errorsLabel = new Label();
    IntegerProperty errorsProperty =
        viewController.getPlayerErrorsProperty(viewController.getCurrentPlayerId());
    errorsLabel.textProperty().bind(Bindings.format("Errors: %d", errorsProperty));
    errorsLabel.setAlignment(Pos.CENTER_RIGHT);

    ProgressBar progressBar = new ProgressBar();
    progressBar
        .progressProperty()
        .bind(viewController.getPlayerProgressProperty(viewController.getCurrentPlayerId()));
    progressBar.setPrefWidth(200);

    VBox wpmContainer = new VBox(wpmLabel);
    VBox accuracyContainer = new VBox(accuracyLabel);
    VBox progressBarContainer = new VBox(progressBar);
    VBox errorsContainer = new VBox(errorsLabel);

    statsPanel
        .getChildren()
        .addAll(wpmContainer, accuracyContainer, progressBarContainer, errorsContainer);

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
    topPlayersLabel = new Label();
    topPlayersLabel.setAlignment(Pos.CENTER);
    topPlayersLabel.setFont(StyleManager.STANDARD_FONT);
    topPlayersPanel.getChildren().add(topPlayersLabel);
    getChildren().add(topPlayersPanel);
    topPlayersLabel
        .textProperty()
        .bind(createTopPlayersBinding(viewController.topPlayersProperty()));
    VBox.setMargin(topPlayersPanel, new Insets(10, 50, 10, 50));
  }

  /**
   * Creates a StringBinding to bind the top players list to a display label.
   *
   * @param topPlayers The list of top players.
   * @return A StringBinding representing the top players.
   */
  private StringBinding createTopPlayersBinding(ListProperty<String> topPlayers) {
    return Bindings.createStringBinding(
        () -> {
          if (topPlayers.isEmpty()) {
            return "Top players: None";
          } else {
            return "Top players: " + topPlayers.stream().collect(Collectors.joining(", "));
          }
        },
        topPlayers);
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
   * Called when the view is shown. Updates the username label with the current username from the
   * view controller.
   */
  public void onViewShown() {
    if (usernameLabel != null) {
      usernameLabel.setText(viewController.getUsername());
    }
  }
}
