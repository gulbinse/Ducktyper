package typeracer.communication.messages.server;

import typeracer.communication.messages.Message;

/**
 * Notification indicating a change in the state of the game. This notification is sent from the
 * server to every client to update them about the game state.
 */
public final class GameStateNotification implements Message {

  private final String gameStatus;

  /**
   * Constructs a new GameStateNotification with the specified arguments.
   *
   * @param gameStatus the new state of the game
   */
  public GameStateNotification(String gameStatus) {
    this.gameStatus = gameStatus;
  }

  /**
   * Returns the new state of the game.
   *
   * @return the new state of the game
   */
  public String getGameStatus() {
    return gameStatus;
  }
}
