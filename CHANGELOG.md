# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.0] - 2024-06-30

### Added
- Creation of sessions in protocol (#31)

### Changed
- "Lobby" to "Session" renamed
- More consistent protocol

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
- Implementation of the basic Methods in the TypeRacerGame class including:
    + TypeRacerGame();
    + start();
    + addPlayer(Player player);
    + -boolean validateUsername(String username);
    + removePlayer(Player player);
- Implementation of the Observable methods in the TypeRacerGame class:
    + subscribe(Observer obs);
    + unsubscribe(Observer obs);
    + notifyAboutState(GameState newState);
    + notifyAboutNewPlayer(String playerName, GameState newState)
    + notifyAboutRemovedPlayer(String playerName, GameState newState)
    + -updateAll(Consumer<Observer> toCall);

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
