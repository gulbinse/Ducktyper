package typeracer.server.mockobjects;

public enum Sleep {
  BETWEEN_SOCKET_ACCEPTS(100),
  BEFORE_TESTING(1000);

  private final int millis;

  Sleep(int millis) {
    this.millis = millis;
  }

  public int getMillis() {
    return millis;
  }
}
