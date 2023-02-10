package controller.impl.user;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

import static settings.properties.PathNameManager.*;

public class LoginUserCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(LoginUserCommand.class);
    private final IUserService userService;

    public LoginUserCommand(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("login");
        String userPassword = request.getParameter("password");
        String responseText;
        HttpSession session = request.getSession();
        try {
            User user = userService.getUser(userName, userPassword);
            if (user != null) {
                session.setAttribute("loggedUser", user);
                session.setAttribute("role", user.getRole());
                session.removeAttribute("userLogin");
                session.setAttribute("contentPage", user.getRole().getDashboard());
                logger.info(String.format("User %s logged in", userName));
                return "controller?command=mainPage";
            } else throw new NoSuchElementException();

        } catch (DbConnectionException ex) {
            logger.error(ex.getMessage());
            responseText = "alert.databaseError";
        } catch (NoSuchElementException ex) {
            logger.error(ex.getMessage());
            responseText = "alert.userNotFound";
        }
        session.setAttribute("alert", responseText);
        session.setAttribute("userLogin", userName);
        return getPathName("page.login");
    }

}
