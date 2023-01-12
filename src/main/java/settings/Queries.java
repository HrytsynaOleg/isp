package settings;

public class Queries {
    private Queries() {
    }
    public static final String GET_COLUMN_NAME_BY_INDEX = "select column_name from information_schema.columns where table_name = ? and ordinal_position = ?";
    //users
    public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE user_email=?";
    public static final String GET_USERS_LIST = "SELECT * FROM users ORDER BY ? %s LIMIT ?,?";
    public static final String GET_USERS_COUNT = "SELECT COUNT(id) FROM users";
    public static final String GET_FIND_USERS_LIST = "SELECT * FROM users WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_FIND_USERS_COUNT = "SELECT COUNT(id) FROM users WHERE %s LIKE ?";
    public static final String UPDATE_USER_PROFILE_BY_ID = "UPDATE users SET user_email=?, user_name=?, " +
            "user_lastname=?, user_phone=?, user_adress=? WHERE id=?";
    public static final String UPDATE_USER_STATUS = "UPDATE users SET user_status=? WHERE id=?";
    public static final String UPDATE_USER_PASSWORD = "UPDATE users SET user_password=? WHERE id=?";
    //services
    public static final String INSERT_SERVICE = "INSERT INTO services VALUES (DEFAULT, ?, ?)";
    public static final String GET_SERVICE_BY_NAME = "SELECT * FROM services WHERE service_name=?";
    public static final String GET_SERVICE_BY_ID = "SELECT * FROM services WHERE id=?";
    public static final String UPDATE_SERVICE_BY_ID = "UPDATE services SET service_name=?, service_description=? WHERE id=?";
    public static final String GET_SERVICES_LIST = "SELECT * FROM services ORDER BY ? %s LIMIT ?,?";
    public static final String GET_FIND_SERVICES_LIST = "SELECT * FROM services WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_SERVICES_COUNT = "SELECT COUNT(id) FROM services";
    public static final String GET_FIND_SERVICES_COUNT = "SELECT COUNT(id) FROM services WHERE %s LIKE ?";
    //tariffs
    public static final String INSERT_TARIFF = "INSERT INTO tarifs VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    public static final String GET_TARIFF_BY_NAME = "SELECT * FROM tarifs WHERE tarif_cost=?";
    public static final String GET_TARIFF_BY_ID = "SELECT * FROM tarifs WHERE id=?";
    public static final String UPDATE_TARIFF_BY_ID = "UPDATE tarifs SET tarif_name=?, tarif_cost=?, " +
            "tarif_description=?, tarif_period=?, tarif_status=? WHERE id=?";
    public static final String GET_TARIFFS_LIST = "SELECT * FROM tarifs ORDER BY ? %s LIMIT ?,?";
    public static final String GET_FIND_TARIFFS_LIST = "SELECT * FROM tarifs WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_TARIFFS_COUNT = "SELECT COUNT(id) FROM tarifs";
    public static final String GET_FIND_TARIFFS_COUNT = "SELECT COUNT(id) FROM tarifs WHERE %s LIKE ?";
    public static final String UPDATE_TARIFF_STATUS = "UPDATE tarifs SET tarif_status=? WHERE id=?";
    public static final String UPDATE_TARIFF_PRICE = "UPDATE tarifs SET tarif_cost=? WHERE id=?";

}
