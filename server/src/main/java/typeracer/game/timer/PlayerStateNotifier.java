package typeracer.game.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import typeracer.game.TypeRacerGame;

/** This class sends periodical player state updates. */
public class PlayerStateNotifier extends Timer {

  private final TypeRacerGame game;
  private ScheduledExecutorService executorService;

  private PlayerStateNotifier(TypeRacerGame game) {
    this.game = game;
  }

  /**
   * Creates a new PlayerStateNotifier with the specified arguments.
   *
   * @param game the game this notifier belongs to
   * @return a new PlayerStateNotifier instance
   */
  public static PlayerStateNotifier create(TypeRacerGame game) {
    return new PlayerStateNotifier(game);
  }

  @Override
  public void start() {
    isRunning = true;
    executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.scheduleAtFixedRate(game::broadcastPlayerStates, 0, 200, TimeUnit.MILLISECONDS);
  }

  @Override
  public void stop() {
    if (isRunning) {
      executorService.shutdownNow();
      isRunning = false;
    }
  }
}
