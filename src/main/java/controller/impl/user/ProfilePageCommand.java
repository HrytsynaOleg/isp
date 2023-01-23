package controller.impl.user;

import controller.ICommand;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class ProfilePageCommand implements ICommand {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        session.setAttribute("contentPage", getPathName("content.profile"));
        if (user!=null) return user.getMainPage();
        session.setAttribute("contentPage", null);
        session.invalidate();
        return getPathName("page.login");
    }
}
