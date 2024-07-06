package typeracer.server;

import static com.google.common.truth.Truth.assertThat;
import static typeracer.server.TestUtils.getNetworkIn;
import static typeracer.server.TestUtils.getNetworkOut;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import typeracer.server.mockobjects.MockInputStream;
import typeracer.server.mockobjects.MockServerSocket;
import typeracer.server.mockobjects.MockSocket;
import typeracer.server.mockobjects.Sleep;
import typeracer.server.session.Session;
import typeracer.server.session.SessionManager;

@Timeout(7)
public class ServerConnectionTest {

  private static final String USER1 = "User1";
  private static final String USER2 = "User2";
  private static final String USER3 = "User3";

  private void assertThatContainsNKeyValuePairs(String message, int numberOfPairsExpected) {
    assertThat(message).matches("[^:]*:[^:]*".repeat(numberOfPairsExpected));
  }

  @Test
  public void testServer_receivesHandshakeRequest_sendsResponse()
      throws IOException, InterruptedException {
    String handshakeRequest =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER1 + "\"}";
    MockInputStream networkIn = getNetworkIn(handshakeRequest);
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
      if (message.matches(".*\"messageType\":\"HandshakeResponse\".*")) {
        assertThat(message.matches(".*\"connectionStatus\":\"(ACCEPTED|DENIED)\".*"));
        assertThat(message.matches(".*\"reason\":\".*\".*"));
        assertThatContainsNKeyValuePairs(message, 3);
        return;
      }
    }
    Assertions.fail("Missing response for player " + USER1 + " on handshake request");
  }

  @Test
  public void testServer_receivesCreateSessionRequest_sendsResponse()
      throws IOException, InterruptedException {
    String handshakeRequest =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER1 + "\"}";
    String createSessionRequest = "{\"messageType\":\"CreateSessionRequest\"}";
    MockInputStream networkIn =
        getNetworkIn(handshakeRequest + System.lineSeparator() + createSessionRequest);
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
      if (message.matches(".*\"messageType\":\"CreateSessionResponse\".*")) {
        assertThat(message.matches(".*\"reason\":\".*\".*"));
        assertThat(message.matches(".*\"sessionId\":(-1|0|[1-9][0-9]*).*"));
        assertThatContainsNKeyValuePairs(message, 3);
        return;
      }
    }
    Assertions.fail("Missing response for player " + USER1 + " on create session request");
  }

  @Test
  public void testServer_receivesJoinSessionRequest_sendsResponseAndPlayerJoin()
      throws IOException, InterruptedException {
    final int sessionId = 69;
    String handshakeRequest1 =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER1 + "\"}";
    String handshakeRequest2 =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER2 + "\"}";
    String joinSessionRequest =
        "{\"messageType\":\"JoinSessionRequest\",\"sessionId\":" + sessionId + "}";
    MockInputStream networkIn1 =
        getNetworkIn(handshakeRequest1 + System.lineSeparator() + joinSessionRequest);
    MockInputStream networkIn2 =
        getNetworkIn(handshakeRequest2 + System.lineSeparator() + joinSessionRequest);
    ByteArrayOutputStream networkOut1 = getNetworkOut();
    ByteArrayOutputStream networkOut2 = getNetworkOut();
    MockSocket mockSocket1 = new MockSocket(networkIn1, networkOut1);
    MockSocket mockSocket2 = new MockSocket(networkIn2, networkOut2);
    List<MockInputStream> inputs = List.of(networkIn1, networkIn2);
    MockServerSocket serverSocket = new MockServerSocket(List.of(mockSocket1, mockSocket2));

    SessionManager.getInstance().createNewSession(sessionId);
    TestUtils.startServer(serverSocket);
    do {
      Thread.sleep(10);
    } while (!TestUtils.areDone(inputs));
    Thread.sleep(Sleep.BEFORE_TESTING.getMillis());

    String sent = networkOut1.toString(StandardCharsets.UTF_8);
    String[] jsonMessages = sent.split(System.lineSeparator());
    boolean joinSessionResponse = false;
    boolean playerJoinedNotification = false;
    for (String message : jsonMessages) {
      if (message.matches(".*\"messageType\":\"JoinSessionResponse\".*")) {
        assertThat(message.matches(".*\"joinStatus\":\"(ACCEPTED|DENIED)\".*"));
        assertThat(message.matches(".*\"reason\":\".*\".*"));
        assertThatContainsNKeyValuePairs(message, 3);
        joinSessionResponse = true;
      }
      if (message.matches(".*\"messageType\":\"PlayerJoinedNotification\".*")
          && message.matches(".*\"playerName\":\"" + USER2 + "\".*")) {
        assertThat(message.matches(".*\"numPlayers\":2"));
        assertThat(message.matches(".*\"playerId\":(0|[1-9][0-9]*).*"));
        assertThat(message.matches(".*\"playerName\":\"" + USER2 + "\".*"));
        assertThatContainsNKeyValuePairs(message, 4);
        playerJoinedNotification = true;
      }
      if (joinSessionResponse && playerJoinedNotification) {
        return;
      }
    }
    Assertions.fail("Missing response on JoinSessionRequest");
  }

  @Test
  public void testServer_receivesReadyRequest_sendsResponseAndGameState()
      throws IOException, InterruptedException {
    String handshakeRequest =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER1 + "\"}";
    String createSessionRequest = "{\"messageType\":\"CreateSessionRequest\"}";
    String readyRequest = "{\"messageType\":\"ReadyRequest\",\"ready\":true}";
    MockInputStream networkIn =
        getNetworkIn(
            handshakeRequest
                + System.lineSeparator()
                + createSessionRequest
                + System.lineSeparator()
                + readyRequest);
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
        assertThat(message.matches(".*\"reason\":\".*\".*"));
        assertThatContainsNKeyValuePairs(message, 3);
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
    String handshakeRequest =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER1 + "\"}";
    String createSessionRequest = "{\"messageType\":\"CreateSessionRequest\"}";
    String readyRequest = "{\"messageType\":\"ReadyRequest\",\"ready\":true}";
    String characterRequest = "{\"messageType\":\"CharacterRequest\",\"character\":\"A\"}";
    MockInputStream networkIn =
        getNetworkIn(
            handshakeRequest
                + System.lineSeparator()
                + createSessionRequest
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
        assertThat(message.matches(".*\"correct\":(true|false).*"));
        assertThatContainsNKeyValuePairs(message, 2);
        characterResponse = true;
      }
      if (message.matches(".*\"messageType\":\"PlayerStateNotification\".*")) {
        assertThat(message.matches(".*\"accuracy\":(0\\.[0-9]+|1\\.0).*"));
        assertThat(message.matches(".*\"playerName\":\"" + USER1 + "\".*"));
        assertThat(message.matches(".*\"progress\":(0\\.[0-9]+|1\\.0).*"));
        assertThat(message.matches(".*\"wpm\":(0|[1-9][0-9]*)\\.[0-9]+.*"));
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
    final int sessionId = 69;
    String handshakeRequest1 =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER1 + "\"}";
    String handshakeRequest2 =
        "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER2 + "\"}";
    String joinSessionRequest =
        "{\"messageType\":\"JoinSessionRequest\",\"sessionId\":" + sessionId + "}";
    MockInputStream networkIn1 =
        getNetworkIn(handshakeRequest1 + System.lineSeparator() + joinSessionRequest);
    MockInputStream networkIn2 =
        getNetworkIn(handshakeRequest2 + System.lineSeparator() + joinSessionRequest);
    ByteArrayOutputStream networkOut1 = getNetworkOut();
    ByteArrayOutputStream networkOut2 = getNetworkOut();
    MockSocket mockSocket1 = new MockSocket(networkIn1, networkOut1);
    MockSocket mockSocket2 = new MockSocket(networkIn2, networkOut2);
    List<MockInputStream> inputs = List.of(networkIn1, networkIn2);
    MockServerSocket serverSocket = new MockServerSocket(List.of(mockSocket1, mockSocket2));

    SessionManager.getInstance().createNewSession(sessionId);
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
  public void testServer_clientLeavesSessionAndJoinsAnother() throws IOException, InterruptedException {
    final int sessionId1 = 69;
    final int sessionId2 = 420;
    String handshakeRequest1 = "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER1 + "\"}";
    String handshakeRequest2 = "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER2 + "\"}";
    String handshakeRequest3 = "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + USER3 + "\"}";
    String joinSessionRequest1 = "{\"messageType\":\"JoinSessionRequest\",\"sessionId\":" + sessionId1 + "}";
    String joinSessionRequest2 = "{\"messageType\":\"JoinSessionRequest\",\"sessionId\":" + sessionId2 + "}";
    String leaveSessionRequest = "{\"messageType\":\"LeaveSessionRequest\"}";
    MockInputStream networkIn1 = getNetworkIn(handshakeRequest1 + System.lineSeparator() + joinSessionRequest1);
    MockInputStream networkIn2 = getNetworkIn(handshakeRequest2 + System.lineSeparator() + joinSessionRequest2);
    MockInputStream networkIn3 = getNetworkIn(handshakeRequest3 + System.lineSeparator() + joinSessionRequest1 + System.lineSeparator() + leaveSessionRequest + System.lineSeparator() + joinSessionRequest2);
    ByteArrayOutputStream networkOut1 = getNetworkOut();
    ByteArrayOutputStream networkOut2 = getNetworkOut();
    ByteArrayOutputStream networkOut3 = getNetworkOut();
    MockSocket mockSocket1 = new MockSocket(networkIn1, networkOut1);
    MockSocket mockSocket2 = new MockSocket(networkIn2, networkOut2);
    MockSocket mockSocket3 = new MockSocket(networkIn3, networkOut3);
    List<MockInputStream> inputs = List.of(networkIn1, networkIn2, networkIn3);
    MockServerSocket serverSocket = new MockServerSocket(List.of(mockSocket1, mockSocket2, mockSocket3));

    SessionManager.getInstance().createNewSession(sessionId1);
    SessionManager.getInstance().createNewSession(sessionId2);
    TestUtils.startServer(serverSocket);
    do {
      Thread.sleep(10);
    } while (!TestUtils.areDone(inputs));
    Thread.sleep(Sleep.BEFORE_TESTING.getMillis());

    // Test session1
    boolean test1 = false;
    String sent1 = networkOut1.toString(StandardCharsets.UTF_8);
    String[] jsonMessages1 = sent1.split(System.lineSeparator());
    for (String message : jsonMessages1) {
      if (message.matches(".*\"messageType\":\"PlayerLeftNotification\".*")) {
        test1 = true;
        break;
      }
    }

    // Test session2
    boolean test2 = false;
    String sent2 = networkOut2.toString(StandardCharsets.UTF_8);
    String[] jsonMessages2 = sent2.split(System.lineSeparator());
    for (String message : jsonMessages2) {
      if (message.matches(".*\"messageType\":\"PlayerJoinedNotification\".*") && message.matches(".*\"playerName\":\"" + USER3 + "\".*")) {
        test2 = true;
        break;
      }
    }

    if (!test1 || !test2) {
      Assertions.fail("Player " + USER3 + " cannot jump from session to session");
    }
  }

  @Test
  public void testServer_multipleClientConnections() throws IOException, InterruptedException {
    final int numUsers = Session.MAX_SIZE;
    final int sessionId = 69;
    List<MockSocket> sockets = new ArrayList<>(numUsers);
    List<ByteArrayOutputStream> outputs = new ArrayList<>(numUsers);
    List<MockInputStream> inputs = new ArrayList<>(numUsers);
    for (int i = 1; i <= numUsers; i++) {
      String user = "U" + i;
      String handshakeRequest =
          "{\"messageType\":\"HandshakeRequest\",\"playerName\":\"" + user + "\"}";
      String joinSessionRequest =
          "{\"messageType\":\"JoinSessionRequest\",\"sessionId\":" + sessionId + "}";
      MockInputStream networkIn =
          getNetworkIn(handshakeRequest + System.lineSeparator() + joinSessionRequest);
      ByteArrayOutputStream networkOut = getNetworkOut();
      inputs.add(networkIn);
      outputs.add(networkOut);
      sockets.add(new MockSocket(networkIn, networkOut));
    }
    MockServerSocket serverSocket = new MockServerSocket(sockets);

    SessionManager.getInstance().createNewSession(sessionId);
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
          assertThat(message.matches(".*\"playerId\":(0|[1-9][0-9]*).*"));
          assertThat(message.matches(".*\"playerName\":\"" + USER2 + "\".*"));
          assertThatContainsNKeyValuePairs(message, 4);
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
