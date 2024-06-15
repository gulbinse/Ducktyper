# JSON Communication Protocol
We use JSON as the communication protocol, every message should be encoded in JSON format. It requires [moshi](https://github.com/square/moshi) in version 1.12.0.

## Client to Server

### JoinGameRequest
The message that asks for joining the game.
```json
{"messageType":"JoinGameRequest","playerName":<NAME>}
```
- String <NAME>: the client's (player's) name

### CharacterRequest
The message containing the client's (player's) typed character.
```json
{"messageType":"CharacterRequest","character":<CHAR>}
```
- String <CHAR>: the client's (player's) typed character

## Server to Client

### JoinGameResponse
The message notifying the client whether its JoinGameRequest has been accepted.
```json
{"messageType":"JoinGameResponse","joinStatus":<STATUS>,"reason":<REASON>}
```
- String <STATUS>: one of "ACCEPTED" or "DENIED"
- String <REASON>: specifies the reason for a denied connection, null otherwise

### CharacterResponse
The message notifying the client whether its typed character was correct.
```json
{"messageType":"CharacterResponse","characterStatus":<STATUS>}
```
- boolean <STATUS>: true if the character was correct, false otherwise

### PlayerJoinedNotification
The message notifying all clients that a new player has joined the game.
```json
{"messageType":"PlayerJoinedNotification","numPlayers":<NUM_PLAYERS>,"playerName":<NAME>}
```
- int <NUM_PLAYERS>: the number of players in the game
- String <NAME>: the name of the player who joined the game

### PlayerLeftNotification
The message notifying all clients that a player has left the game.
```json
{"messageType":"PlayerLeftNotification","numPlayers":<NUM_PLAYERS>,"playerName":<NAME>}
```
- int <NUM_PLAYERS>: the number of players remaining in the game
- String <NAME>: the name of the player who left the game

### PlayerStateNotification
The message notifying all clients about a player's current state.
```json
{"messageType":"PlayerStateNotification","accuracy":<ACCURACY>,"playerName":<NAME>,"progress":<PROGRESS>,"wpm":<WPM>}
```
- double <ACCURACY>: the player's accuracy represented as a value between 0.0 (0%) and 1.0 (100%)
- String <NAME>: the player's name
- double <PROGRESS>: the player's current progress represented as a value between 0.0 (0%) and 1.0 (100%)
- double <WPM>: the player's average words per minute

### GameStateNotification
The message notifying all clients that the game state has changed.
```json
{"messageType":"GameStateNotification","gameStatus":<STATUS>}
```
- String <STATUS>: one of "RUNNING" or "FINISHED"