# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]
- Implementation of bonusfeatures such as
  - Integration of PowerUps
  - Automatic Text generation
  - "Smurf" mode
  - Display number of Typing errors

## [1.0.0] - 2024-07-13

### Added
- Connection between client and GUI: (everyone)
  - Showing players & their ready-status in lobby (#47) (Adrian)
  - `createLobby` method in client (Eric)
  - Methods sending messages in `ViewController` (Alina)
  - Working text display and Ready-Button (Ali, Eric)
- Minimum width and height for GUI windows (#50) (Tobi)
- Automatic text generation from text corpora via markov chain (Eric)
  - Model gets saved automatically when trained to reduce generation time (Eric)
- Better GUI (#45) (everyone)
  - Cool background images (Tobi)
  - Session-ID-Label (Ali, Eric)
  - Default values for username, IP and port (Eric)
  - Enter key as alternative to clicking join (Eric)
  - Show fancy racetracks (Adrian, Eric)
  - Show and update values in game screen (Adrian, Eric)
  - Show current position in text to type (Tobi)
  - Wrapping for inputted text (Ali)
  - Displaying statistics in game results screen (Tobi)
  - Error message for trying to join nonexistent sessions (Eric)
  - Error messages for denied requests (Eric)
  - Hidden ducks as easter egg (Eric)
  - Scalable UI (Eric)
  - Button to copy Session ID (Eric)
  - Prettier displaying of statistics (Adrian, Eric)
- Clientside functionality to join & create sessions (Ali, Eric)
- The players ID added to the `HandshakeResponse` (Adrian)
- Check whether player is ready (Adrian)
- Missing Javadocs (Alina)
- Push-Up message if Server crashes

### Changed
- Cleaned up client and GUI: (Tobi, Eric)
  - Move Data from `ViewController` to `ClientSidePlayerData` (Tobi)
  - Restructured `ViewController` (Tobi, Eric)
- Handle `GameStateNotification`s with `GameState`s instead of `String`s (Eric)
- Renamed "Lobby" to "Session" for consistency (Eric)
- `PlayerJoinedNotification` to `PlayerUpdateNotification` (Adrian)
- Initial Screen button order (Tobi)
- Main Menu screen button design (Tobi)
- increased Text Size of displayed Text in game (Tobi)

### Removed
- unused methods in `ViewController` (Tobi, Eric)
- "Play again" button (Adrian, Eric)
- removed started animation (Tobi)

### Fixed
- Race Condition closing the Socket preemptively (Alina, Adrian)
- Sessions not closing correctly when empty (Adrian)
- WPM calculation wrong by a factor of 20.000.000.000 (Eric)
- WPM still updating when player finished (Eric)
- Accuracy calculation showing wrong value (Eric)
- Game not finishing correctly (Tobi, Eric)
- Game sometimes not starting correctly (Adrian)
- Remove left players from UI (Adrian)
- Show initial prompt on connection loss (Adrian)
- Reset scenes when switching to main menu (Eric)
- Decreasing accuracy if you delete in Game (Tobi)

## [0.2.0] - 2024-06-30

### Added
- Creation of sessions, `TextNotification`, Status codes & reasons in protocol (#30, #31) (Adrian)
- Characters per minute methods in `Player` class (Eric)
- Unit tests for `TextSource` and `TypeRacerGame` (Tobi, Eric)
- `main` class for the server (Adrian)
- Connection & session logic (#23) (Adrian)
- `LeaveSessionRequest` & `LeaveSessionResponse` classes (#40) (Adrian)
- Accuracy support in game logic (Eric)
- Setting custom random texts (with 2 new texts!) (Eric)
- Connection between server and game logic: (#36)
  - Periodic sending of update notifications (Adrian, Eric)
  - Session as mediator between server and game (Adrian, Eric)
- More extensive server & game logic tests (Tobi)
- `MessageHandler` classes in *Chain of Responsibility* to manage requests clientside (Alina)
- Client connection support to server (#44) (Adrian, Alina)

### Changed
- "Lobby" to "Session" renamed (Adrian, Eric)
- More consistent protocol (Adrian)
- IDs look more random (Adrian)
- Move `GameStatus` enum to communication status codes (Tobi)
- Serverside ID usage instead of usernames (Adrian, Tobi, Eric)
- Extracted enums from specific classes (Adrian, Eric)
- Combined server and game packages for better interconnection (Adrian, Tobi, Eric)

### Removed
- `null` values from protocol (#35) (Adrian)
- Unused observer pattern (Eric)
- Faulty getCurrentGameState method from client (#46) (Eric)

## [0.1.0] - 2024-06-23

### Added
- `MessageHandler` classes for character, join and ready requests, using *Chain of Responsibility* (Adrian)
- ID generator (Adrian)
- Skeleton for `Server` class (#16) (Adrian)
- Lobby system & connection classes using the mediator design pattern (Adrian, Tobi, Eric)
- Basic server tests (#20) (Adrian)
- `Main` class for starting the GUI (Ali)
- UI classes: (#19) (Ali)
  - `InitialPromptUi` (Ali)
  - `MainMenuUi` (Ali)
  - `GameUi` (Ali)
  - `GameResultsUi` (Ali)
  - `PlayerStatsUi` (Ali)
  - `ProfileSettingsUi` (Ali)
- `StyleManager` class for consistent theme development (Ali)
- Client & Server communication messages (#17) (Adrian, Alina)
- `MoshiAdapter` class for message conversion (Adrian)
- Lobby support & ready checks in the protocol (#21) (Adrian)
- Basic methods in the `TypeRacerGame` class (#12) (Tobi, Eric)
  - start a game (Tobi, Eric)
  - get the GameState (Tobi)
  - Type a character (Eric)
- Unittests for the `TypeRacerGame` class (Tobi)
- Basic methods in the `Player` class: (#14) (Tobi, Eric)
  - Create a player (Tobi)
  - set its attributes (Tobi)
  - type a character (Eric)
  - Calculate stats and safe them in `PlayerState` (Eric)
- Basic functionality in the `PlayerState` class: (#15) (Tobi, Eric)
  - Progress (Eric)
  - Words per minute (Eric)
  - Ready state (Eric)
  - Finished state (Tobi)
- Basic methods in the `TextSource` class: (Eric)
  - Setting default text (Eric)
  - Setting text from file (Eric)
- Example text file to read the text to type from (Eric)

### Changed
- IDs instead of usernames in the protocol (#21) (Adrian)

### Removed
- `GameController` class (Adrian)

## [0.0.1] - 2024-06-16

### Added
- JSON protocol for Server-Client communication with sequence diagram (#3, #4) (Adrian, Alina)
- `README.md` (Tobi)
- Assignment of tasks for each student (#6) (everyone)
- UML diagram (Ali)
- Information about the GUI (#2) (Ali)
- Information about the game logic (#11) (Tobi, Eric)
- Packages and the relevant classes based on MVC pattern (Adrian)
- `Observable` and `Observer` in `util` package (Tobi)
- `build.gradle` configuration (Adrian)
- UI classes (Ali)
- Javadocs (everyone)

### Changed

### Removed

### Fixed
