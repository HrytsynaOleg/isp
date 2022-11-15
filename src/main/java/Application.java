import dao.IUserDao;
import dao.impl.UserDaoImpl;
import entity.builder.UserBuilder;
import enums.UserRole;
import enums.UserStatus;
import exeptions.DbConnectionExeption;

public class Application {
    public static void main(String[] args) {
        IUserDao userDao = new UserDaoImpl();
        UserBuilder builder = new UserBuilder();
        builder.setUserEmail("user@test.com");
        builder.setUserPassword("mypass");
        builder.setUserRole(UserRole.ADMIN);
        builder.setUserStatus(UserStatus.ACTIVE);
        builder.setUserName("Oleg");
        builder.setUserLastName("Hero");

        try {
            int result = userDao.addUser(builder.build());
            System.out.println(result);
        } catch (DbConnectionExeption e) {
            System.out.println(e.getMessage() + " " + e.getCause().toString());
        }
        System.out.println();
    }
}
