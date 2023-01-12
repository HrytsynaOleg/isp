package controller.impl.user;

import controller.ICommand;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.*;

public class MainPageCommand implements ICommand {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        if (user != null) {
            session.setAttribute("contentPage", user.getDashboard());
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
