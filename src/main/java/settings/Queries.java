package settings;

public class Queries {
    private Queries() {
    }

    public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE user_email=?";
}
