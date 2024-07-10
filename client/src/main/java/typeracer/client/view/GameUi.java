package typeracer.client.view;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import typeracer.client.ClientSideSessionData;
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

  /** The label displaying the top players. */
  private Label topPlayersLabel;

  /** The label that displays the username of the current user. */
  private Label usernameLabel;

  private int currentCharIndex = 0;

  private String gameText;

  private ImageView gooseImage;

  private Label feedbackLabel;

  private ClientSideSessionData playerData;

  private int totalCharsTyped = 0;
  private int correctCharsTyped = 0;
  private long startTime;

  /**
   * Constructs a new GameUi and initializes its user interface.
   *
   * @param viewController The controller to manage views and handle interactions.
   */
  public GameUi(ViewController viewController) {
    this.playerData = new ClientSideSessionData();
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
    addFeedbackLabel();
    addStatsPanel();
    addGooseAnimation();
    addTopPlayersPanel();
    addButtonPanel();
  }

  /** Adds a label to display the username. The label is styled and positioned within the UI. */
  private void addUserLabel() {
    String username = playerData.getUsername();

    if (username == null || username.isEmpty()) {
      username = "Default User";
    }
    usernameLabel = new Label(username);
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
    displayText.setText(playerData.getGameText());
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
    inputText.setOnKeyTyped(
        event ->
            handleTyping(
                event.getCharacter().charAt(0))); // TODO: eine Methode um char zurückzugeben
    panel.getChildren().add(inputText);
    getChildren().add(panel);
  }

  /** Adds the feedback label to the UI */
  private void addFeedbackLabel() {
    feedbackLabel = new Label();
    feedbackLabel.setTextFill(Color.RED);
    feedbackLabel.setFont(StyleManager.STANDARD_FONT);
    feedbackLabel.setAlignment(Pos.CENTER);
    feedbackLabel.setPadding(new Insets(5, 0, 5, 0));
    getChildren().add(feedbackLabel);
  }

  /**
   * Method to display feedback about the typing accuracy.
   *
   * @param isCorrect Whether the typed character was correct.
   */
  public void displayTypingFeedback(boolean isCorrect) {
    if (isCorrect) {
      feedbackLabel.setText("Correct!");
      feedbackLabel.setTextFill(Color.GREEN);
    } else {
      feedbackLabel.setText("Incorrect!");
      feedbackLabel.setTextFill(Color.RED);
    }
  }

  private void handleTyping(char typedCharacter) {
    if (currentCharIndex >= gameText.length()) {
      return;
    }

    if (totalCharsTyped == 0) {
      startTime = System.currentTimeMillis();
    }
    totalCharsTyped++;
    char expectedCharacter = getCurrentExpectedCharacter();

    if (typedCharacter == expectedCharacter) {
      currentCharIndex++;
      correctCharsTyped++;
      updateProgress();
      updateWPM();
      updateAccuracy();
      displayTypingFeedback(true);
    } else {
      displayTypingFeedback(false);
    }
    updateErrors(typedCharacter != expectedCharacter);
  }

  private void updateAccuracy() {
    if (totalCharsTyped == 0) return;
    double accuracy = ((double) correctCharsTyped / totalCharsTyped) * 100;
    DoubleProperty accProp = playerData.getPlayerAccuracies().get(playerData.getId());
    if (accProp != null) {
      accProp.set(accuracy);
    } else {
      playerData.getPlayerAccuracies().put(playerData.getId(), new SimpleDoubleProperty(accuracy));
    }
  }

  private void updateWPM() {
    long timeSpent = (System.currentTimeMillis() - startTime) / 1000;
    if (timeSpent == 0) return;
    int wordsTyped = currentCharIndex / 5;
    double wpm = (wordsTyped / (double) timeSpent) * 60;
    IntegerProperty wpmProp = playerData.getPlayerWpms().get(playerData.getId());
    if (wpmProp != null) {
      wpmProp.set((int) wpm);
    } else {
      playerData.getPlayerWpms().put(playerData.getId(), new SimpleIntegerProperty((int) wpm));
    }
  }

  private void updateProgress() {
    if (gameText == null || gameText.isEmpty()) {
      return;
    }
    double progress = (double) currentCharIndex / gameText.length();
    DoubleProperty progressProp = playerData.getPlayerProgresses().get(playerData.getId());
    if (progressProp != null) {
      progressProp.set(progress);
    } else {
      playerData.getPlayerProgresses().put(playerData.getId(), new SimpleDoubleProperty(progress));
    }
  }

  private void updateErrors(boolean hasError) {
    if (!hasError) {
      return;
    }
    IntegerProperty errorsProp = playerData.getPlayerErrors().get(playerData.getId());
    if (errorsProp != null) {
      errorsProp.set(errorsProp.get() + 1);
    } else {
      playerData.getPlayerErrors().put(playerData.getId(), new SimpleIntegerProperty(1));
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

    int currentPlayerId = playerData.getId();

    Label wpmLabel = new Label();
    SimpleIntegerProperty wpmProperty = playerData.getPlayerWpms().get(currentPlayerId);
    if (wpmProperty != null) {
      wpmLabel.textProperty().bind(Bindings.format("%.2f WPM", wpmProperty));
    } else {
      wpmLabel.setText("WPM: N/A");
    }
    wpmLabel.setAlignment(Pos.CENTER_LEFT);

    Label accuracyLabel = new Label();
    DoubleProperty accuracyProperty = playerData.getPlayerAccuracies().get(currentPlayerId);
    if (accuracyProperty != null) {
      accuracyLabel
          .textProperty()
          .bind(
              Bindings.createStringBinding(
                  () -> String.format("%.2f%% Accuracy", accuracyProperty.get() * 100),
                  accuracyProperty));
    } else {
      accuracyLabel.setText("Accuracy: N/A");
    }
    accuracyLabel.setAlignment(Pos.CENTER);

    Label errorsLabel = new Label();
    IntegerProperty errorsProperty = playerData.getPlayerErrors().get(currentPlayerId);
    if (errorsProperty != null) {
      errorsLabel.textProperty().bind(Bindings.format("Errors: %d", errorsProperty));
    } else {
      errorsLabel.setText("Errors: N/A");
    }
    errorsLabel.setAlignment(Pos.CENTER_RIGHT);

    ProgressBar progressBar = new ProgressBar();
    DoubleProperty progressProperty = playerData.getPlayerProgresses().get(currentPlayerId);
    if (progressProperty != null) {
      progressBar.progressProperty().bind(progressProperty);
    } else {
      progressBar.setProgress(0);
    }
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
    topPlayersLabel.textProperty().bind(createTopPlayersBinding(playerData.topPlayersProperty()));
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
            return "Top players: " + String.join(", ", topPlayers.get());
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
    String currentUsername = playerData.getUsername();
    if (currentUsername != null && !currentUsername.isEmpty()) {
      usernameLabel.setText(currentUsername);
    } else {
      usernameLabel.setText("Default User");
      // checkAndStartGooseAnimation();
    }
  }
}
