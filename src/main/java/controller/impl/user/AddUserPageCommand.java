package controller.impl.user;

import controller.ICommand;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class AddUserPageCommand implements ICommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        UserRole user = (UserRole) session.getAttribute("role");
        if (user == null) {
            session.invalidate();
            return getPathName("page.login");
        }
        session.setAttribute("contentPage", getPathName("content.addUserPage"));
        return user.getMainPage();
    }
}
