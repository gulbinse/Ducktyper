package typeracer.client.view;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import typeracer.client.ViewController;

/**
 * Represents the initial prompt user interface for the TypeRacer game. This class sets up the GUI
 * elements that prompt the user to enter their username.
 */
public class InitialPromptUi extends VBox {
  /** The text field for user to input their username. */
  private TextField usernameField;

  /** The controller to manage views and handle interactions. */
  private ViewController viewController;

  /** The primary stage of the application. */
  private Stage stage;

  /**
   * Constructs a new InitialPromptUi and initializes its user interface components.
   *
   * @param viewController The controller to manage views and handle interactions.
   * @param stage The primary stage of the application.
   */
  public InitialPromptUi(ViewController viewController, Stage stage) {
    this.viewController = viewController;
    this.stage = stage;
    this.setSpacing(10);
    this.setPadding(new Insets(10));
    this.setAlignment(Pos.CENTER);
    this.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    setIconImage(stage);

    VBox titlePanel = this.createImagePanel();
    this.getChildren().add(titlePanel);

    HBox inputPanel = this.createInputPanel();
    this.getChildren().add(inputPanel);
  }

  /**
   * Sets the icon image of the Stage to a typewriter image.
   *
   * @param stage The Stage whose icon will be set.
   */
  private void setIconImage(Stage stage) {
    Image img = new Image(getClass().getResourceAsStream("/images/duck.png"));
    stage.getIcons().add(img);
  }

  /**
   * Creates and returns a panel containing an image. This panel is used as the top section of the
   * BorderPane.
   *
   * @return A VBox containing the image.
   */
  private VBox createImagePanel() {
    VBox imagePanel = new VBox(5);
    imagePanel.setAlignment(Pos.CENTER);

    Image titleImage = new Image(getClass().getResourceAsStream("/images/title.png"));
    ImageView titleImageView = new ImageView(titleImage);
    titleImageView.setFitWidth(350);
    titleImageView.setPreserveRatio(true);
    imagePanel.getChildren().add(titleImageView);

    Image duckImage = new Image(getClass().getResourceAsStream("/images/duck.png"));
    ImageView duckImageView = new ImageView(duckImage);
    duckImageView.setFitWidth(400);
    duckImageView.setPreserveRatio(true);
    imagePanel.getChildren().add(duckImageView);

    applyFadeInAnimation(duckImageView);
    applyContinuousRotateAnimation(duckImageView);

    return imagePanel;
  }

  /**
   * Applies a continuous rotate animation to the given ImageView. The animation rotates the
   * ImageView by 360 degrees over 2 seconds and repeats this 3 times.
   *
   * @param imageView The ImageView to apply the rotate animation to.
   */
  private void applyContinuousRotateAnimation(ImageView imageView) {
    FadeTransition fade = new FadeTransition(Duration.seconds(2), imageView);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.play();
  }

  /**
   * Applies a fade-in animation to the given ImageView. The animation fades the ImageView from
   * fully transparent to fully opaque over 2 seconds.
   *
   * @param imageView The ImageView to apply the fade-in animation to.
   */
  private void applyFadeInAnimation(ImageView imageView) {
    RotateTransition rotate = new RotateTransition(Duration.seconds(2), imageView);
    rotate.setByAngle(360);
    rotate.setCycleCount(3);
    rotate.play();
  }

  /**
   * Creates and returns a panel for user input. This panel includes a label, a text field for
   * entering the username, and a submit button.
   *
   * @return A VBox containing the input elements.
   */
  private HBox createInputPanel() {
    HBox inputPanel = new HBox(10);
    inputPanel.setAlignment(Pos.CENTER);
    inputPanel.setPadding(new Insets(10));
    inputPanel.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    usernameField = new TextField();
    usernameField.setMaxWidth(600);
    usernameField.getStyleClass().add("username-field");
    usernameField.setPromptText("Enter your username");
    usernameField.setFocusTraversable(false);

    ImageView submitImage =
        new ImageView(new Image(getClass().getResourceAsStream("/images/button.png")));
    submitImage.setFitHeight(50);
    submitImage.setFitWidth(50);
    submitImage.setEffect(new DropShadow());

    submitImage.setOnMousePressed(e -> submitImage.setScaleX(0.9));
    submitImage.setOnMousePressed(e -> submitImage.setScaleY(0.9));
    submitImage.setOnMouseReleased(e -> submitImage.setScaleX(1.0));
    submitImage.setOnMouseReleased(e -> submitImage.setScaleY(1.0));

    submitImage.setOnMouseClicked(e -> submitAction());

    inputPanel.getChildren().addAll(usernameField, submitImage);

    return inputPanel;
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
    viewController.setUsername(username);
    ViewController.switchToMainMenu();
  }
}
