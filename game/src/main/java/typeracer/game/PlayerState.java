package typeracer.game;

/** Manages the state of a single player. */
public class PlayerState {
  private double progress = 0; // Ranging from 0.0 to 1.0 according to the protocol
  private double wordsPerMinute = 0;
  private double charactersPerMinute = 0;
  private boolean isReady = false;
  private boolean isFinished = false;
  private int currentTextIndex = 0;
  private int numTypedWords = 0;

  /** The default constructor of this class. */
  public PlayerState() {}

  /**
   * Returns the current progress.
   *
   * @return the current progress
   */
  public double getProgress() {
    return progress;
  }

  /**
   * Sets the current progress to the specified value.
   *
   * @param progress the new progress
   */
  public void setProgress(double progress) {
    this.progress = progress;
  }

  /**
   * Returns the current speed in words per minute.
   *
   * @return the current speed in words per minute
   */
  public double getWordsPerMinute() {
    return wordsPerMinute;
  }

  /**
   * Returns the current speed in characters per minute.
   *
   * @return the current speed in characters per minute
   */
  public double getCharactersPerMinute() {
    return charactersPerMinute;
  }

  /**
   * Sets the words per minute to the specified value.
   *
   * @param wordsPerMinute the new words per minute
   */
  public void setWordsPerMinute(double wordsPerMinute) {
    this.wordsPerMinute = wordsPerMinute;
  }

  /**
   * Sets the characters per minute to the specified value.
   *
   * @param charactersPerMinute the new characters per minute
   */
  public void setCharactersPerMinute(double charactersPerMinute) {
    this.charactersPerMinute = charactersPerMinute;
  }

  /**
   * Returns true if the Player is ready, false otherwise.
   *
   * @return true if the Player is ready, false otherwise
   */
  public boolean isReady() {
    return isReady;
  }

  /**
   * Sets the Player to being ready if true is given, to not ready if false is given.
   *
   * @param isReady the readiness state to set this Player to
   */
  public void setIsReady(boolean isReady) {
    this.isReady = isReady;
  }

  /**
   * Returns the zero-indexed position in the current text as an integer.
   *
   * @return the current text index
   */
  public int getCurrentTextIndex() {
    return currentTextIndex;
  }

  /**
   * Sets the zero-indexed position in the current text to the specified integer.
   *
   * @param currentTextIndex the index to set
   */
  public void setCurrentTextIndex(int currentTextIndex) {
    this.currentTextIndex = currentTextIndex;
  }

  /**
   * Returns the number of typed words until now.
   *
   * @return the number of typed words until now
   */
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

  /**
   * Returns true if the Player is finished, false otherwise.
   *
   * @return true if the Player is finished, false otherwise
   */
  public boolean isFinished() {
    return isFinished;
  }

  /**
   * Sets the Player to being finished if true is given, to not finished if false is given.
   *
   * @param isFinished the state to set this Player to
   */
  public void setIsFinished(boolean isFinished) {
    this.isFinished = isFinished;
  }
}
