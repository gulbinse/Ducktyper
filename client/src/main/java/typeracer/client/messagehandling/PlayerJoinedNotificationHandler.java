package typeracer.client.messagehandling;

import typeracer.client.ViewController;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.server.PlayerUpdateNotification;

/**
 * Handles PlayerJoinedNotification messages in a chain of responsibility pattern. If the message is
 * not of the specified type, it will be passed to the next handler in the chain, if any.
 */
public class PlayerJoinedNotificationHandler implements MessageHandler {

  private final MessageHandler nextHandler;
  private final ViewController viewController;

  /**
   * Constructor with the next handler in chain.
   *
   * @param nextHandler next handler in chain
   */
  public PlayerJoinedNotificationHandler(
      MessageHandler nextHandler, ViewController viewController) {
    this.nextHandler = nextHandler;
    this.viewController = viewController;
  }

  /**
   * Handles the incoming messages.
   *
   * @param message the message to handle
   */
  @Override
  public void handleMessage(Message message) {
    if (message instanceof PlayerUpdateNotification playerJoinedNotification) {
      // set number of the current players
      // TODO!! benenne alle playerJoinedNotification in playerUpdateNotification um, benenne die
      //  aufgerufene funktion in .updatePlayer(getPlayerId(), getPlayerName(), isReady()) um,
      //  benenne die addPlayer Methode in ClientSessionData in updatePlayer um und füge einen ready
      //  status hinzu. Füge den boolean innerhalb der methode anstatt des default "false" wertes
      //  ein. Achte darauf, dass auch die default Werte bei CreateSessionResponse richtig gesetzt
      //  werden, da der spieler hier keine playerUpdateNotification erhält, aber trotzdem bereits
      //  namen, readystatus, etc. anzeigen muss.
      viewController.updatePlayer(
          playerJoinedNotification.getPlayerId(), playerJoinedNotification.getPlayerName(), playerJoinedNotification.isReady());
    } else if (nextHandler != null) {
      nextHandler.handleMessage(message);
    }
  }
}
