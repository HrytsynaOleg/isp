package controller.manager;

import java.util.ResourceBundle;

public class TableHeadManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("tableHead");

    private TableHeadManager() {
    }

    public static String getColumns(String key) {

        return resourceBundle.getString(key);
    }
}

