package typeracer.communication.statuscodes;

/** Represents the status of the Typeracer game. */
public enum GameStatus {
  /** Represents that the game is still running. */
  RUNNING,
  /** Represents that the game is finished. */
  FINISHED,
  /** Represents that the game has not started yet because not every player is ready yet. */
  WAITING_FOR_PLAYERS
}
