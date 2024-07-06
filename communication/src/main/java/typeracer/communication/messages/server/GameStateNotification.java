package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;
import typeracer.communication.statuscodes.GameStatus;

/**
 * Notification indicating a change in the state of the game. This notification is sent from the
 * server to every client to update them about the game state.
 */
public final class GameStateNotification implements Message {

  private final GameStatus gameStatus;

  /**
   * Constructs a new GameStateNotification with the specified arguments.
   *
   * @param gameStatus the new state of the game
   */
  public GameStateNotification(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  /**
   * Returns the new state of the game.
   *
   * @return the new state of the game
   */
  public GameStatus getGameStatus() {
    return gameStatus;
  }
}
