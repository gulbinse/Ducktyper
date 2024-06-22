package typeracer.server.utils;

import java.util.concurrent.atomic.AtomicInteger;

/** Generates unique identifiers. Uses atomic operations to ensure thread-safe ID generation. */
public class IdentifierGenerator {

  private final AtomicInteger currentId = new AtomicInteger(0);

  /** The default constructor of this class. */
  public IdentifierGenerator() {}

  /**
   * Generates a new unique ID.
   *
   * @return a unique ID as an integer
   */
  public int generateId() {
    return currentId.incrementAndGet();
  }
}
