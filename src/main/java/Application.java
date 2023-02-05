import dependecies.DependencyManager;
import repository.IUserRepository;
import repository.impl.UserRepositoryImpl;
import entity.User;
import exceptions.DbConnectionException;

import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        IUserRepository userDao = DependencyManager.userRepo;
//        UserBuilder builder = new UserBuilder();
//        builder.setUserEmail("user@test.com");
//        builder.setUserPassword("mypass");
//        builder.setUserRole(UserRole.ADMIN);
//        builder.setUserStatus(UserStatus.ACTIVE);
//        builder.setUserName("Oleg");
//        builder.setUserLastName("Hero");
//
//        try {
//            int result = userDao.addUser(builder.build());
//            System.out.println(result);
//        } catch (DbConnectionExeption e) {
//            System.out.println(e.getMessage() + " " + e.getCause().toString());
//        }

        try {
            User userByLogin = userDao.getUserByLogin("user@test.com");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
        System.out.println();
    }
}
