package typeracer.server.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import typeracer.communication.messages.Message;
import typeracer.communication.messages.MoshiAdapter;
import typeracer.server.message.MessageHandlerChain;

/**
 * This class represents a client connected to the server. It handles incoming and outgoing messages
 * and implements the Runnable interface to be scheduled in another thread.
 */
public class ClientHandler implements Runnable {

  private static final Charset UTF_8 = StandardCharsets.UTF_8;

  private final MessageHandlerChain messageHandlerChain = new MessageHandlerChain();
  private final MoshiAdapter moshiAdapter = new MoshiAdapter();

  private final Socket socket;
  private final int clientId;

  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;

  private ClientHandler(Socket socket, int clientId) {
    this.socket = socket;
    this.clientId = clientId;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
      bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8));
    } catch (IOException e) {
      System.out.println("Input and output streams could not be created: " + e.getMessage());
      close();
    }
  }

  /**
   * Creates a new ClientHandler instance with the specified arguments and a unique id.
   *
   * @param socket the socket to which this handler should be bound
   * @param clientId the unique id of the client/player
   * @return a new ClientHandler instance
   */
  public static ClientHandler create(Socket socket, int clientId) {
    return new ClientHandler(socket, clientId);
  }

  @Override
  public void run() {
    try {
      String json;
      while ((json = bufferedReader.readLine()) != null && socket.isConnected()) {
        Message message = moshiAdapter.fromJson(json);
        handleMessage(message);
      }
    } catch (IOException e) {
      System.out.println("Client with ID " + clientId + " lost connection: " + e.getMessage());
    } finally {
      close();
    }
  }

  private void handleMessage(Message message) {
    messageHandlerChain.handleMessage(message, clientId);
  }

  /**
   * Sends a message to the client.
   *
   * @param message the message to be sent
   */
  void sendMessage(Message message) {
    try {
      String json = moshiAdapter.toJson(message);
      bufferedWriter.write(json);
      bufferedWriter.newLine();
      bufferedWriter.flush();
    } catch (IOException e) {
      System.out.println("An error occurred trying to send the message: " + e.getMessage());
      close();
    }
  }

  /** Closes the connection to the client. */
  void close() {
    ConnectionManager.getInstance().unhandleClient(clientId);
    try {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (bufferedWriter != null) {
        bufferedWriter.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      System.out.println("An error occurred trying to close the connection: " + e.getMessage());
    }
  }
}
