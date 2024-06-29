package typeracer.communication.messages;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import java.io.IOException;
import typeracer.communication.messages.client.CharacterRequest;
import typeracer.communication.messages.client.JoinGameRequest;
import typeracer.communication.messages.client.JoinLobbyRequest;
import typeracer.communication.messages.client.ReadyRequest;
import typeracer.communication.messages.server.CharacterResponse;
import typeracer.communication.messages.server.GameStateNotification;
import typeracer.communication.messages.server.JoinGameResponse;
import typeracer.communication.messages.server.JoinLobbyResponse;
import typeracer.communication.messages.server.PlayerJoinedNotification;
import typeracer.communication.messages.server.PlayerLeftNotification;
import typeracer.communication.messages.server.PlayerStateNotification;
import typeracer.communication.messages.server.ReadyResponse;

/**
 * This class is used for message conversion. Every {@link Message} has to be registered in the
 * {@link PolymorphicJsonAdapterFactory} of this class to ensure seamless conversion.
 */
public class MoshiAdapter {

  private final JsonAdapter<Message> jsonAdapter;

  /** Constructs a new MoshiAdapter. */
  public MoshiAdapter() {
    Moshi moshi =
        new Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Message.class, "messageType")
                    .withSubtype(CharacterRequest.class, "CharacterRequest")
                    .withSubtype(JoinGameRequest.class, "JoinGameRequest")
                    .withSubtype(JoinLobbyRequest.class, "JoinLobbyRequest")
                    .withSubtype(ReadyRequest.class, "ReadyRequest")
                    .withSubtype(CharacterResponse.class, "CharacterResponse")
                    .withSubtype(GameStateNotification.class, "GameStateNotification")
                    .withSubtype(JoinGameResponse.class, "JoinGameResponse")
                    .withSubtype(JoinLobbyResponse.class, "JoinLobbyResponse")
                    .withSubtype(PlayerJoinedNotification.class, "PlayerJoinedNotification")
                    .withSubtype(PlayerLeftNotification.class, "PlayerLeftNotification")
                    .withSubtype(PlayerStateNotification.class, "PlayerStateNotification")
                    .withSubtype(ReadyResponse.class, "ReadyResponse"))
            .build();
    jsonAdapter = moshi.adapter(Message.class);
  }

  /**
   * Converts a {@link Message} to a JSON string.
   *
   * @param message the message to be converted
   * @return the JSON string representation of the provided Message object
   */
  public String toJson(Message message) {
    return jsonAdapter.toJson(message);
  }

  /**
   * Converts a JSON string to a {@link Message} object.
   *
   * @param json the JSON string to be converted
   * @return the Message object parsed from the JSON string
   * @throws IOException if the JSON string cannot be parsed into a Message object
   */
  public Message fromJson(String json) throws IOException {
    return jsonAdapter.fromJson(json);
  }
}
