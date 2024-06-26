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
  public void testServer_receivesJoinRequest_sendsResponseAndPlayerJoin()
      throws IOException, InterruptedException {
    String joinRequest = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + USER1 + "\"}";
    MockInputStream networkIn = getNetworkIn(joinRequest);
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
    boolean joinGameResponse = false;
    boolean playerJoinedNotification = false;
    for (String message : jsonMessages) {
      if (message.matches(".*\"messageType\":\"JoinGameResponse\".*")) {
        assertThat(message.matches(".*\"joinStatus\":\"(ACCEPTED|DENIED)\".*"));
        assertThat(message.matches(".*\"reason\":\".*\"|null.*"));
        assertThatContainsNKeyValuePairs(message, 3);
        joinGameResponse = true;
      }
      if (message.matches(".*\"messageType\":\"PlayerJoinedNotification\".*")) {
        assertThat(message.matches(".*\"playerName\":\"" + USER1 + "\".*"));
        assertThat(message.matches(".*\"numPlayers\":1.*"));
        assertThatContainsNKeyValuePairs(message, 3);
        playerJoinedNotification = true;
      }
      if (joinGameResponse && playerJoinedNotification) {
        return;
      }
    }
    Assertions.fail("Missing response for player " + USER1 + " on join request");
  }

  @Test
  public void testServer_receivesCharacterRequest_sendsResponseAndPlayerState()
      throws IOException, InterruptedException {
    String joinRequest = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + USER1 + "\"}";
    String characterRequest = "{\"messageType\":\"CharacterRequest\",\"character\":\"a\"}";
    MockInputStream networkIn =
        getNetworkIn(joinRequest + System.lineSeparator() + characterRequest);
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
    boolean characterResponse = false;
    boolean playerState = false;
    for (String message : jsonMessages) {
      if (message.matches(".*\"messageType\":\"CharacterResponse\".*")) {
        assertThat(message.matches(".*\"characterStatus\":(true|false).*"));
        assertThatContainsNKeyValuePairs(message, 2);
        characterResponse = true;
      }
      if (message.matches(".*\"messageType\":\"PlayerStateNotification\".*")) {
        assertThat(message.matches(".*\"accuracy\":(0\\.[0-9]+|1\\.0).*"));
        assertThat(message.matches(".*\"playerName\":\"" + USER1 + "\".*"));
        assertThat(message.matches(".*\"progress\":(0\\.[0-9]+|1\\.0).*"));
        assertThat(message.matches(".*\"wpm\":(0|[1-9][0-9]*).*"));
        assertThatContainsNKeyValuePairs(message, 5);
        playerState = true;
      }
      if (characterResponse && playerState) {
        return;
      }
    }
    Assertions.fail("Missing response for player " + USER1 + " on character request");
  }
}
