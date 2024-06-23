package typeracer.game;

/** Manages the state of a single player. */
public class PlayerState {
  private double progress = 0; // Ranging from 0.0 to 1.0 according to the protocol
  private double wordsPerMinute = 0;
  private boolean isReady = false;
  private int currentTextIndex = 0;
  private int numTypedWords = 0;

  /** The default constructor of this class. */
  public PlayerState() {}

  public double getProgress() {
    return progress;
  }

  public void setProgress(double progress) {
    this.progress = progress;
  }

  public double getWordsPerMinute() {
    return wordsPerMinute;
  }

  public void setWordsPerMinute(double wordsPerMinute) {
    this.wordsPerMinute = wordsPerMinute;
  }

  public boolean isReady() {
    return isReady;
  }

  public void setReady(boolean ready) {
    isReady = ready;
  }

  public int getCurrentTextIndex() {
    return currentTextIndex;
  }

  public void setCurrentTextIndex(int currentTextIndex) {
    this.currentTextIndex = currentTextIndex;
  }

  public int getNumTypedWords() {
    return numTypedWords;
  }

  /**
   * Increases the number of typed words by one. This should generally be the only way the player's
   * number of typed words is changed.
   */
  public void incrementNumTypedWords() {
    numTypedWords++;
  }
}
