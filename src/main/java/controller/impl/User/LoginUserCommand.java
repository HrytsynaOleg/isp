package controller.impl.User;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import service.IUserService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import static controller.manager.PathNameManager.*;

public class LoginUserCommand implements ICommand {
    private static final IUserService service = new UserService();
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("login");
        String userPassword = request.getParameter("password");
        String responseText = "";
        HttpSession session = request.getSession();
        try {
            User user = service.getUser(userName,userPassword);
            if (user != null) {
                session.setAttribute("loggedUser", user);
                session.setAttribute("role", user.getRole());
                session.removeAttribute("userLogin");
                session.setAttribute("contentPage", user.getRole().getDashboard());
                return user.getRole().getMainPage();
            }
        } catch (DbConnectionException ex) {
            responseText = "alert.databaseError";
        } catch (NoSuchElementException ex) {
            responseText = "alert.userNotFound";
        }
        session.setAttribute("alert", responseText);
        session.setAttribute("userLogin", userName);
        return getPathName("page.login");
    }
}
