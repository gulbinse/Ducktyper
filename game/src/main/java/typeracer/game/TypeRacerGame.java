package typeracer.game;

import typeracer.game.observable.Observable;
import typeracer.game.observable.Observer;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * The main class for the game, managing states and providing an interface for the server.
 */
public class TypeRacerGame implements Observable {

    private ConcurrentLinkedQueue<Observer> observers = new ConcurrentLinkedQueue<>();

    private volatile GameState state;

    /**
     * The default constructor of this class.
     */
    public TypeRacerGame() {
        state = new GameState();
    }

    /** Starts a new game with a new text. */
    public void start() {
        state = state.nextRound(state.getNextText());
        notifyAboutState(state);
    }

    /**
     * Adds a player to the game.
     *
     * @param player that will be added to the game
     */
    public void addPlayer(Player player) {
        if (!validateUsername(player.getName())) {
            throw new AssertionError(
                    "An invalid player name reached the game logic. This should be handled before. Name: "
                            + player.getName());
        }
        synchronized (this) {
            state = state.addPlayer(player);
            subscribe(player);
            notifyAboutNewPlayer(player.getName(), state);
        }
    }

    private boolean validateUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        for (Player p : state.getPlayers()) {
            if (p.getName().equals(playerName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes a player from the game.
     *
     * @param player that will be removed
     */
    public void removePlayer(Player player) {
        synchronized (this) {
            state = state.removePlayer(player);
            unsubscribe(player);
            notifyAboutRemovedPlayer(player.getName(), state);
        }
    }

    /**
     * Subscribe a player to the game notifications.
     *
     * @param obs the observer to be added
     */
    @Override
    public void subscribe(Observer obs) {
        if (observers.contains(obs)) {
            throw new AssertionError("Observer " + obs + " already part of observers");
        }
        observers.add(obs);
    }

    /**
     * Unsubscribes a player from the game notifications.
     *
     * @param obs the observer to be removed
     */
    @Override
    public void unsubscribe(Observer obs) {
        observers.remove(obs);
        if (observers.contains(obs)) {
            throw new AssertionError("Observer " + obs + " still part of observers");
        }
    }

    /**
     * Notifies all subscribed players about a new game state.
     * @param newState the new GameState
     */
    @Override
    public void notifyAboutState(GameState newState) {
        updateAll(o -> {
            try {
                o.updateState(state);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Notifies all subscribed players that a new player has joined the game.
     *
     * @param playerName the name of the player who joined the game
     * @param newState the new GameState after the player joined
     */
    @Override
    public void notifyAboutNewPlayer(String playerName, GameState newState) {
        updateAll(o -> {
            try {
                o.updateNewPlayer(playerName, newState);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Notifies all subscribed players that a player has been removed from the game.
     *
     * @param playerName the name of the player who has left the game
     * @param newState the new GameState after the player left
     */
    @Override
    public void notifyAboutRemovedPlayer(String playerName, GameState newState) {
        updateAll(o -> {
            try {
                o.updateRemovedPlayer(playerName, newState);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateAll(Consumer<Observer> toCall) {
        for (Observer o : observers) {
            toCall.accept(o);
        }
    }
}
