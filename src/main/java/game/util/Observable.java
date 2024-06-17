package game.util;

import game.GameState;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides an interface to be observed.
 */
public interface Observable {
  void subscribe(Observer obs);

  void unsubscribe(Observer obs);

  void notifyAboutState(GameState newState);

  void notifyAboutNewPlayer(String playerName, GameState newState);

  void notifyAboutRemovedPlayer(String playerName, GameState newState);
}
