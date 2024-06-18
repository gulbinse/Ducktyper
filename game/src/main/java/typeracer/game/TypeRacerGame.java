package typeracer.game;

import typeracer.game.observable.Observable;
import typeracer.game.observable.Observer;

/** The main class for the game, managing states and providing an interface for the server. */
public class TypeRacerGame implements Observable {

  /** The default constructor of this class. */
  public TypeRacerGame() {}

  @Override
  public void subscribe(Observer obs) {}

  @Override
  public void unsubscribe(Observer obs) {}

  @Override
  public void notifyAboutState(GameState newState) {}

  @Override
  public void notifyAboutNewPlayer(String playerName, GameState newState) {}

  @Override
  public void notifyAboutRemovedPlayer(String playerName, GameState newState) {}
}
