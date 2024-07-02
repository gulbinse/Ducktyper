package typeracer.server.utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/** Generates unique identifiers. Uses atomic operations to ensure thread-safe ID generation. */
public class IdentifierGenerator {

  private static final Random RANDOM = new Random();
  private static final int START_ID_MINIMUM = 1_000_000;
  private static final int START_ID_MAXIMUM = 10_000_000;
  private static final int INCREMENT_MINIMUM = 10_000;
  private static final int INCREMENT_MAXIMUM = 100_000;

  private final int startId = RANDOM.nextInt(START_ID_MINIMUM, START_ID_MAXIMUM + 1);
  private final AtomicInteger currentIntId = new AtomicInteger(startId);

  /** The default constructor of this class. */
  public IdentifierGenerator() {}

  /**
   * Generates a new unique and positive ID. The actual number of unique IDs that can be generated
   * may vary due to the random increments, but a minimum of 21,374 unique IDs is guaranteed before
   * exceeding the maximum integer value.
   *
   * @return a unique ID as a positive integer
   */
  public int generateId() {
    return currentIntId.addAndGet(RANDOM.nextInt(INCREMENT_MINIMUM, INCREMENT_MAXIMUM + 1));
  }
}
