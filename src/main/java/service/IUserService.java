package service;

import dto.DtoUser;
import entity.User;
import enums.SortOrder;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;

import java.util.List;
import java.util.NoSuchElementException;

public interface IUserService {
    User getUser(String userName, String password) throws DbConnectionException;
    boolean isUserExist(String userName) throws DbConnectionException, NoSuchElementException;
    User addUser(DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException;
    User updateUser (DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException;
    List<User> getUsersList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder) throws DbConnectionException;
    List<User> getFindUsersList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria) throws DbConnectionException;
    Integer getUsersCount() throws DbConnectionException;
    Integer getFindUsersCount(int field, String criteria) throws DbConnectionException;
    void setUserStatus(int user, String status) throws DbConnectionException;
    void setUserPassword(int user, String password, String confirm) throws DbConnectionException, IncorrectFormatException;
}
