package typeracer.game.observable;

import typeracer.game.GameState;

/** Provides an interface to be observed. */
public interface Observable {

  /**
   * Adds an observer to the set of observers for this object, provided that it is not the same as
   * some observer already in the set.
   *
   * @param obsv the observer to be added
   */
  void subscribe(Observer obsv);

  /**
   * Deletes an observer from the set of observers of this object.
   *
   * @param obsv the observer to be removed
   */
  void unsubscribe(Observer obsv);

  /**
   * Notifies every observer about a new state of the game.
   *
   * @param newState the new GameState
   */
  void notifyAboutState(GameState newState);

  /**
   * Notifies every observer that a new player has joined the game.
   *
   * @param playerName the name of the player who joined the game
   * @param newState the new GameState after the player joined
   */
  void notifyAboutNewPlayer(String playerName, GameState newState);

  /**
   * Notifies every observer that a player has left the game.
   *
   * @param playerName the name of the player who has left the game
   * @param newState the new GameState after the player left
   */
  void notifyAboutRemovedPlayer(String playerName, GameState newState);
}
