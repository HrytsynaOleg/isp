package controller.impl.service;

import controller.ICommand;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class AddServicePageCommand implements ICommand {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        session.setAttribute("contentPage", getPathName("content.addService"));
        if (user!=null) return user.getMainPage();

        return getPathName("page.login");
    }
}
