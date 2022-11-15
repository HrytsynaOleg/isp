package settings;

public class Queries {
    private Queries() {
    }

    public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
}
