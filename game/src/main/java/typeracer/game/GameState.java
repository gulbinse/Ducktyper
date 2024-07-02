package typeracer.game;

import java.util.List;

/** Manages the state of the game. */
public class GameState {

  /** The default constructor of this class. */
  public GameState() {}

  public int getWpm() {
    return 0;
  }

  public int getErrors() {
    return 0;
  }

  public List<String> getTopPlayers() {
    return List.of();
  }

  public Iterable<Object> getPlayers() {
    return null;
  }
}
