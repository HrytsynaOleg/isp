package settings;

public class Queries {
    private Queries() {
    }
    public static final String GET_COLUMN_NAME_BY_INDEX = "select column_name from information_schema.columns where table_name = ? and ordinal_position = ?";
    //users
    public static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE user_email=?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id=?";
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
    public static final String GET_ALL_SERVICES_LIST = "SELECT * FROM services";
    public static final String GET_FIND_SERVICES_LIST = "SELECT * FROM services WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_SERVICES_COUNT = "SELECT COUNT(id) FROM services";
    public static final String GET_FIND_SERVICES_COUNT = "SELECT COUNT(id) FROM services WHERE %s LIKE ?";
    public static final String DELETE_SERVICE_BY_ID = "DELETE FROM services WHERE id=?";
    //tariffs
    public static final String INSERT_TARIFF = "INSERT INTO tarifs VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    public static final String GET_TARIFF_BY_NAME = "SELECT * FROM tarifs WHERE tarif_cost=?";
    public static final String GET_TARIFF_BY_ID = "SELECT * FROM tarifs WHERE id=?";
    public static final String UPDATE_TARIFF_BY_ID = "UPDATE tarifs SET tarif_name=?, tarif_cost=?, " +
            "tarif_description=?, tarif_period=?, tarif_status=? WHERE id=?";
    public static final String GET_TARIFFS_LIST = "SELECT * FROM tarifs ORDER BY ? %s LIMIT ?,?";
    public static final String GET_PRICE_TARIFFS_LIST = "SELECT * FROM tarifs WHERE tarif_status='ACTIVE'";
    public static final String GET_FIND_TARIFFS_LIST = "SELECT * FROM tarifs WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_TARIFFS_COUNT = "SELECT COUNT(id) FROM tarifs";
    public static final String GET_FIND_TARIFFS_COUNT = "SELECT COUNT(id) FROM tarifs WHERE %s LIKE ?";
    public static final String UPDATE_TARIFF_STATUS = "UPDATE tarifs SET tarif_status=? WHERE id=?";
    public static final String UPDATE_TARIFF_PRICE = "UPDATE tarifs SET tarif_cost=? WHERE id=?";
    public static final String GET_USER_TARIFFS_LIST = "SELECT tarifs.*, utr.status as is_user_subscribed FROM tarifs LEFT JOIN " +
            "(SELECT  * FROM usertarif WHERE users_id=?) as utr ON tarifs.id=utr.tarifs_id ORDER BY ? %s LIMIT ?,?";
    public static final String GET_FIND_USER_TARIFFS_LIST = "SELECT tarifs.*, utr.status as is_user_subscribed FROM tarifs LEFT JOIN " +
            "(SELECT  * FROM usertarif WHERE users_id=?) as utr ON tarifs.id=utr.tarifs_id WHERE %s LIKE ? ORDER BY ? %s LIMIT ?,?";

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
    public static final String GET_ACTIVE_USER_TARIFFS = "SELECT tarifs.*, usertarif.status, usertarif.date_end FROM usertarif " +
            "RIGHT JOIN tarifs ON usertarif.tarifs_id=tarifs.id WHERE (usertarif.status='ACTIVE' OR usertarif.status='PAUSED' OR usertarif.status='BLOCKED') " +
            "AND users_id=?";
    public static final String GET_EXPIRED_ACTIVE_USER_TARIFFS = "SELECT * FROM usertarif WHERE usertarif.status='ACTIVE' AND usertarif.date_end<=?";
    public static final String GET_SUBSCRIBED_USER_TARIFFS = "SELECT * FROM usertarif WHERE (status='ACTIVE' OR status='PAUSED') AND users_id=?";
    public static final String GET_BLOCKED_USER_TARIFFS = "SELECT * FROM usertarif WHERE status='BLOCKED' AND users_id=?";
    //payments
    public static final String INSERT_PAYMENT = "INSERT INTO payments VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    public static final String GET_USER_BALANCE = "SELECT id, user_balance FROM users WHERE id=?";
    public static final String UPDATE_USER_BALANCE = "UPDATE users SET user_balance=? WHERE id=?";
    public static final String GET_USER_PAYMENTS_LIST = "SELECT * FROM payments WHERE users_id=? AND type=? ORDER BY ? %s LIMIT ?,?";
    public static final String GET_USER_PAYMENTS_COUNT = "SELECT COUNT(id) FROM payments WHERE users_id=? AND type=?";

}
