package typeracer.client.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Entry point of the application. */
public class Main {

  /** The default constructor of this class. */
  public Main() {}

  /**
   * The entry point of the Typeracer Game application. Ensures that the GUI is created on the Event
   * Dispatch Thread (EDT) for thread safety.
   *
   * @param args The command-line arguments (not used).
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(Main::createAndShowGui);
  }

  /**
   * Initializes and displays the main GUI window for the Typeracer Game. Sets up the frame,
   * initializes the InitialPromptUi panel, and makes the frame visible. The window is set to close
   * the application upon exiting.
   */
  private static void createAndShowGui() {
    JFrame frame = new JFrame("Typeracer Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200);

    InitialPromptUi initialPromptUi = new InitialPromptUi(frame);
    frame.getContentPane().add(initialPromptUi);

    frame.pack();
    frame.setVisible(true);
  }
}
