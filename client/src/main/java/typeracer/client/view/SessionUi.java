package typeracer.client.view;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import typeracer.client.ViewController;

/**
 * This class represents the UI for the game session view. It includes components such as player
 * list, mode selection dropdown, and buttons to ready up or go back to the main menu.
 */
public class SessionUi extends VBox {

  private final Map<Integer, HBox> playerLabelById = new HashMap<>();
  private VBox playerList;
  private Button readyButton;
  private Button backButton;
  private ViewController viewController;
  private Label usernameLabel;
  private Label sessionIdLabel;

  private SessionUi(ViewController viewController) {
    this.viewController = viewController;
  }

  /**
   * Creates and initializes a new SessionUi instance. This method constructs a new SessionUi,
   * initializes its UI components, updates the username label, and returns the initialized
   * instance.
   *
   * @param viewController the controller to manage views and handle interactions.
   * @return the initialized SessionUi instance.
   */
  public static SessionUi create(ViewController viewController) {
    SessionUi sessionUi = new SessionUi(viewController);
    sessionUi.initializeUi();
    sessionUi.updateUsernameLabel();
    return sessionUi;
  }

  /**
   * Updates the username label based on the current user's username. If the username is not set or
   * is empty, the label will display a default message.
   */
  private void updateUsernameLabel() {
    String username = viewController.getUsername();
    if (username != null && !username.isEmpty()) {
      usernameLabel.setText(username);
    } else {
      usernameLabel.setText("No username set");
    }
  }

  /** Initializes the UI components and layout. */
  private void initializeUi() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(20);
    this.setPadding(new Insets(15));
    this.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    Image titleImage = new Image(getClass().getResourceAsStream("/images/title.png"));
    ImageView titleImageView = new ImageView(titleImage);
    titleImageView.setFitWidth(350);
    titleImageView.setPreserveRatio(true);

    usernameLabel = new Label();
    usernameLabel.setFont(StyleManager.BOLD_FONT);

    sessionIdLabel = new Label("Session ID: Not set");
    sessionIdLabel.setFont(StyleManager.BOLD_FONT);

    Label copiedSessionId = new Label("Copied Session ID to clipboard.");
    copiedSessionId.setOpacity(0);
    Button copySessionIdButton =
        StyleManager.createStyledButton(
            "Copy Session ID", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    copySessionIdButton.setPrefWidth(150);
    copySessionIdButton.setMaxWidth(150);
    copySessionIdButton.setOnAction(
        e -> {
          Clipboard clipboard = Clipboard.getSystemClipboard();
          ClipboardContent content = new ClipboardContent();
          content.putString(Integer.toString(viewController.getSessionId()));
          clipboard.setContent(content);
          FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), copiedSessionId);
          fadeIn.setFromValue(0.0);
          fadeIn.setToValue(1.0);
          copiedSessionId.setText("Copied Session ID to clipboard.");
          copiedSessionId.setOpacity(1);
          fadeIn.setCycleCount(2);
          fadeIn.setAutoReverse(true);
          fadeIn.play();
          copiedSessionId.setOpacity(0);
        });

    playerList = new VBox();
    playerList.setPrefHeight(180);
    playerList.setBackground(
        new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

    readyButton =
        StyleManager.createStyledButton(
            "ready", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    backButton =
        StyleManager.createStyledButton(
            "back", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);
    readyButton.setOnAction(e -> viewController.setPlayerReady());
    backButton.setOnAction(e -> viewController.leaveSessionOrGame());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, readyButton);

    VBox sessionIdArea = new VBox(10);
    sessionIdArea.setAlignment(Pos.CENTER);
    sessionIdArea.getChildren().addAll(sessionIdLabel, copySessionIdButton, copiedSessionId);

    this.getChildren().addAll(titleImageView, usernameLabel, sessionIdArea, playerList, buttonBox);
  }

  /**
   * Adds a label for a player to the player list.
   *
   * @param playerId the ID of the player to add.
   */
  public void addPlayerLabel(int playerId) {
    HBox label = new HBox(10);
    label.setAlignment(Pos.CENTER_LEFT);
    label.setPadding(new Insets(10, 10, 10, 10));

    Circle readyStatus = new Circle(5);
    BooleanProperty readyProperty = viewController.getPlayerReadyProperty(playerId);
    readyStatus
        .fillProperty()
        .bind(
            Bindings.createObjectBinding(
                () -> {
                  boolean ready = readyProperty.getValue();
                  return ready ? Color.GREEN : Color.RED;
                },
                readyProperty));

    Text name = new Text(viewController.getUsernameById(playerId));

    label.getChildren().addAll(readyStatus, name);

    playerList.getChildren().add(label);
    playerLabelById.put(playerId, label);

    repaintPlayerListBackgrounds();
  }

  /**
   * Removes the player label for the specified player ID.
   *
   * @param playerId the ID of the player whose label is to be removed.
   */
  public void removePlayerLabel(int playerId) {
    HBox label = playerLabelById.getOrDefault(playerId, null);
    playerList.getChildren().remove(label);
    playerLabelById.remove(playerId);
    repaintPlayerListBackgrounds();
  }

  private void repaintPlayerListBackgrounds() {
    for (int i = 0; i < playerList.getChildren().size(); i++) {
      Node node = playerList.getChildren().get(i);
      if (node instanceof HBox playerLabel) {
        playerLabel.setBackground(
            new Background(
                new BackgroundFill(
                    i % 2 == 0 ? Color.LIGHTGRAY : StyleManager.GREY_BOX,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
      }
    }
  }

  // Simulated method to fetch game mode for a given session
  private String fetchGameModeForSession(String sessionId) {
    // Example hardcoded response
    return "Duo"; // Example game mode based on sessionId
  }

  /** Called when the view is shown to the user. Sets the username label. */
  public void onViewShown() {
    updateUsernameLabel();
    sessionIdLabel.setText("Session ID: " + viewController.getSessionId());
  }
}
