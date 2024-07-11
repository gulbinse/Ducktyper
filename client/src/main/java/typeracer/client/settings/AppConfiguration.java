package typeracer.client.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfiguration {
  private static final Properties properties = new Properties();
  private static final String CONFIG_FILE = "config.properties";

  static {
    loadProperties();
  }

  private static void loadProperties() {
    try (InputStream inputStream =
        AppConfiguration.class.getResourceAsStream("config.properties")) {
      if (inputStream == null) {
        throw new IOException("Cannot find config.properties file");
      }
      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load configuration properties", e);
    }
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }

  // access with --> String backendUrl = Configuration.getProperty("backend.url");
}
