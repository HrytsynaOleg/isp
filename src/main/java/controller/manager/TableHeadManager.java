package controller.manager;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class TableHeadManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("tableHead");

    private TableHeadManager() {
    }

    public static String getColumns(String key) {

        return resourceBundle.getString(key);
    }
}

