package typeracer.game;

/** Represents a player of the game. */
public class Player {
  private String username;
  private final PlayerState state;

  public enum TypingResult {
    SUCCESSFUL,
    UNSUCCESSFUL
  }

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

  public TypingResult typeLetter(char typedLetter, String textToType, long gameStartTime) {
    int currentTextIndex = state.getCurrentTextIndex();
    char correctLetter = textToType.charAt(currentTextIndex);

    double progress = (double) currentTextIndex / textToType.length();
    assert 0 <= progress && progress <= 1;

    // Update WPM in every case, since it might change with a wrong letter as well
    updateWPM(gameStartTime);

    if (typedLetter == correctLetter) {
      state.setCurrentTextIndex(currentTextIndex + 1);

      // Update Progress only if typing was successful, to avoid unnecessary updates
      state.setProgress(progress);

      if (correctLetter == ' ') {
        state.incrementNumTypedWords();
      }
      return TypingResult.SUCCESSFUL;
    }
    return TypingResult.UNSUCCESSFUL;
  }

  private void updateWPM(long gameStartTime) {
    long elapsedTime = System.nanoTime() - gameStartTime;
    double elapsedMinutes = (double) elapsedTime / (10^9);
    double wpm = state.getNumTypedWords() / elapsedMinutes;

    assert wpm >= 0;

    state.setWPM(wpm);
  }
}
