package controller.impl.user;

import controller.ICommand;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class AddUserPageCommand implements ICommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        UserRole user = (UserRole) session.getAttribute("role");
        session.setAttribute("contentPage", getPathName("content.addUserPage"));
        if (user != null) return user.getMainPage();
        session.setAttribute("contentPage", null);
        return getPathName("page.login");
    }
}
