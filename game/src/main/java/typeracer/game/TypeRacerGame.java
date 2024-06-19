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

    public void start() {
        state = state.nextRound(state.getNextText());
        notifyAboutState(state);
    }

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

    public void removePlayer(Player player) {
        synchronized (this) {
            state = state.removePlayer(player);
            unsubscribe(player);
            notifyAboutRemovedPlayer(player.getName(), state);
        }
    }

    @Override
    public void subscribe(Observer obs) {
        if (observers.contains(obs)) {
            throw new AssertionError("Observer " + obs + " already part of observers");
        }
        observers.add(obs);
    }

    @Override
    public void unsubscribe(Observer obs) {
        observers.remove(obs);
        if (observers.contains(obs)) {
            throw new AssertionError("Observer " + obs + " still part of observers");
        }
    }

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
