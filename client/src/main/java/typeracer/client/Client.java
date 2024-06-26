package typeracer.client;

import typeracer.client.view.Main;

/**
 * Entry point class for the TypeRacer game application. This class contains the main method which
 * kicks off the application by initializing the GUI.
 */
public class Client {

  /** Default constructor for the Client class. */
  public Client() {}

  /**
   * The main method that starts the TypeRacer game application. It delegates the responsibility of
   * initializing and displaying the GUI to the {@link Main} class.
   *
   * @param args Command line arguments passed to the program (not used in this application).
   */
  public static void main(String[] args) {
    Main.main(args);
  }
}
