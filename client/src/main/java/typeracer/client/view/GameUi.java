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
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Represents the game user interface for the TypeRacer game. This class sets up the GUI elements
 * that display the game UI, including the typing area, statistics, and top players.
 */
public class GameUi extends VBox {

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

  /** Constructs a GameUi pane. Initializes the layout with specified spacing. */
  public GameUi() {
    setSpacing(10);
    setAlignment(Pos.TOP_CENTER);
    setBackground(
        new Background(
            new BackgroundFill(StyleManager.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    initUi();
  }

  /** Initializes the UI elements of the GameUi pane. */
  private void initUi() {
    Label headerLabel = createHeaderLabel();
    VBox displayPanel = createDisplayPanel();
    VBox inputPanel = createInputPanel();
    HBox statsPanel = createStatsPanel();
    VBox topPlayersPanel = createTopPlayersPanel();
    FlowPane buttonPanel = createButtonPanel();

    getChildren()
        .addAll(headerLabel, displayPanel, inputPanel, statsPanel, topPlayersPanel, buttonPanel);

    StyleManager.applyFadeInAnimation(headerLabel, 1000);
    StyleManager.applyFadeInAnimation(displayPanel, 1200);
    StyleManager.applyFadeInAnimation(inputPanel, 1400);
    StyleManager.applyFadeInAnimation(statsPanel, 1600);
    StyleManager.applyFadeInAnimation(topPlayersPanel, 1800);
    StyleManager.applyFadeInAnimation(buttonPanel, 2000);

    StyleManager.applyButtonHoverAnimation(buttonPanel.getChildren().toArray(new Button[0]));
  }

  /**
   * Creates and returns the header label for the game UI.
   *
   * @return A Label containing the header text.
   */
  private Label createHeaderLabel() {
    Label headerLabel = new Label("Typeracer");
    headerLabel.setFont(StyleManager.BOLD_FONT);
    headerLabel.setAlignment(Pos.CENTER);
    return headerLabel;
  }

  /**
   * Creates and returns the display panel for the game UI. This panel displays the text that users
   * need to type, formatted to be large and clear.
   *
   * @return A VBox containing the display text area with increased size and appropriate margins.
   */
  private VBox createDisplayPanel() {
    VBox panel = new VBox();
    panel.setMaxHeight(350);
    panel.setAlignment(Pos.CENTER);

    displayText = new TextArea("Typing text will appear here");
    displayText.setEditable(false);
    displayText.setWrapText(true);
    displayText.setPrefHeight(150);
    displayText.setMaxWidth(Double.MAX_VALUE);
    displayText.setPadding(new Insets(10));

    displayText.setStyle("-fx-alignment: top-left;");

    panel.getChildren().add(displayText);

    VBox.setMargin(panel, new Insets(10, 50, 10, 50));

    return panel;
  }

  /**
   * Creates and returns the input panel for the game UI. This panel includes a single, larger text
   * field where users can type their input, with text starting from the top-left corner.
   *
   * @return A VBox containing the input text field with increased size and appropriate margins.
   */
  private VBox createInputPanel() {
    VBox panel = new VBox();
    panel.setMaxHeight(350);
    panel.setAlignment(Pos.CENTER);

    inputText = new TextField();
    inputText.setPrefHeight(150);
    inputText.setMaxWidth(Double.MAX_VALUE);
    inputText.setPadding(new Insets(10));

    inputText.setStyle("-fx-alignment: top-left;");

    panel.getChildren().add(inputText);

    VBox.setMargin(panel, new Insets(10, 50, 10, 50));

    return panel;
  }

  /**
   * Creates and returns the statistics panel for the game UI. This panel displays the WPM and
   * Errors in a stylized box, aligning them with equal distance from the edges of the box, and adds
   * external margins to separate it from surrounding content.
   *
   * @return A HBox containing the statistics labels with specified alignments, spacing, and
   *     external margins.
   */
  private HBox createStatsPanel() {
    HBox statsPanel = new HBox();
    statsPanel.setAlignment(Pos.CENTER);
    statsPanel.setSpacing(10);
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

    return statsPanel;
  }

  /**
   * Creates and returns the top players panel for the game UI, styled similarly to the statistics
   * panel. This panel displays the top players centered within a styled box.
   *
   * @return A VBox containing the top players label, centered within a styled background.
   */
  private VBox createTopPlayersPanel() {
    VBox topPlayersPanel = new VBox();
    topPlayersPanel.setAlignment(Pos.CENTER);
    topPlayersPanel.setMaxHeight(30);

    topPlayersPanel.setBackground(
        new Background(
            new BackgroundFill(StyleManager.GREY_BOX, new CornerRadii(5), Insets.EMPTY)));
    topPlayersPanel.setBorder(
        new Border(
            new BorderStroke(
                Paint.valueOf("black"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1))));
    topPlayersPanel.setPadding(new Insets(10, 50, 10, 50));

    topPlayersLabel = new Label("Top players: 1. A, 2. B, 3. C");
    topPlayersLabel.setAlignment(Pos.CENTER);
    topPlayersLabel.setFont(StyleManager.STANDARD_FONT);

    topPlayersPanel.getChildren().add(topPlayersLabel);

    VBox.setMargin(topPlayersPanel, new Insets(10, 50, 10, 50));

    return topPlayersPanel;
  }

  /**
   * Creates and returns the button panel for the game UI.
   *
   * @return A FlowPane containing the exit button.
   */
  private FlowPane createButtonPanel() {
    FlowPane buttonPanel = new FlowPane();
    buttonPanel.setHgap(10);
    buttonPanel.setAlignment(Pos.CENTER);

    Button exitButton =
        StyleManager.createStyledButton(
            "exit", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);
    exitButton.setOnAction(e -> attemptToExitGame());
    buttonPanel.getChildren().add(exitButton);

    return buttonPanel;
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
    alert.setHeaderText(null);

    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
    Image img = new Image(getClass().getResourceAsStream("/images/typewriter.jpeg"));
    alertStage.getIcons().add(img);

    alert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.YES) {
                StyleManager.switchToGameResultUi((Stage) getScene().getWindow());
              }
            });
  }
}
