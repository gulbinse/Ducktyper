package typeracer.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.NoSuchElementException;
import typeracer.server.mockobjects.MockInputStream;

public final class TestUtils {

  public static Server startServer(ServerSocket serverSocket) {
    Server server = new Server();
    new Thread(
            () -> {
              try {
                server.start(serverSocket);
              } catch (NoSuchElementException e) {
                // expected, because number of incoming clients will be exhausted quickly.
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();
    return server;
  }

  public static MockInputStream getNetworkIn(String input) {
    return new MockInputStream(input + System.lineSeparator());
  }

  public static ByteArrayOutputStream getNetworkOut() {
    return new ByteArrayOutputStream();
  }

  public static boolean areDone(Collection<MockInputStream> inputs) {
    return inputs.stream().allMatch(MockInputStream::isDone);
  }
}
