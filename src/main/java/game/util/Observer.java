package game.util;

import java.util.Observable;

/**
 * Interface for objects that receive updates from an Observable.
 */
public interface Observer {
    /**
     * Called when an Observable is updated.
     *
     * @param o   the observable object
     * @param arg an argument passed to the update method
     */
    void update(Observable o, Object arg);

    /**
     * Called when an Observable is updated without arguments.
     */
    void update();
}
