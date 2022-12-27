package service.impl;

import exceptions.IncorrectFormatException;
import service.IValidatorService;

public class ValidatorService implements IValidatorService {
    @Override
    public void validateString(String string, String regex, String message) throws IncorrectFormatException {
        if (string == null || !string.matches(regex))
            throw new IncorrectFormatException(message);
    }
    public void validateConfirmPassword(String password, String confirm, String message) throws IncorrectFormatException {
        if (!password.equals(confirm))
            throw new IncorrectFormatException(message);
    }
}
