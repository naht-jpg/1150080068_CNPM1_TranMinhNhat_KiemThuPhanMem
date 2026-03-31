package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Đọc cấu hình từ file config-{env}.properties.
 * Sử dụng Singleton pattern để đảm bảo chỉ load file một lần.
 *
 * File properties đặt trong src/test/resources/:
 *   config-dev.properties
 *   config-staging.properties
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        String env = System.getProperty("env", "dev");
        String fileName = "config-" + env + ".properties";
        properties = new Properties();

        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("Không tìm thấy file cấu hình: " + fileName);
            }
            properties.load(is);
            System.out.println("▶ Đã load cấu hình từ: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file cấu hình: " + fileName, e);
        }
    }

    /**
     * Lấy instance duy nhất (Singleton). Thread-safe với double-checked locking.
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /**
     * Reset instance khi môi trường thay đổi (dùng trong @BeforeMethod).
     */
    public static void resetInstance() {
        instance = null;
    }

    public String getBaseUrl()    { return properties.getProperty("base.url"); }
    public String getBrowser()    { return properties.getProperty("browser", "chrome"); }
    public String getUsername()   { return properties.getProperty("username"); }
    public String getPassword()   { return properties.getProperty("password"); }
    public int    getTimeout()    { return Integer.parseInt(properties.getProperty("timeout", "10")); }

    public String get(String key) { return properties.getProperty(key); }
}
