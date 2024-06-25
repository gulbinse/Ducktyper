package typeracer.server;

import static com.google.common.truth.Truth.assertThat;
import static typeracer.server.TestUtils.getNetworkIn;
import static typeracer.server.TestUtils.getNetworkOut;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Timeout;
import typeracer.server.mockobjects.MockInputStream;
import typeracer.server.mockobjects.MockServerSocket;
import typeracer.server.mockobjects.MockSocket;
import typeracer.server.mockobjects.Sleep;

@Timeout(7)
public class ServerConnectionTest {

  private static final String USER1 = "User1";

  private void assertThatContainsNKeyValuePairs(String message, int numberOfPairsExpected) {
    assertThat(message).matches("[^:]*:[^:]*".repeat(numberOfPairsExpected));
  }

  @Test
  public void testServer_receivesJoinRequest_sendsPlayerJoin()
      throws IOException, InterruptedException {
    MockInputStream networkIn =
        getNetworkIn("{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + USER1 + "\"}");
    ByteArrayOutputStream networkOut = getNetworkOut();
    MockSocket mockSocket = new MockSocket(networkIn, networkOut);
    MockServerSocket serverSocket = new MockServerSocket(List.of(mockSocket));

    TestUtils.startServer(serverSocket);
    do {
      Thread.sleep(10);
    } while (!networkIn.isDone());
    Thread.sleep(Sleep.BEFORE_TESTING.getMillis());

    String sent = networkOut.toString(StandardCharsets.UTF_8);
    String[] jsonMessages = sent.split(System.lineSeparator());
    for (String message : jsonMessages) {
      if (message.matches(".*\"messageType\":\"PlayerJoinedNotification\".*")
          && message.matches(".*\"playerName\":\"" + USER1 + "\".*")) {
        assertThat(message.matches(".*\"numPlayers\":1.*"));
        assertThatContainsNKeyValuePairs(message, 3);
        return;
      }
    }
    Assertions.fail("No PlayerJoinedNotification for player " + USER1);
  }
}
