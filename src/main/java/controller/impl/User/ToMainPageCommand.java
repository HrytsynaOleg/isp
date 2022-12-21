package controller.impl.User;

import controller.ICommand;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static controller.manager.PathNameManager.*;

public class ToMainPageCommand implements ICommand {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        if (user!=null) return user.getMainPage();

        return getPathName("page.login");
    }
}
