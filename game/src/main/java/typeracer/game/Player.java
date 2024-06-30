package typeracer.game;

/** Represents a player of the game. */
public class Player {
  private String username;
  private final PlayerState state;

  /** A result of trying to type a letter. */
  public enum TypingResult {
    /** The typing was correct. */
    CORRECT,

    /** The typing was incorrect. */
    INCORRECT,

    /** This Player has already finished the game. */
    PLAYER_FINISHED_ALREADY
  }

  /**
   * Creates a new Player with the given username.
   *
   * @param username The username of the player
   */
  public Player(String username) {
    this.username = username;
    state = new PlayerState();
  }

  /**
   * Returns this Player's username.
   *
   * @return this Player's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns this Player's current progress.
   *
   * @return this Player's current progress
   */
  public double getProgress() {
    return state.getProgress();
  }

  /**
   * Returns this Player's current words per minute.
   *
   * @return this Player's current words per minute
   */
  public double getWordsPerMinute() {
    return state.getWordsPerMinute();
  }

  /**
   * Returns true if this Player is ready, false otherwise.
   *
   * @return true if this Player is ready, false otherwise
   */
  public boolean isReady() {
    return state.isReady();
  }

  /**
   * Sets the ready state for this player.
   *
   * @param isReady true if player is ready, false otherwise
   */
  public void setIsReady(boolean isReady) {
    state.setIsReady(isReady);
  }

  public boolean isFinished() {
    return state.isFinished();
  }

  public void setIsFinished(boolean isFinished) {
    state.setIsFinished(isFinished);
  }

  /**
   * Makes this player type the given letter. Checks if the typed letter appears in the given text
   * at the position this Player is currently at, and calculates updated words per minute and
   * progress if necessary.
   *
   * @param typedLetter the letter this player has typed
   * @param textToType the text the player has to type
   * @param gameStartTime the time the game started at, given as a long like returned by
   *     System.nanoTime()
   * @return {@link TypingResult#CORRECT} if the letter was correct, else {@link
   *     TypingResult#INCORRECT}
   */
  public synchronized TypingResult typeLetter(
      char typedLetter, String textToType, long gameStartTime) {
    int currentTextIndex = state.getCurrentTextIndex();
    char correctLetter = textToType.charAt(currentTextIndex);

    double progress = (double) currentTextIndex / textToType.length();
    assert 0 <= progress && progress <= 1;

    // Update words per minute in every case, since it might change with a wrong letter as well
    updateWordsPerMinute(gameStartTime);

    if (typedLetter == correctLetter) {
      state.setCurrentTextIndex(currentTextIndex + 1);

      // Update Progress only if typing was successful, to avoid unnecessary updates
      state.setProgress(progress);
      if (progress >= 1) {
        setIsFinished(true);
      }

      if (Character.isSpaceChar(correctLetter)) {
        state.incrementNumTypedWords();
      }
      return TypingResult.CORRECT;
    }
    return TypingResult.INCORRECT;
  }

  private synchronized void updateWordsPerMinute(long gameStartTime) {
    long elapsedTime = System.nanoTime() - gameStartTime;
    double elapsedMinutes = (double) elapsedTime / (10 ^ 9);
    double wordsPerMinute = state.getNumTypedWords() / elapsedMinutes;

    assert wordsPerMinute >= 0;

    state.setWordsPerMinute(wordsPerMinute);
  }
}
