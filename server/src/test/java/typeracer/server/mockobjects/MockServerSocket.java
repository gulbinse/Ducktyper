package typeracer.server.mockobjects;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class MockServerSocket extends ServerSocket {

  private boolean isClosed;
  private Iterator<MockSocket> sockets;

  public MockServerSocket(List<MockSocket> socketsToAccept) throws IOException {
    sockets = socketsToAccept.iterator();
  }

  @Override
  public Socket accept() {
    if (sockets.hasNext()) {
      try {
        Thread.sleep(Sleep.BETWEEN_SOCKET_ACCEPTS.getMillis());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return sockets.next();
    }
    synchronized (this) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public void waitOnFinish() throws InterruptedException {
    synchronized (this) {
      wait();
    }
  }

  public void finish() {
    synchronized (this) {
      notifyAll();
    }
  }

  @Override
  public void close() {
    synchronized (this) {
      notifyAll();
    }
    isClosed = true;
  }

  @Override
  public boolean isClosed() {
    return isClosed;
  }
}
