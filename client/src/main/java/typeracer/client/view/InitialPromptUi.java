package typeracer.client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Represents the initial prompt user interface for the TypeRacer game. This class sets up the GUI
 * elements that prompt the user to enter their username.
 */
public class InitialPromptUi extends BorderPane {
  /** The text field for entering the username. */
  private TextField usernameField;

  /** The button to submit the username. */
  private Button submitButton;

  /**
   * Constructs a new InitialPromptUi and initializes its user interface.
   *
   * @param stage The stage on which this UI will be displayed.
   */
  public InitialPromptUi(Stage stage) {
    setBackground(
        new Background(
            new BackgroundFill(StyleManager.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    setIconImage(stage);
    stage.setResizable(true);
    this.setTop(createImagePanel());
    this.setCenter(createInputPanel());
  }

  /**
   * Sets the icon image of the Stage to a typewriter image.
   *
   * @param stage The Stage whose icon will be set.
   */
  private void setIconImage(Stage stage) {
    Image img = new Image(getClass().getResourceAsStream("/images/typewriter.jpeg"));
    stage.getIcons().add(img);
  }

  /**
   * Creates and returns a panel containing an image. This panel is used as the top section of the
   * BorderPane.
   *
   * @return A VBox containing the image.
   */
  private VBox createImagePanel() {
    VBox imagePanel = new VBox();
    imagePanel.setAlignment(Pos.CENTER);
    imagePanel.setBackground(
        new Background(
            new BackgroundFill(StyleManager.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    Image image = new Image(getClass().getResourceAsStream("/images/typewriter.jpeg"));
    ImageView imageView = new ImageView(image);

    StyleManager.applyFadeInAnimation(imageView, 1500);

    imagePanel.getChildren().add(imageView);
    return imagePanel;
  }

  /**
   * Creates and returns a panel for user input. This panel includes a label, a text field for
   * entering the username, and a submit button.
   *
   * @return A VBox containing the input elements.
   */
  private VBox createInputPanel() {
    VBox centerPanel = new VBox(10);
    centerPanel.setAlignment(Pos.CENTER);
    centerPanel.setPadding(new Insets(10));
    centerPanel.setBackground(
        new Background(
            new BackgroundFill(StyleManager.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

    Label label = new Label("Enter your username");
    label.setFont(StyleManager.BOLD_FONT);

    StyleManager.applyFadeInAnimation(label, 1500);

    centerPanel.getChildren().add(label);

    usernameField = new TextField();
    usernameField.setMaxWidth(200);

    usernameField.setStyle("-fx-background-color: white;");
    usernameField
        .focusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                usernameField.setStyle("-fx-background-color: lightblue;");
              } else {
                usernameField.setStyle("-fx-background-color: white;");
              }
            });

    StyleManager.applyFadeInAnimation(usernameField, 1500);

    centerPanel.getChildren().add(usernameField);

    submitButton =
        StyleManager.createStyledButton(
            "submit", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);

    StyleManager.applyButtonHoverAnimation(submitButton);

    StyleManager.applyFadeInAnimation(submitButton, 1500);

    submitButton.setOnAction(e -> submitAction());

    centerPanel.getChildren().add(submitButton);

    return centerPanel;
  }

  /**
   * Handles the submit button action. Retrieves the username from the text field and switches to
   * the main menu UI.
   */
  private void submitAction() {
    String username = usernameField.getText();
    System.out.println("Username: " + username);

    Stage stage = (Stage) this.getScene().getWindow();
    stage.setResizable(true);

    MainMenuUi mainMenuUi = new MainMenuUi();

    Scene mainMenuScene = new Scene(mainMenuUi, stage.getWidth(), stage.getHeight());
    StyleManager.switchToScene(stage, mainMenuScene);
  }
}
