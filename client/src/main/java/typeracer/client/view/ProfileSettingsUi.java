package typeracer.client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import typeracer.client.ViewController;

/**
 * Represents the profile settings user interface for the TypeRacer game. This class sets up the GUI
 * elements that allow the user to change their username, WPM goal, and favorite text, and save or
 * cancel these changes.
 */
public class ProfileSettingsUi extends VBox {
  /** The view controller to manage views and handle interactions. */
  private ViewController viewController;

  /** The text field for entering the username. */
  private TextField usernameField;

  /** The text field for entering the WPM goal. */
  private TextField wpmGoalField;

  /** The text field for entering the favorite text. */
  private TextField favoriteTextField;

  /** The button to save the profile settings. */
  private Button saveButton;

  /** The button to cancel the profile settings changes. */
  private Button cancelButton;

  private ProfileSettingsUi(ViewController viewController) {
    this.viewController = viewController;
  }

  /**
   * Creates a new ProfileSettingsUi and initializes its user interface components.
   *
   * @param viewController The controller to manage views and handle interactions.
   */
  public static ProfileSettingsUi create(ViewController viewController) {
    ProfileSettingsUi profileSettingsUi = new ProfileSettingsUi(viewController);
    profileSettingsUi.initializeUi();
    return profileSettingsUi;
  }

  /**
   * Initializes the user interface components for the profile settings UI. Sets up the layout,
   * styling, and button actions.
   */
  private void initializeUi() {
    this.setSpacing(10);
    this.setAlignment(Pos.TOP_CENTER);
    this.setBackground(
        new Background(
            new BackgroundFill(StyleManager.START_SCREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    usernameField = new TextField();
    wpmGoalField = new TextField();
    favoriteTextField = new TextField();

    VBox box = new VBox(10);
    box.setAlignment(Pos.TOP_CENTER);
    box.setPadding(new Insets(20, 20, 20, 20));
    box.setBackground(
        new Background(new BackgroundFill(StyleManager.GREY_BOX, CornerRadii.EMPTY, Insets.EMPTY)));
    box.setBorder(
        new Border(
            new BorderStroke(
                Paint.valueOf("black"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1))));

    box.getChildren().add(createLabeledField("Change username:", usernameField));
    box.getChildren().add(createLabeledField("WPM goal:", wpmGoalField));
    box.getChildren().add(createLabeledField("Favorite text:", favoriteTextField));
    box.getChildren().add(createButtonsPanel());

    this.getChildren().add(box);
    VBox.setMargin(box, new Insets(10, 100, 70, 100));

    configureButtonActions();

    StyleManager.applyFadeInAnimation(box, 1000);
    StyleManager.applyButtonHoverAnimation(saveButton, cancelButton);
  }

  /**
   * Creates an HBox containing a label and a text field.
   *
   * @param labelText The text for the label.
   * @param textField The text field for user input.
   * @return A HBox containing the label and text field.
   */
  private HBox createLabeledField(String labelText, TextField textField) {
    HBox hbox = new HBox(10);
    hbox.setAlignment(Pos.CENTER_LEFT);

    Label label = new Label(labelText);
    label.setFont(StyleManager.ITALIC_FONT);

    textField.setPrefWidth(200);

    hbox.getChildren().addAll(label, textField);

    return hbox;
  }

  /**
   * Creates an HBox containing the save and cancel buttons.
   *
   * @return A HBox containing the buttons.
   */
  private HBox createButtonsPanel() {
    HBox hbox = new HBox(25);
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(5, 0, 0, 0));

    saveButton =
        StyleManager.createStyledButton(
            "save", StyleManager.GREEN_BUTTON, StyleManager.STANDARD_FONT);
    cancelButton =
        StyleManager.createStyledButton(
            "cancel", StyleManager.RED_BUTTON, StyleManager.STANDARD_FONT);

    hbox.getChildren().addAll(saveButton, cancelButton);

    return hbox;
  }

  /** Configures the actions for the save and cancel buttons. */
  private void configureButtonActions() {
    saveButton.setOnAction(
        e -> {
          String wpmText = wpmGoalField.getText();
          if (isNumeric(wpmText)) {
            int wpmGoal = Integer.parseInt(wpmText);
            viewController.saveUserSettings(
                usernameField.getText(), wpmGoal, favoriteTextField.getText());
          } else {
            showAlert("Error", "WPM goal must be a whole number");
          }
        });

    cancelButton.setOnAction(e -> viewController.showScene(ViewController.SceneName.MAIN_MENU));
  }

  /**
   * Utility method to show an alert dialog.
   *
   * @param title the title of the alert dialog
   * @param content the content text of the alert dialog
   */
  private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  /**
   * Checks if the input string is numeric.
   *
   * @param str String to check
   * @return true if the string is numeric, false otherwise
   */
  private boolean isNumeric(String str) {
    return str != null && str.matches("-?\\d+");
  }
}
