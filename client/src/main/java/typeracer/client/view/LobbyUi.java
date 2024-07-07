package typeracer.client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import typeracer.client.ViewController;

/**
 * This class represents the UI for the game lobby. It includes components such as player list, mode
 * selection dropdown, and buttons to ready up or go back to the main menu.
 */
public class LobbyUi extends VBox {
  private ListView<String> playerList;
  private Button readyButton;
  private Button backButton;
  private ComboBox<String> modeDropdown;
  private ViewController viewController;
  private Label usernameLabel;

  /**
   * Constructor to initialize the Lobby UI.
   *
   * @param viewController the controller to manage views.
   */
  public LobbyUi(ViewController viewController) {
    this.viewController = viewController;
    initializeUi();
    updateUsernameLabel();
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

    playerList = new ListView<>();
    playerList.setPrefHeight(200);
    playerList.setItems(viewController.getPlayerUsernames());
    customizePlayerList();

    modeDropdown = new ComboBox<>();
    modeDropdown.getItems().addAll("Trial", "Duo", "Trio");
    modeDropdown.setPromptText("Select game mode");

    VBox modeSelection = new VBox(10);
    modeSelection.getChildren().addAll(modeDropdown);

    readyButton =
        StyleManager.createStyledButton(
            "ready", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    backButton =
        StyleManager.createStyledButton(
            "back", StyleManager.BLUE_BUTTON, StyleManager.STANDARD_FONT);
    readyButton.setOnAction(e -> ViewController.startNewGame());
    backButton.setOnAction(e -> ViewController.switchToMainMenu());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backButton, readyButton);

    this.getChildren().addAll(titleImageView, playerList, modeSelection, buttonBox);
  }

  /** Customizes the player list by setting a custom cell factory to display player statuses. */
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
                  String[] parts = item.split(" - ");
                  if (parts.length > 1) {
                    nameLabel.setText(parts[0]);
                    statusCircle.setFill("Active".equals(parts[1]) ? Color.GREEN : Color.RED);
                  } else {
                    nameLabel.setText(parts[0]);
                    statusCircle.setFill(Color.GRAY);
                  }
                  setGraphic(cellLayout);
                }
              }
            });
  }

  /** Called when the view is shown to the user. Sets the username label. */
  public void onViewShown() {
    updateUsernameLabel();
    playerList.refresh();
  }
}
