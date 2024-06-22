package typeracer.game;

import typeracer.game.observable.Observable;
import typeracer.game.observable.Observer;

/** Represents a player of the game. */
public class Player {
  private String username;
  private PlayerState state;

  public Player(String username) {
    this.username = username;
    state = new PlayerState();
  }

  public String getName() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public double getProgress() {
    return state.getProgress();
  }

  public double getWPM() {
    return state.getWPM();
  }

  public boolean isReady() {
    return state.isReady();
  }

  public void typeLetter(char letter) {

  }
}
