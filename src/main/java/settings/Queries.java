package settings;

public class Queries {
    private Queries() {
    }

    public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE user_email=?";
    public static final String GET_USERS_LIST = "SELECT * FROM users ORDER BY ? %s LIMIT ?,?";
    public static final String GET_USERS_COUNT = "SELECT COUNT(id) FROM users";
    public static final String GET_FIND_USERS_LIST = "SELECT * FROM users WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_FIND_USERS_COUNT = "SELECT COUNT(id) FROM users WHERE %s LIKE ?";
    public static final String GET_COLUMN_NAME_BY_INDEX = "select column_name from information_schema.columns where table_name = ? and ordinal_position = ?";
    public static final String UPDATE_USER_PROFILE_BY_ID = "UPDATE users SET user_email=?, user_name=?, " +
            "user_lastname=?, user_phone=?, user_adress=? WHERE id=?";
    public static final String UPDATE_USER_STATUS = "UPDATE users SET user_status=? WHERE id=?";
    public static final String UPDATE_USER_PASSWORD = "UPDATE users SET user_password=? WHERE id=?";
}
