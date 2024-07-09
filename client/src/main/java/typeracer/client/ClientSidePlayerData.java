package typeracer.client;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSidePlayerData {

    private String username;
    private int id;
    private final Map<Integer, String> playerNameById = new ConcurrentHashMap<>();
    private final Map<Integer, SimpleBooleanProperty> playerReady = new HashMap<>();
    private Map<Integer, SimpleIntegerProperty> playerWpms = new HashMap<>();
    private Map<Integer, DoubleProperty> playerAccuracies = new HashMap<>();
    private Map<Integer, DoubleProperty> playerProgresses = new HashMap<>();
    private Map<Integer, IntegerProperty> playerErrors = new HashMap<>();
    private ListProperty<String> topPlayers =
            new SimpleListProperty<>(FXCollections.observableArrayList());
    String gameText;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, String> getPlayerNameById() {
        return playerNameById;
    }

    public Map<Integer, SimpleBooleanProperty> getPlayerReady() {
        return playerReady;
    }

    public Map<Integer, SimpleIntegerProperty> getPlayerWpms() {
        return playerWpms;
    }

    public void setPlayerWpms(int playerId, int playerWpm) {
        playerWpms.computeIfAbsent(playerId, k -> new SimpleIntegerProperty()).set(playerWpm);
    }

    public Map<Integer, DoubleProperty> getPlayerAccuracies() {
        return playerAccuracies;
    }

    public void setPlayerAccuracies(int playerId, double playerAccuracy) {
        playerAccuracies.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(playerAccuracy);
    }

    public Map<Integer, DoubleProperty> getPlayerProgresses() {
        return playerProgresses;
    }

    public void setPlayerProgresses(int playerId, double playerProgress) {
        playerProgresses.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(playerProgress);
    }

    public Map<Integer, IntegerProperty> getPlayerErrors() {
        return playerErrors;
    }

    public void setPlayerErrors(Map<Integer, IntegerProperty> playerErrors) {
        this.playerErrors = playerErrors;
    }

    public ObservableList<String> getTopPlayers() {
        return topPlayers.get();
    }

    public ListProperty<String> topPlayersProperty() {
        return topPlayers;
    }

    public void setTopPlayers(ObservableList<String> topPlayers) {
        this.topPlayers.set(topPlayers);
    }

    public String getGameText() {
        return gameText;
    }

    public void setGameText(String gameText) {
        this.gameText = gameText;
    }

}
