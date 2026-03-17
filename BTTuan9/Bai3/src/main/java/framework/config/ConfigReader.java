package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Singleton đọc config-{env}.properties từ classpath.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        String env = System.getProperty("env", "dev");
        String file = "config-" + env + ".properties";
        properties = new Properties();
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(file)) {
            if (is == null) throw new RuntimeException("Không tìm thấy: " + file);
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc config: " + file, e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) instance = new ConfigReader();
            }
        }
        return instance;
    }

    public static void resetInstance() { instance = null; }

    public String getBaseUrl() { return properties.getProperty("base.url"); }
    public String getBrowser() { return properties.getProperty("browser", "chrome"); }
}
