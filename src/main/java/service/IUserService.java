package service;

import entity.User;
import exeptions.DbConnectionExeption;

public interface IUserService {
    User validateUser(String userName) throws DbConnectionExeption;
}
