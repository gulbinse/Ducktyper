package typeracer.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/** Stores the Data of all known users for the Client. */
public class ClientSideSessionData {

  /**
   * Constructs a new ClientSideSessionData instance. Initializes the session data for the client.
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
   * Retrieves or creates a property to track whether a player is ready.
   *
   * @param playerId the ID of the player.
   * @return a BooleanProperty reflecting the player's ready status.
   */
  public BooleanProperty getPlayerReadyProperty(int playerId) {
    return playerReady.computeIfAbsent(playerId, k -> new SimpleBooleanProperty());
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
   * Returns the WPM property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The WPM property for the player.
   */
  public DoubleProperty getPlayerWpmProperty(int playerId) {
    return playerWpms.computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
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
   * Returns the accuracy property for the specified player ID.
   *
   * @param playerId The ID of the player.
   * @return The accuracy property for the player.
   */
  public DoubleProperty getPlayerAccuracyProperty(int playerId) {
    return playerAccuracies.computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
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
   * Gets the progress property of the player with the specified ID. If the player's progress
   * property does not exist, it is created and initialized to 0.0.
   *
   * @param playerId the ID of the player.
   * @return the DoubleProperty representing the player's progress.
   */
  public DoubleProperty getPlayerProgressProperty(int playerId) {
    return playerProgresses.computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
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
   * This method updates the player's name and ready status, and initializes the player's statistics
   * properties (WPM, accuracy, progress, and errors).
   *
   * @param playerId the ID of the player to update.
   * @param playerName the name of the player.
   * @param ready the ready status of the player.
   * @return true if the player is new (not previously present), false otherwise.
   */
  public boolean updatePlayer(int playerId, String playerName, boolean ready) {

    final boolean isNew = !playerNamesById.containsKey(playerId);

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
