package typeracer.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** Handles the display of the initial prompt. */
public class InitialPromptUi extends JPanel {

  private JTextField usernameField;
  private JButton submitButton;

  /** The default constructor of this class. */
  public InitialPromptUi(JFrame frame) {
    setBackground(new Color(0xF5F5F5));
    setLayout(new BorderLayout());
    setIconImage(frame);

    add(createImagePanel(), BorderLayout.NORTH);
    add(createInputPanel(), BorderLayout.CENTER);

    frame.getContentPane().setBackground(new Color(0xF5F5F5));
  }

  /**
   * Sets the icon image of the provided JFrame. This method attempts to load an image from the
   * specified resource path and set it as the icon image of the JFrame.
   *
   * @param frame The JFrame whose icon image is to be set. This frame must not be null.
   * @throws IOException If there is an issue reading the image file from the resources. The
   *     exception is caught and printed to the standard error stream.
   */
  private void setIconImage(JFrame frame) {
    try {
      Image img = ImageIO.read(getClass().getResource("/images/typewriter.jpeg"));
      frame.setIconImage(img);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a JPanel that displays a centered image from the resources directory. This panel is
   * used to enhance the GUI with a visual element. If the image fails to load, an error is printed
   * to the standard error stream.
   *
   * @return JPanel with a centered image, styled to fit the application's theme.
   * @throws IOException if there is an issue reading the image file, which is caught and printed to
   *     the standard error stream.
   */
  private JPanel createImagePanel() {
    JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    imagePanel.setBackground(new Color(0xF5F5F5));
    try {
      ImageIcon icon =
          new ImageIcon(ImageIO.read(getClass().getResource("/images" + "/typewriter.jpeg")));
      JLabel imageLabel = new JLabel(icon);
      imagePanel.add(imageLabel);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return imagePanel;
  }

  /**
   * Constructs a JPanel designed for user input with vertically arranged components: a label, a
   * text field, and a submit button. Each component is spaced for clarity. The panel is designed
   * for entering a username and submitting it.
   *
   * @return JPanel with organized input elements for username submission.
   */
  private JPanel createInputPanel() {
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setBackground(new Color(0xF5F5F5));
    centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel label = new JLabel("Enter your username");
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(Box.createVerticalStrut(10));
    centerPanel.add(label);

    usernameField = new JTextField(10);
    usernameField.setMaximumSize(new Dimension(200, 20));
    centerPanel.add(Box.createVerticalStrut(10));
    centerPanel.add(usernameField);

    submitButton = new JButton("submit");
    submitButton.setMaximumSize(new Dimension(90, 30));
    submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    submitButton.setBackground(new Color(0x009900));
    submitButton.setForeground(Color.WHITE);
    submitButton.setFont(new Font("Arial", Font.BOLD, 14));
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(this::submitAction);
    centerPanel.add(Box.createVerticalStrut(10));
    centerPanel.add(submitButton);

    return centerPanel;
  }

  /**
   * Handles the action performed when the submit button is clicked. Retrieves the text from the
   * usernameField and prints it to the console.
   *
   * @param e The action event triggered by clicking the submit button.
   */
  private void submitAction(ActionEvent e) {
    String username = usernameField.getText();
    System.out.println("Username: " + username);
  }
}
