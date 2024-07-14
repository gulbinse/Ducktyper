package typeracer.client.view;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import typeracer.client.ViewController;

/**
 * Represents the game user interface for the TypeRacer game. This class sets up the GUI elements
 * that display the game UI, including the typing area, statistics, and top players.
 */
public class GameUi extends VBox {

  /** The controller to manage views and handle interactions. */
  private final ViewController viewController;

  /** The text area where the typing text is displayed. */
  private TextFlow displayText;

  Text uncopiedGameText = new Text();
  Text expectedCharacter = new Text();
  Text copiedGameText = new Text();

  Color colorOfUncopiedGame = Color.BLACK;
  Color colorOfExpectedCharacter = Color.RED;
  Color colorOfCopiedGame = Color.WHITE;

  private static String TEXT_SIZE = "-fx-font-size: 16px;";

  private Label usernameLabel;
  private final VBox playersPanel = new VBox();
  private final Map<Integer, VBox> playerDisplayById = new HashMap<>();

  private GameUi(ViewController viewController) {
    this.viewController = viewController;
  }

  /**
   * Creates a new GameUi and initializes its user interface.
   *
   * @param viewController The controller to manage views and handle interactions.
   */
  public static GameUi create(ViewController viewController) {
    GameUi gameUi = new GameUi(viewController);
    gameUi.initUi();
    return gameUi;
  }

