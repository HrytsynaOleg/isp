package service;

import exceptions.IncorrectFormatException;

public interface IValidatorService {
    void validateString (String string, String regex, String message) throws IncorrectFormatException;
    void validateConfirmPassword(String password, String confirm, String message) throws IncorrectFormatException;
}
