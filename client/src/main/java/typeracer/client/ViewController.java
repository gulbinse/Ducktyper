package typeracer.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import typeracer.client.view.GameResultsUi;
import typeracer.client.view.GameUi;
import typeracer.client.view.InitialPromptUi;
import typeracer.client.view.LobbyUi;
import typeracer.client.view.MainMenuUi;
import typeracer.client.view.PlayerStatsUi;
import typeracer.client.view.ProfileSettingsUi;

/**
 * Manages the transition between different scenes and states in the TypeRacer game application.
 */
public class ViewController {

    /**
     * Enum representing the different views available in the TypeRacer game application.
     */
    public enum ViewName {
        /**
         * The initial prompt view.
         */
        INITIAL_PROMPT,

        /**
         * The main menu view.
         */
        MAIN_MENU,

        /**
         * The game view.
         */
        GAME,

        /**
         * The player statistics view.
         */
        STATS,

        /**
         * The profile settings view.
         */
        PROFILE_SETTINGS,

        /**
         * The game results view.
         */
        GAME_RESULTS,

        /**
         * The lobby view.
         */
        LOBBY
    }

    /**
     * The width of the application window.
     */
    private static final int DEFAULT_WINDOW_WIDTH = 800;

    /**
     *
     * The height of the application window.
     */
    private static final int DEFAULT_WINDOW_HEIGHT = 600;

    /**
     * A map of view names to their corresponding scenes.
     */
    private static Map<ViewName, Scene> views;

    /**
     * The primary stage of the application.
     */
    private static Stage stage;

    /**
     * The client handling the backend communication.
     */
    private static Client client;


    /**
     * The username of the current user.
     */
    private String username;

    private InitialPromptUi initialPromptUi;


    /**
     * A list property of the top players' usernames.
     */
    private ListProperty<String> topPlayers =
            new SimpleListProperty<>(FXCollections.observableArrayList());



    private ClientSideSessionData playerData = new ClientSideSessionData();
    /**
     * Constructs a ViewController with a given stage and client. Initializes the view mappings and
     * sets up the initial views.
     *
     * @param stage  The primary stage of the application.
     * @param client The client handling the backend communication.
     */
    public ViewController(Stage stage, Client client) {
        this.stage = stage;
        views = new HashMap<>();
        this.client = client;
        initializeViews();
        initializeTopPlayers();
        addDummyPlayer();
    }

    private void addDummyPlayer() {
        addPlayerToGame(1, "dummyPlayer");
    }

    /**
     * Connects to the server with the given IP address and port number.
     *
     * @param ip   The IP address of the server.
     * @param port The port number of the server.
     * @throws IOException If an I/O error occurs when attempting to connect to the server.
     */
    public void connectToServer(String ip, int port, String username) throws IOException {
        //TODO: add Logic, that makes Client connect to Server and transfer username
        System.out.println("Connected to server at " + ip + ":" + port);
        this.username = username;
    }

    public void joinLobby(int lobbyId){
        //TODO: add Logic, that makes Client send a JoinLobbyRequest to Server
        System.out.println("Request to join lobby " + lobbyId);
        //For Testing purpose only:
        startGame();
    }

    public void setPlayerReady(boolean isReady) {
        System.out.println("Player wants to update his readyStatus to: " + isReady);
        //TODO: add Logic, that makes Client send a ReadyRequest to Server
        //For Testing purpose only:
        startNewGame();
    }

    public void handleCharacterTyped(char character) {
        System.out.println("Character typed: " + character);
        //TODO: add Logic, that makes Client send a CharacterRequest to Server
    }


    /**
     * Sets the username for the current session.
     *
     * @param username The username of the player.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the current username.
     *
     * @return The username of the player.
     */
    public String getUsername() {
        return playerData.getUsername();
    }

    /**
     * Returns the current player's ID.
     *
     * @return The ID of the current player.
     */
    public int getCurrentPlayerId() {
        return playerData.getId();
    }

    /**
     * Returns an observable list of player usernames.
     *
     * @return An observable list containing the usernames of the players.
     */
    public ObservableList<String> getPlayerUsernames() {
        return FXCollections.observableArrayList(playerData.getPlayerNameById().values());
    }

    /**
     * Initializes the different views used in the application.
     */
    private void initializeViews() {
        addView(ViewName.INITIAL_PROMPT, new InitialPromptUi(this, stage));
        addView(ViewName.MAIN_MENU, new MainMenuUi(this));
        addView(ViewName.GAME, new GameUi(this));
        addView(ViewName.STATS, new PlayerStatsUi(this));
        addView(ViewName.PROFILE_SETTINGS, new ProfileSettingsUi(this));
        addView(ViewName.GAME_RESULTS, new GameResultsUi(this));
        addView(ViewName.LOBBY, new LobbyUi(this));
    }

