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

  void updateNewPlayer(String playerName, GameState newState);

  void updateState(GameState state);

  void updateRemovedPlayer(String playerName, GameState newState);
}
