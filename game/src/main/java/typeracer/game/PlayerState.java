package typeracer.game;

/** Manages the state of a single player. */
public class PlayerState {
  private double progress = 0; // Ranging from 0.0 to 1.0 according to the protocol
  private double wordsPerMinute = 0;

  /** The default constructor of this class. */
  public PlayerState() {}

  public double getProgress() {
    return progress;
  }

  public void setProgress(double progress) {
    this.progress = progress;
  }

  public double getWPM() {
    return wordsPerMinute;
  }

  public void setWPM(double wpm) {
    this.wordsPerMinute = wpm;
  }
}
