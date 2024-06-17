package game.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of observers and notifies them of changes.
 */
public class Observable {
    private List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the list.
     *
     * @param observer the observer to add
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of a change.
     *
     * @param arg an argument passed to the observers
     */
    protected void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
