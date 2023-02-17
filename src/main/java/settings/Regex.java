package settings;

public class Regex {
    public static final String EMAIL_REGEX = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
//    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=\\S+$).{8,20}$";
    public static final String NAME_REGEX = "^[A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'\\- ]{1,30}";
    public static final String PHONE_NUMBER_REGEX ="\\+380[0-9]{9}";
    public static final String DECIMAL_NUMBER_REGEX ="^\\d*$|(?=^.*$)^\\d+\\.\\d{0,2}$";
}
