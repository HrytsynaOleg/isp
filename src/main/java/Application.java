import dao.IUserDao;
import dao.impl.UserDaoImpl;
import entity.User;
import exceptions.DbConnectionException;

public class Application {
    public static void main(String[] args) {
        IUserDao userDao = new UserDaoImpl();
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
        } catch (DbConnectionException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
        System.out.println();
    }
}
