package service;

import dto.DtoTable;
import dto.DtoUser;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.NotEnoughBalanceException;

import java.util.List;
import java.util.NoSuchElementException;

public interface IUserService {
    User getUser(String userName, String password) throws DbConnectionException;
    User getUserByLogin(String userName) throws DbConnectionException;
    boolean isUserExist(String userName) throws DbConnectionException;
    User addUser(DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException;
    User updateUser (DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException;
    List<User> getUsersList(DtoTable dtoTable) throws DbConnectionException;
    Integer getUsersCount(DtoTable dtoTable) throws DbConnectionException;
    Integer getTotalUsersCount() throws DbConnectionException;
    void blockUser(int userId) throws DbConnectionException;
    void unblockUser(int userId) throws DbConnectionException;
    void setUserPassword(int user, String password, String confirm) throws DbConnectionException, IncorrectFormatException;
}
