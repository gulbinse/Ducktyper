package typeracer.game.timer;

/** Abstract class for handling timers. */
public abstract class Timer {

  /** Returns whether the timer is running. */
  protected boolean isRunning;

  /** The default constructor of this class. */
  public Timer() {}

  /** Starts the timer. */
  public abstract void start();

  /** Stops the timer. */
  public abstract void stop();
}
