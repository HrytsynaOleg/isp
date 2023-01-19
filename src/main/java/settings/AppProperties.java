package settings;

import java.io.IOException;
import java.util.Properties;

public class AppProperties {
    private static final Properties properties = new Properties();
    static {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            properties.load(classLoader.getResourceAsStream("app.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
