package service;

import dto.DtoUser;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;

import java.util.NoSuchElementException;

public interface IUserService {
    User getUser(String userName, String password) throws DbConnectionException;
    boolean isUserExist(String userName) throws DbConnectionException, NoSuchElementException;
    User addUser(DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException;
    User updateUser (DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException;
}
