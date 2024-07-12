package typeracer.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores the Data of all known users for the Client.
 */
public class ClientSideSessionData {

    private String username;
    private int playerId;
    private int sessionId;
    private final Map<Integer, String> playerNamesById = new ConcurrentHashMap<>();
    private final Map<Integer, SimpleBooleanProperty> playerReady = new HashMap<>();
    private final Map<Integer, DoubleProperty> playerWpms = new HashMap<>();
    private final Map<Integer, DoubleProperty> playerAccuracies = new HashMap<>();
    private final Map<Integer, DoubleProperty> playerProgresses = new HashMap<>();
    private Map<Integer, IntegerProperty> playerErrors = new HashMap<>();
    private final ListProperty<String> topPlayers = new SimpleListProperty<>(FXCollections.observableArrayList());
    String gameText;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Map<Integer, String> getPlayerNamesById() {
        return playerNamesById;
    }

    public Map<Integer, SimpleBooleanProperty> getPlayerReady() {
        return playerReady;
    }

    public Map<Integer, DoubleProperty> getPlayerWpms() {
        return playerWpms;
    }

    public void setPlayerWpms(int playerId, double playerWpm) {
        playerWpms.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(playerWpm);
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
        //.replace("\n", "").replace("\r", "")
        //                .replace("\t", "").replace("\r\n", "").replaceAll("\\s+", " ")
    }

    /**
     * Adds A Player to the data.
     *
     * @param playerId   of the joined player
     * @param playerName of the joined player
     */
    public void addPlayer(int playerId, String playerName) {
        playerNamesById.put(playerId, playerName);
        playerReady.put(playerId, new SimpleBooleanProperty(false));
        playerWpms.put(playerId, new SimpleDoubleProperty(0));
        playerAccuracies.put(playerId, new SimpleDoubleProperty(0));
        playerProgresses.put(playerId, new SimpleDoubleProperty(0));
        playerErrors.put(playerId, new SimpleIntegerProperty(0));
    }

    /**
     * Removes a player from the data.
     *
     * @param playerId of the player who left
     */
    public void removePlayer(int playerId) {
        playerNamesById.remove(playerId);
        playerReady.remove(playerId);
        playerWpms.remove(playerId);
        playerAccuracies.remove(playerId);
        playerProgresses.remove(playerId);
        playerErrors.remove(playerId);
        String playerName = playerNamesById.get(playerId);
        if (topPlayers.get().contains(playerName)) {
            topPlayers.remove(playerName);
        }
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
