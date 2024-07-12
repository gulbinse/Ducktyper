package typeracer.client.view;

import java.util.Objects;
import java.util.stream.Collectors;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import typeracer.client.ViewController;

/**
 * Represents the game user interface for the TypeRacer game. This class sets up the GUI elements
 * that display the game UI, including the typing area, statistics, and top players.
 */
public class GameUi extends VBox {

  /** The controller to manage views and handle interactions. */
  private ViewController viewController;

  /** The text area where the typing text is displayed. */
  private TextFlow displayText;

  private SimpleStringProperty displayTextProperty;

  /** The text field where the user inputs their typing. */
  private TextField inputText;

  Text uncopiedGameText = new Text();
  Text expectedCharacter = new Text();
  Text copiedGameText = new Text();

  /** The label displaying the top players. */
  private Label topPlayersLabel;

  /** The label that displays the username of the current user. */
  private Label usernameLabel;

  private int currentCharIndex = 0;

  private String gameText;

  private ImageView gooseImage;

  private final VBox playersPanel = new VBox();

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
    //addHeaderImage();
    addUserLabel();
    addDisplayPanel();
    addInputPanel();
    addStatsPanel();
    addPlayersPanel();
    // addGooseAnimation();
    // addTopPlayersPanel();
    addButtonPanel();
  }

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
    uncopiedGameText.setFill(Color.BLACK);
    expectedCharacter.setFill(Color.RED);
    copiedGameText.setFill(Color.WHITE);
    displayText = new TextFlow();
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
   * <p>
   * Sets the uncopied game text to the game text obtained from the view controller.
   */
  private void finaliseDisplayText(){
    uncopiedGameText.setText(viewController.getGameText());
  }

  /**
   * Updates the display text based on whether the typed character is correct.
   * <p>
   * If the typed character is correct, it moves the character from the uncopied text to the
   * copied text.
   * If the typed character is incorrect, it highlights the expected character in red.
   *
   * @param correctChar true if the typed character is correct, false otherwise.
   */
  public void updateDisplayText(boolean correctChar) {
    Platform.runLater(() -> {
      String uncopiedText = uncopiedGameText.getText();
      String copiedText = copiedGameText.getText();
      String expectedChar;

      if (Objects.equals(expectedCharacter.getText(), "")){
        expectedChar = String.valueOf(uncopiedText.charAt(0));
      }
      else{
        expectedChar = expectedCharacter.getText();
      }
      if(correctChar) {
        copiedText = copiedText + expectedChar;
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

    inputText.setOnKeyTyped(
            event -> handleTyping(event.getCharacter().charAt(0)));

    VBox panel = new VBox();
    panel.setMaxHeight(350);
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(10, 50, 10, 50));
    panel.getChildren().add(inputText);

    getChildren().add(panel);
  }

  private void handleTyping(char typedCharacter) {
    viewController.handleCharacterTyped(typedCharacter);
    // TODO: Display wrong or correct character maybe by color
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
    DoubleProperty wpmProperty = viewController.getPlayerWpmProperty(viewController.getPlayerId());
    wpmLabel.textProperty().bind(Bindings.format("%.2f WPM", wpmProperty));
    wpmLabel.setAlignment(Pos.CENTER_LEFT);

    Label accuracyLabel = new Label();
    accuracyLabel
        .textProperty()
        .bind(
            viewController
                .getPlayerAccuracyProperty(viewController.getPlayerId())
                .multiply(100)
                .asString("%.2f%% Accuracy"));
    accuracyLabel.setAlignment(Pos.CENTER);

    Label errorsLabel = new Label();

    ProgressBar progressBar = new ProgressBar();
    progressBar
        .progressProperty()
        .bind(viewController.getPlayerProgressProperty(viewController.getPlayerId()));
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

  private void addGooseAnimation() {
    Image gooseImg = new Image(getClass().getResourceAsStream("/images/gooseanimation.gif"));
    gooseImage = new ImageView(gooseImg);
    gooseImage.setVisible(false);
    gooseImage.setFitHeight(50);
    gooseImage.setPreserveRatio(true);
    getChildren().add(gooseImage);
  }

  public void addPlayer(int playerId) {
    getChildren().add(new Button("Test"));
    Rectangle racetrack = new Rectangle();
    ImageView racer =
        new ImageView(new Image(getClass().getResourceAsStream("/images/gooseanimation.gif")));
    racer.setFitHeight(50);

    DoubleProperty progressProperty = viewController.getPlayerProgressProperty(playerId);

    racer
        .xProperty()
        .bind(
            Bindings.createDoubleBinding(
                () -> racetrack.getWidth() * progressProperty.getValue() + racetrack.getX(),
                progressProperty));

    Label wpmLabel = new Label();
    DoubleProperty wpmProperty = viewController.getPlayerWpmProperty(playerId);
    StringBinding wpmBinding = Bindings.createStringBinding(() -> wpmProperty.getValue() + " WPM", wpmProperty); // TODO: runden
    wpmLabel.textProperty().bind(wpmBinding);

    Label accuracyLabel = new Label();
    DoubleProperty accuracyProperty = viewController.getPlayerAccuracyProperty(playerId);
    StringBinding accuracyBinding = Bindings.createStringBinding(() -> accuracyProperty.getValue() * 100 + "% Accuracy", accuracyProperty); // TODO: runden
    accuracyLabel
        .textProperty()
        .bind(accuracyBinding);

    Label errorsLabel = new Label();
    Label usernameLabel = new Label(viewController.getUsernameById(playerId));

    HBox stats = new HBox(usernameLabel, wpmLabel, accuracyLabel, errorsLabel);

    HBox playerDisplay = new HBox(new StackPane(racetrack/*, racer*/), stats);

    getChildren().add(playerDisplay);
  }

  private void startGooseAnimation(int trackLength) {
    gooseImage.setVisible(true);
    double startX = -gooseImage.getBoundsInLocal().getWidth();
    double endX = trackLength;

    TranslateTransition transition = new TranslateTransition(Duration.seconds(5), gooseImage);
    transition.setFromX(startX);
    transition.setToX(endX);
    transition.setOnFinished(event -> gooseImage.setVisible(false));
    transition.play();
  }

  private void checkAndStartGooseAnimation() {
    //    int maxPlayers = viewController.getMaxPlayers();
    //    if (maxPlayers > 3) {
    //      startGooseAnimation((int) inputText.getWidth());
    //    }
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
    //    topPlayersLabel
    //        .textProperty()
    //        .bind(createTopPlayersBinding(viewController.topPlayersProperty()));
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
              }
            });
    viewController.leaveSessionOrGame();
  }

  /**
   * Called when the view is shown. Updates the username label with the current username from the
   * view controller.
   */
  public void onViewShown() {
    if (usernameLabel != null) {
      usernameLabel.setText(viewController.getUsername());
      finaliseDisplayText();
      // checkAndStartGooseAnimation();
    }
  }
}
