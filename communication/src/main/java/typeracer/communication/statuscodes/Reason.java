package typeracer.communication.statuscodes;

/** Possible reason for a denied client request. */
public enum Reason {
  /** Represents that a player is not allowed to connect. */
  HANDSHAKE_BANNED("You are currently banned."),
  /** Represents that a player provided an invalid username. */
  INVALID_USERNAME("This username is not allowed."),
  /** Represents that the specified session does not exist. */
  SESSION_NOT_FOUND("This session does not exist."),
  /** Represents that the game has already started. */
  SESSION_GAME_ALREADY_STARTED("The game has already started."),
  /** Represents that the session is already full. */
  SESSION_FULL("This session is full."),
  /** Represents that a player has been kicked. */
  SESSION_KICKED("You have been kicked."),
  /** Represents that the client does not have permission to create a session. */
  SESSION_CREATE_NO_PERMISSION("You do not have permission to create a session."),
  /** Represents that an undefined or unknown error occurred. */
  UNKNOWN("An unexpected error occurred.");

  private final String string;

  Reason(String string) {
    this.string = string;
  }

  /**
   * Returns the reason for a denied client request as a string.
   *
   * @return the reason for a denied client request
   */
  public String getString() {
    return string;
  }
}
