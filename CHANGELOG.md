# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2024-06-19

### Added
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

## [0.0.1] - 2024-06-16

### Added
- JSON protocol for Server-Client communication with sequence diagram (#3, #4).
- README.md.
- Assignment of tasks for each student (#6).
- UML diagram.
- Information about the GUI (#2).
- Information about the game logic (#11).
- Packages and the relevant classes based on MVC pattern.
- Observable and Observer in util package.
- build.gradle configuration.
- UI Classes.
- Javadoc.

### Changed

### Removed

### Fixed
