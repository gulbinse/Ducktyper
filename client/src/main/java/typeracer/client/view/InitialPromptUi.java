package typeracer.client.view;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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

  /** Text field for entering the IP address of the server. */
  private TextField ipField;

  /** Text field for entering the port number of the server. */
  private TextField portField;

  /** The controller to manage views and handle interactions. */
  private final ViewController viewController;

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
    initializeUi();
  }

  /**
   * Initializes the user interface by setting up the layout, padding, alignment, background, and
   * adding the necessary UI components to the main container.
   */
  private void initializeUi() {
    this.setSpacing(20);
    this.setPadding(new Insets(20));
    this.setAlignment(Pos.CENTER);
    this.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    setIconImage(stage);
    VBox titlePanel = createImagePanel();
    VBox inputPanel = createInputPanel();

    this.getChildren().addAll(titlePanel, inputPanel);
    this.setOnKeyPressed(
        e -> {
          if (e.getCode().equals(KeyCode.ENTER)) {
            submitAction();
          }
        });
  }

  /**
   * Creates the input panel containing the username text field and a submit image. The panel is
   * styled and positioned within the UI.
   *
   * @return An HBox containing the username input field and submit image.
   */
  private VBox createInputPanel() {
    VBox inputPanel = new VBox(10);
    inputPanel.setAlignment(Pos.CENTER);
    inputPanel.setPadding(new Insets(10));
    inputPanel.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    usernameField = new TextField("TheRealDonaldDuck");
    usernameField.setPromptText("Username");
    usernameField.setMaxWidth(300);
    usernameField.setStyle("-fx-alignment: center;");
    usernameField.getStyleClass().add("startScreen-input-field");
    usernameField.setFocusTraversable(false);

    ipField = new TextField("localhost");
    ipField.setPromptText("Server-IP");
    ipField.setMaxWidth(300);
    ipField.setStyle("-fx-alignment: center;");
    ipField.getStyleClass().add("startScreen-input-field");
    ipField.setFocusTraversable(false);

    portField = new TextField("4441");
    portField.setPromptText("Port");
    portField.setMaxWidth(300);
    portField.setStyle("-fx-alignment: center;");
    portField.getStyleClass().add("startScreen-input-field");
    portField.setFocusTraversable(false);

    ImageView submitImage = createSubmitImage();

    inputPanel
        .getChildren()
        .addAll(
            submitImage,
            usernameField,
            ipField,
            portField);

    return inputPanel;
  }

  /**
   * Creates an ImageView for the submit button with styling and click animations.
   *
   * @return An ImageView configured as a submit button.
   */
  private ImageView createSubmitImage() {
    ImageView submitImage =
        new ImageView(new Image(getClass().getResourceAsStream("/images/button.png")));
    submitImage.setFitHeight(50);
    submitImage.setFitWidth(50);
    submitImage.setEffect(new DropShadow());

    submitImage.setOnMousePressed(
        e -> {
          submitImage.setScaleX(0.9);
          submitImage.setScaleY(0.9);
        });
    submitImage.setOnMouseReleased(
        e -> {
          submitImage.setScaleX(1.0);
          submitImage.setScaleY(1.0);
        });

    submitImage.setOnMouseClicked(e -> submitAction());

    return submitImage;
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
    titleImageView.setFitWidth(400);
    titleImageView.setPreserveRatio(true);
    imagePanel.getChildren().add(titleImageView);

    Image duckImage = new Image(getClass().getResourceAsStream("/images/duck.png"));
    ImageView duckImageView = new ImageView(duckImage);
    duckImageView.setFitWidth(400);
    duckImageView.setPreserveRatio(true);
    imagePanel.getChildren().add(duckImageView);

    applyFadeInAnimation(duckImageView);

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
    rotate.setCycleCount(1);
    rotate.play();
  }

  /**
   * Handles the submit button action. Retrieves the username from the text field and switches to
   * the main menu UI.
   */
  private void submitAction() {
    String username = usernameField.getText().trim();
    String ip = ipField.getText().trim();
    String port = portField.getText().trim();

    if (username.isEmpty() || ip.isEmpty() || port.isEmpty()) {
      viewController.showAlert("Please fill in all fields correctly.");
      return;
    }

    if (username.length() > 18) {
      viewController.showAlert("Username must not be longer than 18 characters.");
      return;
    }

    try {
      int portNumber = Integer.parseInt(port);
      viewController.connectToServer(ip, portNumber, username);
    } catch (NumberFormatException e) {
      viewController.showAlert("Please enter a valid port number.");
    } catch (Exception e) {
      viewController.showAlert("Could not connect to the server: " + e.getMessage());
    }
  }
}
