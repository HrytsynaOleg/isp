package dao.impl;

import connector.DbConnectionPool;
import dao.IUserDao;
import entity.User;
import exeptions.DbConnectionExeption;
import settings.Queries;

import java.sql.*;

public class UserDaoImpl implements IUserDao {
    @Override
    public int addUser(User user) throws DbConnectionExeption {

        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.setString(4, user.getStatus().toString());
            statement.setString(5, user.getName());
            statement.setString(6, user.getLastName());
            statement.setString(7, user.getPhone());
            statement.setString(8, user.getAdress());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            user.setId(keys.getInt(1));
            return keys.getInt(1);

        } catch (SQLException e) {
            throw new DbConnectionExeption("Add user database error",e);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        return null;
    }
}
