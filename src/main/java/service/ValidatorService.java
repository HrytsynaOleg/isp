package service;

import exceptions.IncorrectFormatException;

public class ValidatorService {

    private ValidatorService() {
    }

    public static void validateString(String string, String regex, String message) throws IncorrectFormatException {
        if (string == null || !string.matches(regex) || isEmptyString(string))
            throw new IncorrectFormatException(message);
    }

    public static void validateStringDuplicates(String first, String second, String message) throws IncorrectFormatException {
        if (first.equals(second))
            throw new IncorrectFormatException(message);
    }

    public static void validateEmptyString(String string, String message) throws IncorrectFormatException {
        if (isEmptyString(string))
            throw new IncorrectFormatException(message);
    }

    public static void validateConfirmPassword(String password, String confirm, String message) throws IncorrectFormatException {
        if (!password.equals(confirm))
            throw new IncorrectFormatException(message);
    }

    private static boolean isEmptyString(String string) {
        if (string != null) return string.trim().length() == 0;
        return true;
    }

}
