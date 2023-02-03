package controller.impl.user;

import controller.ICommand;
import entity.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static settings.properties.PathNameManager.*;

public class LogoutUserCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(LogoutUserCommand.class);
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        session.invalidate();
        logger.info(String.format("User %s logged out", user.getEmail()));
        return getPathName("page.login");
    }
}
