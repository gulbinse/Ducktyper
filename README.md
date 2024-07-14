# Task 5
This program is a multi-player typewriter game.

## Requirements
Java-Version: Java 21
Gradle-Version: Gradle 8.7

## Usage

To start a server:
```
./gradlew :server:run
```

You can provide additional arguments on the command-line with
```
--args"--port PORT"
```

To start a GUI of the Ducktyper game:
```
./gradlew run
```

## JSON protocol for Server-Client communication
The communication between the server and clients (players) is described in a JSON protocol.
The details of this protocol are provided in a separate document:
[Protocol](project_planning/protocol.md)

## Features
- Hidden Ducks!
- Singleplayer support
- Multiplayer support
- Multi-Lobby support
- Player Stats
- Automatic text generation
- Fancy racetracks

## Developers
The Ducktyper game was developed by the following team:

- Alina Pham
- Tobias Daake
- Adrian Moritz
- Ali Khavari
- Eric Gulbins

With support by tutor Lucas Angerer.
