package typeracer.server;

import static com.google.common.truth.Truth.assertThat;
import static typeracer.server.TestUtils.getNetworkIn;
import static typeracer.server.TestUtils.getNetworkOut;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
  private static final String USER2 = "User2";

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
        assertThat(message.matches(".*\"numPlayers\":1.*"));
        assertThat(message.matches(".*\"playerName\":\"" + USER1 + "\".*"));
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
  public void testServer_receivesReadyRequest_sendsResponseAndGameState()
      throws IOException, InterruptedException {
    String joinRequest = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + USER1 + "\"}";
    String readyRequest = "{\"messageType\":\"ReadyRequest\",\"ready\":true}";
    MockInputStream networkIn = getNetworkIn(joinRequest + System.lineSeparator() + readyRequest);
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
    boolean readyResponse = false;
    boolean gameStateNotification = false;
    for (String message : jsonMessages) {
      if (message.matches(".*\"messageType\":\"ReadyResponse\".*")) {
        assertThat(message.matches(".*\"readyStatus\":\"(ACCEPTED|DENIED)\".*"));
        assertThatContainsNKeyValuePairs(message, 2);
        readyResponse = true;
      }
      if (message.matches(".*\"messageType\":\"GameStateNotification\".*")) {
        assertThat(message.matches(".*\"gameStatus\":\"RUNNING\".*"));
        assertThatContainsNKeyValuePairs(message, 2);
        gameStateNotification = true;
      }
      if (readyResponse && gameStateNotification) {
        return;
      }
    }
    Assertions.fail("No response for player " + USER1 + " on ready request");
  }

  @Test
  public void testServer_receivesCharacterRequest_sendsResponseAndPlayerState()
      throws IOException, InterruptedException {
    String joinRequest = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + USER1 + "\"}";
    String readyRequest = "{\"messageType\":\"ReadyRequest\",\"ready\":true}";
    String characterRequest = "{\"messageType\":\"CharacterRequest\",\"character\":'a'}";
    MockInputStream networkIn =
        getNetworkIn(
            joinRequest
                + System.lineSeparator()
                + readyRequest
                + System.lineSeparator()
                + characterRequest);
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

  @Test
  public void testServer_clientUnreachable_sendsPlayerLeft()
      throws IOException, InterruptedException {
    String joinRequest1 = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + USER1 + "\"}";
    String joinRequest2 = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + USER2 + "\"}";
    MockInputStream networkIn1 = getNetworkIn(joinRequest1);
    MockInputStream networkIn2 = getNetworkIn(joinRequest2);
    ByteArrayOutputStream networkOut1 = getNetworkOut();
    ByteArrayOutputStream networkOut2 = getNetworkOut();
    MockSocket mockSocket1 = new MockSocket(networkIn1, networkOut1);
    MockSocket mockSocket2 = new MockSocket(networkIn2, networkOut2);
    List<MockInputStream> inputs = List.of(networkIn1, networkIn2);
    MockServerSocket serverSocket = new MockServerSocket(List.of(mockSocket1, mockSocket2));

    TestUtils.startServer(serverSocket);
    do {
      Thread.sleep(10);
    } while (!TestUtils.areDone(inputs));
    Thread.sleep(Sleep.BEFORE_TESTING.getMillis());
    networkIn2.finish();
    Thread.sleep(Sleep.BEFORE_TESTING.getMillis());

    String sent = networkOut1.toString(StandardCharsets.UTF_8);
    String[] jsonMessages = sent.split(System.lineSeparator());
    for (String message : jsonMessages) {
      if (message.matches(".*\"messageType\":\"PlayerLeftNotification\".*")) {
        assertThat(message.matches(".*\"numPlayers\":1.*"));
        assertThat(message.matches(".*\"playerName\":\"" + USER2 + "\".*"));
        assertThatContainsNKeyValuePairs(message, 3);
        return;
      }
    }
    Assertions.fail("No PlayerLeftNotification for player " + USER2);
  }

  @Test
  public void testServer_multipleClientConnections() throws IOException, InterruptedException {
    final int numUsers = 20;
    List<MockSocket> sockets = new ArrayList<>(numUsers);
    List<ByteArrayOutputStream> outputs = new ArrayList<>(numUsers);
    List<MockInputStream> inputs = new ArrayList<>(numUsers);
    for (int i = 1; i <= numUsers; i++) {
      String user = "U" + i;
      String joinRequest = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"" + user + "\"}";
      MockInputStream networkIn = getNetworkIn(joinRequest);
      ByteArrayOutputStream networkOut = getNetworkOut();
      inputs.add(networkIn);
      outputs.add(networkOut);
      sockets.add(new MockSocket(networkIn, networkOut));
    }
    MockServerSocket serverSocket = new MockServerSocket(sockets);

    TestUtils.startServer(serverSocket);
    do {
      Thread.sleep(10);
    } while (!TestUtils.areDone(inputs));
    Thread.sleep(Sleep.BEFORE_TESTING.getMillis());

    for (int i = 1; i <= numUsers; i++) {
      boolean found = false;
      String sent = outputs.get(i - 1).toString(StandardCharsets.UTF_8);
      String[] jsonMessages = sent.split(System.lineSeparator());
      for (String message : jsonMessages) {
        if (message.matches(".*\"messageType\":\"PlayerJoinedNotification\".*")
            && message.matches(".*\"playerName\":\"U" + outputs.size() + "\".*")) {
          assertThat(message.matches(".*\"numPlayers\":([1-9]|1[0-9]|20).*"));
          assertThatContainsNKeyValuePairs(message, 3);
          found = true;
          break;
        }
      }
      Assertions.assertTrue(
          found,
          "User U" + i + " did not receive PlayerJoinedNotification for user U" + outputs.size());
    }
  }
}
