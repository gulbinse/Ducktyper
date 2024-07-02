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
- Creation of sessions in protocol (#31)
- Status codes & reasons in protocol (#30)
- Characters per minute methods in ``Player`` class
- Unit tests for ``TextSource`` and `TypeRacerGame`
- ``TextNotification`` in protocol
- ``main`` class for the server
- Implementation of ``MessageHandler`` classes
- Connection & session logic (#23)

### Changed
- "Lobby" to "Session" renamed
- More consistent protocol
- IDs look more random

### Removed
- ``null`` values from protocol (#35)

## [0.1.0] - 2024-06-23

### Added
- ``MessageHandler`` classes for character, join and ready requests, using *Chain of Responsibility*
- ID generator
- Skeleton for ``Server`` class (#16)
- Lobby system & connection classes using the mediator design pattern
- Basic server tests (#20)
- ``Main`` class for starting the GUI
- UI classes: (#19)
  - ``InitialPromptUi``
  - ``MainMenuUi``
  - ``GameUi``
  - ``GameResultsUi``
  - ``PlayerStatsUi``
  - ``ProfileSettingsUi``
- ``StyleManager`` class for consistent theme development
- JavaFX 21.0.3 in ``build.gradle``
- Client & Server communication messages (#17)
- ``MoshiAdapter`` class for message conversion
- Lobby support & ready checks in the protocol (#21)
- Basic methods in the ``TypeRacerGame`` class (#12)
  - start a game
  - get the GameState
  - Type a character
- Unitests for the ''TypeRacerGame'' class
- Basic methods in the ``Player`` class: (#14)
  - Create a player
  - set its attributes
  - type a Character
  - Calculate Stats and safe them in ''PlayerState''
- Basic functionality in the ``PlayerState`` class: (#15)
  - Progress
  - Words per minute
  - Ready state
  - Finished state
- Basic methods in the ``TextSource`` class:
  - Setting default text
  - Setting text from file
- Example text file to read the text to type from

### Changed
- IDs instead of usernames in the protocol (#21)

### Removed
- ``GameController`` class

## [0.0.1] - 2024-06-16

### Added
- JSON protocol for Server-Client communication with sequence diagram (#3, #4).
- ``README.md``
- Assignment of tasks for each student (#6)
- UML diagram
- Information about the GUI (#2)
- Information about the game logic (#11)
- Packages and the relevant classes based on MVC pattern
- ``Observable`` and ``Observer`` in ``util`` package
- ``build.gradle`` configuration
- UI classes
- Javadocs

### Changed

### Removed

### Fixed
