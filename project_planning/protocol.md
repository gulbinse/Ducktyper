# JSON Communication Protocol
We use JSON as the communication protocol, every message should be encoded in JSON format. It requires [moshi](https://github.com/square/moshi) in version 1.12.0.

## Client to Server

### HandshakeRequest
The message that asks for connecting.
```json
{
    "messageType":"HandshakeRequest",
    "playerName":<NAME>
}
```
- `String <NAME>`: the client's (player's) name

### CreateSessionRequest
The message that asks for creating a session.
```json
{
    "messageType":"CreateSessionRequest"
}
```

### JoinSessionRequest
The message that asks for joining the session.
```json
{
    "messageType":"JoinSessionRequest",
    "sessionId":<ID>
}
```
- `int <ID>`: the id of the session

### LeaveSessionRequest
The message that asks for leaving the session.
```json
{
    "messageType":"LeaveSessionRequest"
}
```

### ReadyRequest
The message notifying the server that the client's (player's) readiness status has changed.
```json
{
    "messageType":"ReadyRequest",
    "ready":<STATUS>
}
```
- `boolean <STATUS>`: true if the player is ready, false otherwise

### CharacterRequest
The message containing the client's (player's) typed character.
```json
{
    "messageType":"CharacterRequest",
    "character":<CHAR>
}
```
- `char <CHAR>`: the client's (player's) typed character

## Server to Client

### HandshakeResponse
The message notifying the client whether its HandshakeRequest has been accepted.
```json
{
    "messageType":"HandshakeResponse",
    "connectionStatus":<STATUS>,
    "reason":<REASON>
}
```
- `String <STATUS>`: one of `"ACCEPTED"` or `"DENIED"`
- `String <REASON>`: specifies the reason for a denied connection, SUCCESS otherwise

### CreateSessionResponse
The message notifying the client whether its CreateSessionRequest was successful.
```json
{
    "messageType":"CreateSessionResponse",
    "reason":<REASON>,
    "sessionId":<ID>
}
```
- `String <REASON>`: specifies the reason for a denied request, SUCCESS otherwise
- `int <ID>`: the id of the created session, -1 if creation was denied

### JoinSessionResponse
The message notifying the client whether its JoinSessionRequest has been accepted.
```json
{
    "messageType":"JoinSessionResponse",
    "joinStatus":<STATUS>,
    "reason":<REASON>
}
```
- `String <STATUS>`: one of `"ACCEPTED"` or `"DENIED"`
- `String <REASON>`: specifies the reason for a denied connection, SUCCESS otherwise

### LeaveSessionResponse
The message notifying the client whether its LeaveSessionRequest has been accepted.
```json
{
    "messageType":"LeaveSessionResponse",
    "leaveStatus":<STATUS>,
    "reason":<REASON>
}
```
- `String <STATUS>`: one of `"ACCEPTED"` or `"DENIED""`
- `String <REASON>`: specifies the reason for a denied disconnection, SUCCESS otherwise

### ReadyResponse
The message notifying the client whether its ReadyRequest has been accepted.
```json
{
    "messageType":"ReadyResponse",
    "readyStatus":<STATUS>,
    "reason":<REASON>
}
```
- `String <STATUS>`: one of `"ACCEPTED"` or `"DENIED"`
- `String <REASON>`: specifies the reason for a denied request, SUCCESS otherwise

### CharacterResponse
The message notifying the client whether its typed character was correct.
```json
{
    "messageType":"CharacterResponse",
    "correct":<STATUS>
}
```
- `boolean <STATUS>`: true if the character was correct, false otherwise

### PlayerJoinedNotification
The message notifying all clients that a new player has joined the game.
```json
{
    "messageType":"PlayerJoinedNotification",
    "numPlayers":<NUM_PLAYERS>,
    "playerId":<ID>,
    "playerName":<NAME>
}
```
- `int <NUM_PLAYERS>`: the number of players in the game
- `int <ID>`: the id of the player who joined the game
- `String <NAME>`: the name of the player who joined the game

### PlayerLeftNotification
The message notifying all clients that a player has left the game.
```json
{
    "messageType":"PlayerLeftNotification",
    "numPlayers":<NUM_PLAYERS>,
    "playerId":<ID>
}
```
- `int <NUM_PLAYERS>`: the number of players remaining in the game
- `int <ID>`: the id of the player who left the game

### PlayerStateNotification
The message notifying all clients about a player's current state.
```json
{
    "messageType":"PlayerStateNotification",
    "accuracy":<ACCURACY>,
    "playerId":<ID>,
    "progress":<PROGRESS>,
    "wpm":<WPM>
}
```
- `double <ACCURACY>`: the player's accuracy represented as a value between 0.0 (0%) and 1.0 (100%)
- `int <ID>`: the player's id
- `double <PROGRESS>`: the player's current progress represented as a value between 0.0 (0%) and 1.0 (100%)
- `double <WPM>`: the player's average words per minute

### GameStateNotification
The message notifying all clients that the game state has changed.
```json
{
    "messageType":"GameStateNotification",
    "gameStatus":<STATUS>
}
```
- `String <STATUS>`: one of `"RUNNING"` or `"FINISHED"`

### TextNotification
The message notifying all clients that the game is played with a new text.
```json
{
    "messageType":"TextNotification",
    "text":<TEXT>
}
```
- `String <TEXT>`: the current text in the game
