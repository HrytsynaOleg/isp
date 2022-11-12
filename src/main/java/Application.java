import dao.IUserDao;
import dao.impl.UserDaoImpl;

public class Application {
    public static void main(String[] args) {
        IUserDao userDao= new UserDaoImpl();
        userDao.addUser();
    }
}
