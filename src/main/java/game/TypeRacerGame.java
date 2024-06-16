package game;

import game.util.Observable;
import game.util.Observer;

public class TypeRacerGame implements Observable {
    @Override
    public void subscribe(Observer obs) {

    }

    @Override
    public void unsubscribe(Observer obs) {

    }

    @Override
    public void notifyAboutState(GameState newState) {

    }

    @Override
    public void notifyAboutNewPlayer(String playerName, GameState newState) {

    }

    @Override
    public void notifyAboutRemovedPlayer(String playerName, GameState newState) {

    }
}
