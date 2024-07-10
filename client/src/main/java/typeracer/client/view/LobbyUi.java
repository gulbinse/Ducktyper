package typeracer.client.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import typeracer.client.ClientSideSessionData;
import typeracer.client.ViewController;

/**
 * This class represents the UI for the game lobby. It includes components such as player list, mode
 * selection dropdown, and buttons to ready up or go back to the main menu.
 */
public class LobbyUi extends VBox {
  private ListView<String> playerList;
  private Button readyButton;
  private Button backButton;
  private ViewController viewController;
  private Label usernameLabel;
  private boolean isReady = false;
  private ClientSideSessionData playerData;

  /**
   * Constructor to initialize the Lobby UI.
   *
   * @param viewController the controller to manage views.
   */
  public LobbyUi(ViewController viewController) {
    this.viewController = viewController;
    this.playerData = new ClientSideSessionData();
    initializeUi();
    populatePlayerList();
  }

  /**
   * Populates the player list with names retrieved from client-side session data. The list includes
   * all player names, with the active user's name prepended by " - Active".
   */
  private void populatePlayerList() {
    ObservableList<String> playerNames = FXCollections.observableArrayList();
    playerData.getPlayerNameById().forEach((id, name) -> {
      String status = playerData.getPlayerReady().get(id).get() ? "Ready" : "Not Ready";
      playerNames.add(name + " - " + status);
    });
    playerList.setItems(playerNames);
  }

  /** Initializes the UI components and layout. */
  private void initializeUi() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(20);
    this.setPadding(new Insets(45));
    this.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    ImageView titleImageView =
        new ImageView(new Image(getClass().getResourceAsStream("/images/title.png")));
    titleImageView.setFitWidth(350);
    titleImageView.setPreserveRatio(true);

    usernameLabel = new Label();
    usernameLabel.setFont(StyleManager.BOLD_FONT);

    playerList = new ListView<>();
    playerList.setPrefHeight(200);
    customizePlayerList();

    readyButton =
        StyleManager.createStyledButton(
            "ready", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    backButton =
        StyleManager.createStyledButton(
            "back", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);
    readyButton.setOnAction(e -> toggleReadyState());
    backButton.setOnAction(e -> ViewController.switchToMainMenu());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, readyButton);

    this.getChildren().addAll(titleImageView, usernameLabel, playerList, buttonBox);
  }

  /**
   * Toggles the player's ready state and updates the UI accordingly. This method inverts the
   * current ready state, updates the view controller with the new state, repopulates the player
   * list, and switches the UI to the game view.
   */
  private void toggleReadyState() {
    isReady = !isReady;
    playerData.getPlayerReady().put(playerData.getId(), new SimpleBooleanProperty(isReady));
    viewController.setPlayerReady(isReady);
    if (allPlayersReady()) {
      viewController.switchToGameUi();
    }
    populatePlayerList();
  }

  /**
   * Checks if all players are ready.
   *
   * @return {@code true} if all players are ready, {@code false} otherwise.
   */

  private boolean allPlayersReady() {
    return playerData.getPlayerReady().values().stream().allMatch(SimpleBooleanProperty::get);
  }

  /**
   * Customizes the player list to display player names with a status indicator. Each list cell
   * contains a circle representing the player's status (green for active, gray for inactive) and a
   * label with the player's name.
   */
  private void customizePlayerList() {
    playerList.setCellFactory(
        lv ->
            new ListCell<String>() {
              private final Circle statusCircle = new Circle(5);
              private final Label nameLabel = new Label();
              private final HBox cellLayout = new HBox(10, statusCircle, nameLabel);

              @Override
              protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setGraphic(null);
                } else {
                  nameLabel.setText(item.split(" - ")[0]);
                  String status = item.contains("Active") ? "Active" : "Inactive";
                  statusCircle.setFill("Active".equals(status) ? Color.GREEN : Color.GRAY);
                  setGraphic(cellLayout);
                }
              }
            });
  }

  /** Called in ViewController when the view is shown to the user. Sets the username label. */
  public void onViewShown() {
    populatePlayerList();
    usernameLabel.setText(playerData.getUsername());
    playerList.refresh();
  }
}
