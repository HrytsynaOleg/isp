package controller.impl.user;

import controller.ICommand;
import enums.UserRole;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class AddUserPageCommand implements ICommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        session.setAttribute("contentPage", getPathName("content.addUserPage"));
        if (user != null) return user.getMainPage();

        return getPathName("page.login");
    }
}
