package typeracer.game;

import typeracer.server.utils.Enums.TypingResult;

/** Represents a player of the game. */
public class Player {
  private final int id;
  private final PlayerState state;
  private static final long MINUTES_TO_NANO_SECONDS_FACTOR = 10 ^ 9;
  private long gameStartTime;

  /**
   * Creates a new Player with the given username.
   *
   * @param id The unique ID of the player
   */
  public Player(int id) {
    this.id = id;
    state = new PlayerState();
  }

  /**
   * Returns this Player's username.
   *
   * @return this Player's username
   */
  public int getId() {
    return id;
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

  /**
   * Returns true if this Player is finished, false otherwise.
   *
   * @return true if this Player is finished, false otherwise
   */
  public boolean isFinished() {
    return state.isFinished();
  }

  /**
   * Sets the finished state for this player.
   *
   * @param isFinished true if player is finished, false otherwise
   */
  public void setIsFinished(boolean isFinished) {
    state.setIsFinished(isFinished);
  }

  /**
   * Makes this player type the given character. Checks if the typed character appears in the given
   * text at the position this Player is currently at, and calculates updated words per minute and
   * progress if necessary.
   *
   * @param typedCharacter the character this player has typed
   * @param textToType the text the player has to type
   * @param gameStartTime the time the game started at, given as a long like returned by
   *     System.nanoTime()
   * @return {@link TypingResult#CORRECT} if the character was correct, else {@link
   *     TypingResult#INCORRECT}
   */
  synchronized TypingResult typeCharacter(
      char typedCharacter, String textToType, long gameStartTime) {
    this.gameStartTime = gameStartTime;
    int currentTextIndex = state.getCurrentTextIndex();
    char correctCharacter = textToType.charAt(currentTextIndex);

    // Update typing speeds in every case, since it might change with a wrong character as well
    updateAllTypingSpeeds();

    if (typedCharacter == correctCharacter) {
      state.incrementCurrentTextIndex();
      int newTextIndex = state.getCurrentTextIndex(); // Should be incremented by one
      assert newTextIndex == currentTextIndex + 1;

      // Update progress only if typing was successful, to avoid unnecessary updates
      double progress = (double) newTextIndex / textToType.length();
      assert 0 <= progress && progress <= 1;
      state.setProgress(progress);
      if (progress >= 1) {
        setIsFinished(true);
      }

      if (Character.isSpaceChar(correctCharacter)) {
        state.incrementNumTypedWords();
      }
      return TypingResult.CORRECT;
    }
    return TypingResult.INCORRECT;
  }

  private synchronized void updateAllTypingSpeeds() {
    updateWordsPerMinute();
    updateCharactersPerMinute();
  }

  private synchronized void updateWordsPerMinute() {
    double wordsPerMinute =
        getGeneralTypingSpeed(state.getNumTypedWords(), MINUTES_TO_NANO_SECONDS_FACTOR);
    state.setWordsPerMinute(wordsPerMinute);
  }

  private synchronized void updateCharactersPerMinute() {
    double charactersPerMinute =
        getGeneralTypingSpeed(state.getCurrentTextIndex(), MINUTES_TO_NANO_SECONDS_FACTOR);
    state.setCharactersPerMinute(charactersPerMinute);
  }

  /**
   * Returns a general typing speed since start of the game, using the current progress measured by
   * some metric and a time factor to determine the referred timeframe.
   *
   * @param progressMetric the metric used to determine the progress, e.g. the number of typed words
   *     or typed characters. Must be positive
   * @param timeFactor the factor used to determine the referred timeframe, such that speed =
   *     progressMetric * timeFactor / elapsedTime. The factor has to convert the desired time unit
   *     to nanoseconds as returned by System.nanoTime() and must be positive
   * @return the typing speed measured in the given metric
   */
  private double getGeneralTypingSpeed(int progressMetric, long timeFactor) {
    assert progressMetric >= 0;
    assert timeFactor >= 0;
    long elapsedTime = System.nanoTime() - gameStartTime;
    assert elapsedTime >= 0;
    return (double) (progressMetric * timeFactor) / elapsedTime;
  }
}
