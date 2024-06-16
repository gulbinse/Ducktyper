package game.util;

import java.util.Observable;

public interface Observer {
    void update(Observable o, Object arg);

    void update();
}
