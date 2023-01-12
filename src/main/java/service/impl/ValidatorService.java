package service.impl;

import exceptions.IncorrectFormatException;
import service.IValidatorService;

public class ValidatorService implements IValidatorService {
    @Override
    public void validateString(String string, String regex, String message) throws IncorrectFormatException {
        if (string == null || !string.matches(regex) || isEmptyString(string))
            throw new IncorrectFormatException(message);
    }

    @Override
    public void validateEmptyString(String string, String message) throws IncorrectFormatException {
        if (isEmptyString(string))
            throw new IncorrectFormatException(message);
    }

    public void validateConfirmPassword(String password, String confirm, String message) throws IncorrectFormatException {
        if (!password.equals(confirm))
            throw new IncorrectFormatException(message);
    }

    private boolean isEmptyString (String string) {
        if (string!=null) return string.trim().length()==0;
        return true;
    }

}
