package typeracer.communication.messages;

/**
 * Classes implementing this interface represent messages used for client-server communication and
 * support the serialization of their instances to JSON strings and deserialization from JSON
 * strings. Subclasses must be added to the {@link
 * com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory} in {@link MoshiAdapter} to ensure
 * seamless conversion.
 */
public interface Message {}