  /** Initializes the UI elements of the GameUi pane. */
  private void initUi() {
    setSpacing(10);
    setAlignment(Pos.TOP_CENTER);
    setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));
    addUserLabel();
    addDisplayPanel();
    addInputPanel();
    addPlayersPanel();
    addButtonPanel();
  }

  /**
   * Called when the view is shown. Updates the username label with the current username from the
   * view controller.
   */
  public void onViewShown() {
    if (usernameLabel != null) {
      usernameLabel.setText(viewController.getUsername());
      finaliseDisplayText();
    }
  }

  /**
   * Adds the players panel to the game display.
   * This method sets the alignment of the players panel to the center and adds it to the
   * list of children in the current layout.
   */
  private void addPlayersPanel() {
    playersPanel.setAlignment(Pos.CENTER);
    getChildren().add(playersPanel);
  }

  /** Adds a label to display the username. The label is styled and positioned within the UI. */
  private void addUserLabel() {
    usernameLabel = new Label("Default User");
    usernameLabel.setFont(StyleManager.BOLD_FONT);
    usernameLabel.setPadding(new Insets(5, 10, 5, 10));
    usernameLabel.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));
    usernameLabel.setAlignment(Pos.CENTER);
    getChildren().add(usernameLabel);
  }

  /** Adds a display panel that contains a non-editable TextArea for displaying typing text. */
  private void addDisplayPanel() {
    uncopiedGameText.setFill(colorOfUncopiedGame);
    expectedCharacter.setFill(colorOfExpectedCharacter);
    copiedGameText.setFill(colorOfCopiedGame);
    uncopiedGameText.setStyle(TEXT_SIZE);expectedCharacter.setStyle(TEXT_SIZE);copiedGameText.setStyle(TEXT_SIZE);displayText = new TextFlow();
    displayText.setPrefHeight(150);
    displayText.setMaxWidth(Double.MAX_VALUE);
    displayText.getChildren().addAll(copiedGameText, expectedCharacter, uncopiedGameText);
    VBox panel = new VBox();
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(10, 50, 10, 50));
    panel.getChildren().add(displayText);
    getChildren().add(panel);
  }

  /**
   * Finalizes the display text by resetting the uncopied game text.
   * Sets the uncopied game text to the game text obtained from the view controller.
   */
  private void finaliseDisplayText() {
    uncopiedGameText.setText(viewController.getGameText());
    copiedGameText.setText("");
  }

  /**
   * Updates the display text based on whether the typed character is correct.
   * If the typed character is correct, it moves the character from the uncopied text to the
   * copied text. If the typed character is incorrect, it highlights the expected character in red.
   *
   * @param correctChar true if the typed character is correct, false otherwise.
   */
  public void updateDisplayText(boolean correctChar) {
    Platform.runLater(
        () -> {
          String uncopiedText = uncopiedGameText.getText();
          String copiedText = copiedGameText.getText();
          String expectedChar = expectedCharacter.getText();

          if (expectedChar.isEmpty()) {
            expectedChar = String.valueOf(uncopiedText.charAt(0));
          }

          if (correctChar) {
            copiedText += expectedChar;
            uncopiedText = uncopiedText.substring(1);
            copiedGameText.setText(copiedText);
            uncopiedGameText.setText(uncopiedText);
            expectedCharacter.setText("");
          }
        });
  }

  /**
   * Adds an input panel that contains a TextField for user input. The panel is styled and
   * positioned within the UI.
   */
  private void addInputPanel() {
    TextArea inputText = new TextArea();
    inputText.setWrapText(true);
    inputText.setPrefHeight(150);
    inputText.setMaxWidth(Double.MAX_VALUE);
    inputText.setPadding(new Insets(10));
    inputText.setStyle("-fx-font-size: 16px; -fx-alignment: top-left;");

    inputText.setOnKeyTyped(event -> handleTyping(event.getCharacter().charAt(0)));

    VBox panel = new VBox();
    panel.setMaxHeight(350);
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(10, 50, 10, 50));
    panel.getChildren().add(inputText);

    getChildren().add(panel);
  }

  /**
   * Handles the event when a character is typed.
   * This method passes the typed character to the view controller for further processing.
   *
   * @param typedCharacter the character that was typed.
   */
  private void handleTyping(char typedCharacter) {
    viewController.handleCharacterTyped(typedCharacter);
  }

  /**
   * Adds a new player to the game display.
   * This method creates and configures various UI components to represent a player's progress in
   * the game, including a racetrack, player statistics, and a racer's animation. The player's
   * progress and performance statistics are dynamically bound to properties provided by the view
   * controller.
   *
   * @param playerId the ID of the player to be added.
   */
  public void addPlayer(int playerId) {
    Rectangle raceBorder = new Rectangle(300, 60, Color.TRANSPARENT);
    raceBorder
        .widthProperty()
        .bind(
            Bindings.createDoubleBinding(
                () -> this.widthProperty().getValue() - 100, this.widthProperty()));
    raceBorder.setStroke(Color.WHITE);
    raceBorder.setStrokeWidth(4);
    raceBorder.getStrokeDashArray().addAll(8.0, 16.0);
    raceBorder.setArcHeight(20);
    raceBorder.setArcWidth(20);

    Line line = new Line();
    line.setStartX(0);
    line.endXProperty()
        .bind(
            Bindings.createDoubleBinding(
                () -> this.widthProperty().getValue() - 110, this.widthProperty()));
    line.getStrokeDashArray().addAll(4.0, 8.0);
    line.setStrokeWidth(2);
    line.setStroke(Color.WHITE);
    line.setFill(Color.RED);
    line.setTranslateY(-20);

    Line line2 = new Line();
    line2.setStartX(0);
    line2
        .endXProperty()
        .bind(
            Bindings.createDoubleBinding(
                () -> this.widthProperty().getValue() - 110, this.widthProperty()));
    line2.getStrokeDashArray().addAll(4.0, 8.0);
    line2.setStrokeWidth(2);
    line2.setStroke(Color.WHITE);
    line2.setFill(Color.RED);
    line2.setTranslateY(20);

    Rectangle racetrack = new Rectangle(300, 60, Color.RED);
    racetrack
        .widthProperty()
        .bind(
            Bindings.createDoubleBinding(
                () -> this.widthProperty().getValue() - 100, this.widthProperty()));
    racetrack.setFill(Color.DARKSLATEGRAY);
    racetrack.setStroke(Color.RED);
    racetrack.setStrokeWidth(4);
    racetrack.setStrokeDashOffset(12);
    racetrack.getStrokeDashArray().addAll(8.0, 16.0);
    racetrack.setArcHeight(20);
    racetrack.setArcWidth(20);

    ImageView racer =
        new ImageView(new Image(getClass().getResourceAsStream("/images/gooseanimation.gif")));
    racer.setPreserveRatio(true);
    racer.setFitWidth(50);

    DoubleProperty progressProperty = viewController.getPlayerProgressProperty(playerId);
    DoubleBinding progressBinding =
        Bindings.createDoubleBinding(
            () ->
                (racetrack.getWidth() - racer.getFitWidth()) * progressProperty.getValue()
                    + racetrack.getLayoutX(),
            progressProperty,
            this.widthProperty());
    racer.xProperty().bind(progressBinding);

    Label wpmLabel = new Label();
    DoubleProperty wpmProperty = viewController.getPlayerWpmProperty(playerId);
    StringBinding wpmBinding =
        Bindings.createStringBinding(
            () -> String.format("%.2f", wpmProperty.getValue()) + " WPM", wpmProperty);
    wpmLabel.textProperty().bind(wpmBinding);
    wpmLabel.setFont(StyleManager.BOLD_ITALIC_FONT);
    wpmLabel.setTextFill(StyleManager.GREY_BOX);

    Label accuracyLabel = new Label();
    DoubleProperty accuracyProperty = viewController.getPlayerAccuracyProperty(playerId);
    StringBinding accuracyBinding =
        Bindings.createStringBinding(
            () -> String.format("%.2f", accuracyProperty.getValue() * 100) + "% Accuracy",
            accuracyProperty);
    accuracyLabel.textProperty().bind(accuracyBinding);
    accuracyLabel.setFont(StyleManager.BOLD_ITALIC_FONT);
    accuracyLabel.setTextFill(StyleManager.GREY_BOX);

    Label errorsLabel = new Label();

    Label usernameLabel = new Label(viewController.getUsernameById(playerId));
    usernameLabel.setFont(StyleManager.BOLD_ITALIC_FONT);
    usernameLabel.setTextFill(StyleManager.GREY_BOX);
    usernameLabel.setPadding(new Insets(0, 10, 0, 10));

    HBox stats = new HBox(10);
    stats.getChildren().addAll(wpmLabel, accuracyLabel, errorsLabel);
    stats.setAlignment(Pos.CENTER);

    HBox banner = new HBox(20);
    banner.getChildren().addAll(usernameLabel, stats);
    banner.setAlignment(Pos.CENTER_LEFT);
    // banner.setBackground(new Background(new BackgroundFill(StyleManager.GREY_BOX,
    // CornerRadii.EMPTY, Insets.EMPTY)));

    VBox playerDisplay =
        new VBox(new Pane(new StackPane(racetrack, raceBorder, line, line2, banner), racer));
    playerDisplay.setPadding(new Insets(10, 50, 10, 50));
    playerDisplay.setAlignment(Pos.CENTER);
    HBox.setHgrow(racetrack, Priority.ALWAYS);

    getChildren().add(getChildren().size() - 1, playerDisplay);
    playerDisplayById.put(playerId, playerDisplay);
  }

  /**
   * Removes the player from the game display.
   * This method removes the UI components associated with the specified player ID from the game
   * display.
   *
   * @param playerId the ID of the player to be removed.
   */
  public void removePlayer(int playerId) {
    VBox playerDisplay = playerDisplayById.getOrDefault(playerId, null);
    getChildren().remove(playerDisplay);
    playerDisplayById.remove(playerId);
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
                viewController.leaveSessionOrGame();
              }
            });
  }
}
