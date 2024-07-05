package typeracer.server.utils;

/**
 * A result of trying to type a character.
 */
public enum TypingResult {
  /**
   * The typing was correct.
   */
  CORRECT,

  /**
   * The typing was incorrect.
   */
  INCORRECT,

  /**
   * This Player has already finished the game.
   */
  PLAYER_FINISHED_ALREADY
}
