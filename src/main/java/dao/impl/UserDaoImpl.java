package dao.impl;

import connector.DbConnectionPool;
import dao.IUserDao;
import entity.User;
import settings.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoImpl implements IUserDao {
    @Override
    public void addUser() {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_USER);
            statement.setString(1, "new user");
            int count = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        return null;
    }
}