    /**
     * Helper method to add views to the map.
     */
    private void addView(ViewName viewName, Parent ui) {
        views.put(viewName, new Scene(ui, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
    }

    /**
     * Starts a new game by fetching the game text and updating the UI accordingly.
     */
    //TODO: This method should be called by Client on receiving a GameStateNotification with GameStatus == Running
    public void startNewGame() {
        switchToGameUi();
    }

    /**
     * Adds a player to the game on client side
     *
     * @param playerId of joined player
     * @param playerName of joined player
     */
    //TODO: this method should be called by Client on receiving a PlayerJoinedNotification
    public void addPlayerToGame(int playerId, String playerName) {
        playerData.addPlayer(playerId, playerName);
    }

    //TODO: this method should be called by Client on receiving a PlayerLeftNotification
    public void removePlayerFromGame(int playerId){
        playerData.removePlayer(playerId);
    }

    /**
     * Ends the current game, updates stats, and switches the UI to display game results.
     */
    //TODO: This method should be called by view on receiving a GameStateNotification with GameStatus == Finished
    public void endGame() {
        switchToGameResultUi();
    }

    //TODO: This method should be called by view on receiving a CharacterResponse
    public void handleCharacterAnswer(boolean isCorrect) {

    }

    public String getGameText() {
        return playerData.getGameText();
    }

    /**
     * Updates the game text with the specified new text.
     *
     * @param newText The new game text to set.
     */
    //TODO: This method should be called by view on receiving a TextNotification. We have to make sure, that we always have a text when the game starts
    public void setGameText(String newText) {
        playerData.setGameText(newText);
    }

    /**
     *
     * @param playerId
     * @param playerWpm
     * @param playerAccuracy
     * @param playerProgress
     */
    //TODO: This method should be called by Client, when it receives a PlayerStateNotification
    public void updatePlayerStateInformation(int playerId, int playerWpm, double playerAccuracy, double playerProgress) {
        playerData.setPlayerWpms(playerId, playerWpm);
        playerData.setPlayerAccuracies(playerId, playerAccuracy);
        playerData.setPlayerProgresses(playerId, playerProgress);
    }

    /**
     * Returns the WPM property for the specified player ID.
     *
     * @param playerId The ID of the player.
     * @return The WPM property for the player.
     */
    public IntegerProperty getPlayerWpmProperty(int playerId) {
        return playerData.getPlayerWpms().computeIfAbsent(playerId, k -> new SimpleIntegerProperty());
    }

    /**
     * Returns the accuracy property for the specified player ID.
     *
     * @param playerId The ID of the player.
     * @return The accuracy property for the player.
     */
    public DoubleProperty getPlayerAccuracyProperty(int playerId) {
        return playerData.getPlayerAccuracies().computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
    }

    /**
     * Returns the progress property for the specified player ID.
     *
     * @param playerId The ID of the player.
     * @return The progress property for the player.
     */
    public DoubleProperty getPlayerProgressProperty(int playerId) {
        return playerData.getPlayerProgresses().computeIfAbsent(playerId, k -> new SimpleDoubleProperty());
    }

    /**
     * Initializes the list of top players.
     */
    private void initializeTopPlayers() {
        List<String> players = client.getTopPlayers();
        topPlayers.set(FXCollections.observableArrayList(players));
    }

    public ListProperty<String> topPlayersProperty() {
        return topPlayers;
    }

    /**
     * Changes the current scene to the specified view.
     *
     * @param viewName The name of the view to display.
     */
    public static void showView(ViewName viewName) {
        Scene scene = views.get(viewName);
        if (scene != null) {
            stage.setScene(scene);
            stage.show();
            if (scene.getRoot() instanceof LobbyUi) {
                ((LobbyUi) scene.getRoot()).onViewShown();
            } else if (scene.getRoot() instanceof GameUi) {
                ((GameUi) scene.getRoot()).onViewShown();
            }
        } else {
            System.err.println("View not found: " + viewName);
        }
    }

    /**
     * Adds a scene to the views map with a specified name.
     *
     * @param name  The name to assign to the scene.
     * @param scene The scene to add.
     */
    public void addScene(ViewName name, Scene scene) {
        views.put(name, scene);
    }

    /**
     * Displays the lobby view.
     */
    public void startGame() {
        showView(ViewName.LOBBY);
    }

    /**
     * Displays the statistics view.
     */
    public void viewStats() {
        showView(ViewName.STATS);
    }

    /**
     * Displays the profile settings view.
     */
    public void editProfile() {
        showView(ViewName.PROFILE_SETTINGS);
    }

    /**
     * Switches the current scene to the main menu.
     */
    public void switchToMainMenu() {
        showView(ViewName.MAIN_MENU);
    }

    /**
     * Switches the current scene to the lobby UI.
     */
    // Was ist der Unterschied zwischen der Methode und startGame(); ?
    public static void switchToLobbyUi() {
        showView(ViewName.LOBBY);
        LobbyUi lobbyUi = (LobbyUi) views.get(ViewName.LOBBY).getRoot();
        if (lobbyUi != null) {
            lobbyUi.onViewShown();
        }
    }

    /**
     * Switches the current scene to the game UI.
     */
    public static void switchToGameUi() {
        showView(ViewName.GAME);
        GameUi gameUi = (GameUi) views.get(ViewName.GAME).getRoot();
        if (gameUi != null) {
            gameUi.onViewShown();
        }
    }

    /**
     * Switches the current scene to the game results UI.
     */
    public static void switchToGameResultUi() {
        showView(ViewName.GAME_RESULTS);
    }

    /**
     * Saves user settings and switches back to the main menu.
     *
     * @param username     The username to save.
     * @param wpmGoal      The words per minute goal.
     * @param favoriteText The favorite text of the user.
     */
    public void saveUserSettings(String username, int wpmGoal, String favoriteText) {
//        client.saveSettings(username, wpmGoal, favoriteText);
        showView(ViewName.MAIN_MENU);
    }

    /**
     * Cancels any changes made in the settings and returns to the main menu.
     */
    public void cancelSettings() {
        showView(ViewName.MAIN_MENU);
    }

    /**
     * Resets the user's game statistics.
     */
    public static void handleResetStats() {
        client.resetStats();
        PlayerStatsUi statsUi = getPlayerStatsUi();
        statsUi.clearDisplayedStats();
    }

    /**
     * Gets the PlayerStatsUi from the views map.
     *
     * @return PlayerStatsUi instance if available.
     */
    private static PlayerStatsUi getPlayerStatsUi() {
        return (PlayerStatsUi) views.get(ViewName.STATS).getRoot();
    }
}
