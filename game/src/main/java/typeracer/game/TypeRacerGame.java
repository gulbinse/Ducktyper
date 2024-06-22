package typeracer.game;

/**
 * The main class for the game, managing states and providing an interface for the server.
 */
public class TypeRacerGame {

    private final GameState state;
    private long gameStartTime;

    /**
     * The default constructor of this class.
     */
    public TypeRacerGame(TextSource textSource) {
        state = new GameState(textSource);
    }

    /** Starts a new game with a new text. */
    public void start() {
        // check if all players ready
        for (Player player : state.getPlayers()) {
            if (!player.isReady()) {
                throw new AssertionError("Player " + player.getName() + " not yet ready, but start was attempted");
            }
        }
        state.setStatus(GameState.GameStatus.RUNNING);
        gameStartTime = System.nanoTime();
        // TODO: notify Mediator about game start
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
            state.addPlayer(player);
            // TODO: notify Mediator about added Player
        }
    }

    private boolean validateUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        for (Player p : state.getPlayers()) {
            if (p.getName().equals(username)) {
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
            state.removePlayer(player);
            // TODO: notify Mediator about removed Player
        }
    }

    public Player.TypingResult typeLetter(Player player, char letter) {
        return player.typeLetter(letter, state.getTextSource().getCurrentText(), gameStartTime);
    }
}
