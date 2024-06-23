package typeracer.game.observable;

import typeracer.game.GameState;

/** Interface for objects that receive updates from an Observable. */
public interface Observer {

  /**
   * Called when an Observable is updated.
   *
   * @param o the observable object
   * @param arg an argument passed to the update method
   */
  void update(Observable o, Object arg);

  /** Updates something. */
  void update();

  /**
   * Called when a new player joins.
   *
   * @param id of the new player
   * @param newGameState the new game state
   */
  void updateNewPlayer(int id, GameState newGameState);

  /**
   * Called when the game state changes.
   *
   * @param newGameState the new game state
   */
  void updateState(GameState newGameState);

  /**
   * Called when a player is removed from the game.
   *
   * @param id of the removed player
   * @param newGameState the new game state
   */
  void updateRemovedPlayer(int id, GameState newGameState);
}
