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

/** Stores the Data of all known users for the Client. */
public class ClientSideSessionData {

  /**
   * Constructs a new ClientSideSessionData instance.
   * Initializes the session data for the client.
   */
  public ClientSideSessionData() {
    // Constructor implementation
  }

  private int sessionId;
  private final Map<Integer, String> playerNamesById = new ConcurrentHashMap<>();
  private final Map<Integer, SimpleBooleanProperty> playerReady = new HashMap<>();
  private final Map<Integer, DoubleProperty> playerWpms = new HashMap<>();
  private final Map<Integer, DoubleProperty> playerAccuracies = new HashMap<>();
  private final Map<Integer, DoubleProperty> playerProgresses = new HashMap<>();
  private Map<Integer, IntegerProperty> playerErrors = new HashMap<>();
  private final ListProperty<String> topPlayers =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  String gameText;

  /**
   * Gets an unmodifiable copy of the player names by their IDs.
   *
   * @return an unmodifiable map of player IDs to player names.
   */
  public Map<Integer, String> getPlayerNamesById() {
    return Map.copyOf(playerNamesById);
  }

  /**
   * Gets the map of player readiness properties.
   *
   * @return a map of player IDs to their readiness properties.
   */
  public Map<Integer, SimpleBooleanProperty> getPlayerReady() {
    return playerReady;
  }

  /**
   * Sets the ready status for a player.
   *
   * @param playerId the ID of the player.
   * @param ready the ready status to set.
   */
  public void setPlayerReady(int playerId, boolean ready) {
    playerReady.computeIfAbsent(playerId, k -> new SimpleBooleanProperty()).set(ready);
  }

  /**
   * Gets the map of player WPM (words per minute) properties.
   *
   * @return a map of player IDs to their WPM properties.
   */
  public Map<Integer, DoubleProperty> getPlayerWpms() {
    return playerWpms;
  }

  /**
   * Sets the WPM for a player.
   *
   * @param playerId the ID of the player.
   * @param playerWpm the WPM to set.
   */
  public void setPlayerWpms(int playerId, double playerWpm) {
    playerWpms.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(playerWpm);
  }

  /**
   * Gets the map of player accuracy properties.
   *
   * @return a map of player IDs to their accuracy properties.
   */
  public Map<Integer, DoubleProperty> getPlayerAccuracies() {
    return playerAccuracies;
  }

  /**
   * Sets the accuracy for a player.
   *
   * @param playerId the ID of the player.
   * @param playerAccuracy the accuracy to set.
   */
  public void setPlayerAccuracies(int playerId, double playerAccuracy) {
    playerAccuracies.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(playerAccuracy);
  }

  /**
   * Gets the map of player progress properties.
   *
   * @return a map of player IDs to their progress properties.
   */
  public Map<Integer, DoubleProperty> getPlayerProgresses() {
    return playerProgresses;
  }

  /**
   * Sets the progress for a player.
   *
   * @param playerId the ID of the player.
   * @param playerProgress the progress to set.
   */
  public void setPlayerProgresses(int playerId, double playerProgress) {
    playerProgresses.computeIfAbsent(playerId, k -> new SimpleDoubleProperty()).set(playerProgress);
  }

  public Map<Integer, IntegerProperty> getPlayerErrors() {
    return Map.copyOf(playerErrors);
  }

  public void setPlayerErrors(Map<Integer, IntegerProperty> playerErrors) {
    this.playerErrors = Map.copyOf(playerErrors);
  }

  /**
   * Gets an unmodifiable list of the top players.
   *
   * @return an unmodifiable observable list of top players.
   */
  public ObservableList<String> getTopPlayers() {
    return FXCollections.unmodifiableObservableList(topPlayers.get());
  }

  public void setTopPlayers(ObservableList<String> topPlayers) {
    this.topPlayers.set(topPlayers);
  }

  /**
   * Gets the game text.
   *
   * @return the game text.
   */
  public String getGameText() {
    return gameText;
  }

  /**
   * Sets the game text.
   *
   * @param gameText the new game text.
   */
  public void setGameText(String gameText) {
    this.gameText = gameText;
    // .replace("\n", "").replace("\r", "")
    //                .replace("\t", "").replace("\r\n", "").replaceAll("\\s+", " ")
  }

  /**
   * Updates the player's information or adds a new player if the player ID is not already present.
   * This method updates the player's name and ready status, and initializes the player's
   * statistics properties (WPM, accuracy, progress, and errors).
   *
   * @param playerId the ID of the player to update.
   * @param playerName the name of the player.
   * @param ready the ready status of the player.
   * @return true if the player is new (not previously present), false otherwise.
   */
  public boolean updatePlayer(int playerId, String playerName, boolean ready) {

    boolean isNew = !playerNamesById.containsKey(playerId);

    playerNamesById.put(playerId, playerName);
    setPlayerReady(playerId, ready);
    playerWpms.put(playerId, new SimpleDoubleProperty(0));
    playerAccuracies.put(playerId, new SimpleDoubleProperty(0));
    playerProgresses.put(playerId, new SimpleDoubleProperty(0));
    playerErrors.put(playerId, new SimpleIntegerProperty(0));

    return isNew;
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

  /**
   * Gets the session ID.
   *
   * @return the session ID.
   */
  public int getSessionId() {
    return sessionId;
  }

  /**
   * Sets the session ID.
   *
   * @param sessionId the session ID to set.
   */
  public void setSessionId(int sessionId) {
    this.sessionId = sessionId;
  }
}
