package typeracer.server.mockobjects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class MockSocket extends Socket {

  private final InputStream input;
  private final OutputStream output;
  private boolean closed;

  public MockSocket(InputStream input, OutputStream output) {
    this.input = input;
    this.output = output;
  }

  @Override
  public void connect(SocketAddress endpoint) {}

  @Override
  public void connect(SocketAddress endpoint, int timeout) {}

  @Override
  public InputStream getInputStream() throws IOException {
    return input;
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    return output;
  }

  @Override
  public boolean isConnected() {
    return !closed;
  }

  @Override
  public boolean isBound() {
    return true;
  }

  @Override
  public boolean isClosed() {
    return closed;
  }
}
