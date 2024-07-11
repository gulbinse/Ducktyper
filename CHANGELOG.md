# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]
- Implementation of bonusfeatures such as
  - Integration of PowerUps
  - Automatic Text generation
  - "Smurf" mode
  - Display number of Typing errors

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
