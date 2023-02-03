package settings.properties;

import java.util.ResourceBundle;

public class PathNameManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("pages");
    private PathNameManager() { }
    public static String getPathName(String key) {
        return resourceBundle.getString(key);
    }
}
