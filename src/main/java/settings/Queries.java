package settings;

public class Queries {
    private Queries() {
    }
    public static final String GET_COLUMN_NAME_BY_INDEX = "select column_name from information_schema.columns where table_name = ? and ordinal_position = ?";
    //users
    public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE user_email=?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    public static final String GET_USERS_LIST = "SELECT * FROM users";
    public static final String GET_USERS_COUNT = "SELECT COUNT(id) FROM users";
    public static final String UPDATE_USER_PROFILE_BY_ID = "UPDATE users SET user_email=?, user_name=?, " +
            "user_lastname=?, user_phone=?, user_adress=? WHERE id=?";
    public static final String UPDATE_USER_STATUS = "UPDATE users SET user_status=? WHERE id=?";
    public static final String UPDATE_USER_PASSWORD = "UPDATE users SET user_password=? WHERE id=?";
    //services
    public static final String INSERT_SERVICE = "INSERT INTO services VALUES (DEFAULT, ?, ?)";
    public static final String GET_SERVICE_BY_NAME = "SELECT * FROM services WHERE service_name=?";
    public static final String GET_SERVICE_BY_ID = "SELECT * FROM services WHERE id=?";
    public static final String UPDATE_SERVICE_BY_ID = "UPDATE services SET service_name=?, service_description=? WHERE id=?";
    public static final String GET_SERVICES_LIST = "SELECT * FROM services";
    public static final String GET_ALL_SERVICES_LIST = "SELECT * FROM services";
    public static final String GET_FIND_SERVICES_LIST = "SELECT * FROM services WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_SERVICES_COUNT = "SELECT COUNT(id) FROM services";
    public static final String GET_TARIFFS_COUNT_FIND_BY_FIELD = "SELECT COUNT(id) FROM tarifs WHERE %s LIKE ?";
    public static final String GET_FIND_SERVICES_COUNT = "SELECT COUNT(id) FROM services WHERE %s LIKE ?";
    public static final String DELETE_SERVICE_BY_ID = "DELETE FROM services WHERE id=?";
    //tariffs
    public static final String INSERT_TARIFF = "INSERT INTO tarifs VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    public static final String GET_TARIFF_BY_NAME = "SELECT * FROM tarifs WHERE tarif_cost=?";
    public static final String GET_TARIFF_BY_ID = "SELECT * FROM tarifs WHERE id=?";
    public static final String UPDATE_TARIFF_BY_ID = "UPDATE tarifs SET tarif_name=?, tarif_cost=?, " +
            "tarif_description=?, tarif_period=?, tarif_status=? WHERE id=?";
    public static final String GET_TARIFFS_COUNT = "SELECT COUNT(id) FROM tarifs";
    public static final String GET_TARIFFS_LIST = "SELECT * FROM tarifs";
    public static final String GET_PRICE_TARIFFS_LIST = "SELECT * FROM tarifs WHERE tarif_status='ACTIVE'";
    public static final String UPDATE_TARIFF_STATUS = "UPDATE tarifs SET tarif_status=? WHERE id=?";
    public static final String UPDATE_TARIFF_PRICE = "UPDATE tarifs SET tarif_cost=? WHERE id=?";
    public static final String DELETE_TARIFF_BY_ID = "DELETE FROM tarifs WHERE id=?";

    //user tariffs
    public static final String INSERT_USER_TARIFF = "INSERT INTO usertarif VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    public static final String UPDATE_USER_TARIFF_STATUS = "UPDATE usertarif SET status=? WHERE id=?";
    public static final String UPDATE_USER_TARIFF_END_DATE = "UPDATE usertarif SET date_end=? WHERE id=?";
    public static final String GET_USER_TARIFF_COUNT = "SELECT COUNT(id) FROM usertarif WHERE users_id=? AND tarifs_id=?";
    public static final String GET_USER_TARIFF_END_DATE = "SELECT date_end FROM usertarif WHERE id=?";
    public static final String GET_USER_TARIFF_START_DATE = "SELECT date_begin FROM usertarif WHERE id=?";
    public static final String GET_USER_TARIFF_STATUS = "SELECT status FROM usertarif WHERE id=?";
    public static final String GET_USER_TARIFF = "SELECT id FROM usertarif WHERE users_id=? AND tarifs_id=?";
    public static final String GET_USER_TARIFFS_BY_SERVICE_ID = "SELECT tarifs.* FROM usertarif " +
            "RIGHT JOIN tarifs ON usertarif.tarifs_id=tarifs.id WHERE services_id=? AND users_id=?";
    public static final String DELETE_USER_TARIFF = "DELETE FROM usertarif WHERE id=?";
    public static final String GET_USER_TARIFFS_BY_USER = "SELECT * FROM usertarif RIGHT JOIN tarifs ON usertarif.tarifs_id=tarifs.id WHERE users_id=?";
    public static final String GET_ALL_USER_TARIFFS = "SELECT * FROM usertarif RIGHT JOIN tarifs ON usertarif.tarifs_id=tarifs.id WHERE status='ACTIVE'";
    public static final String GET_ACTIVE_USER_TARIFFS_COUNT = "SELECT COUNT(usertarif.id) FROM usertarif RIGHT JOIN tarifs ON usertarif.tarifs_id=tarifs.id " +
            "WHERE (usertarif.status='ACTIVE' OR usertarif.status='PAUSED' OR usertarif.status='BLOCKED') " +
            "AND users_id=?";
    public static final String GET_EXPIRED_ACTIVE_USER_TARIFFS = "SELECT * FROM usertarif WHERE usertarif.status='ACTIVE' AND usertarif.date_end<=?";
    public static final String GET_SUBSCRIBED_USER_TARIFFS = "SELECT * FROM usertarif WHERE (status='ACTIVE' OR status='PAUSED') AND users_id=?";
    public static final String GET_BLOCKED_USER_TARIFFS = "SELECT * FROM usertarif WHERE status='BLOCKED' AND users_id=?";
    public static final String GET_SUBSCRIBERS_BY_TARIFF = "SELECT * FROM usertarif WHERE (status='ACTIVE' OR status='PAUSED') AND tarifs_id=?";
    //payments
    public static final String INSERT_PAYMENT = "INSERT INTO payments VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    public static final String GET_USER_BALANCE = "SELECT id, user_balance FROM users WHERE id=?";
    public static final String UPDATE_USER_BALANCE = "UPDATE users SET user_balance=? WHERE id=?";
    public static final String GET_USER_PAYMENTS_LIST = "SELECT payments.*, users.user_email, users.user_name, users.user_lastname, users.user_phone " +
            "FROM payments INNER JOIN users ON payments.users_id=users.id WHERE type=? AND users_id=?";
    public static final String GET_ALL_PAYMENTS_LIST = "SELECT payments.*, users.user_email, users.user_name, users.user_lastname, users.user_phone " +
            "FROM payments INNER JOIN users ON payments.users_id=users.id WHERE type=?";
    public static final String GET_USER_PAYMENTS_COUNT = "SELECT COUNT(id) FROM payments WHERE type=? AND users_id=?";
    public static final String GET_ALL_PAYMENTS_COUNT = "SELECT COUNT(id) FROM payments WHERE type=?";

}
