package controller.impl.User;

import controller.ICommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutUserCommand implements ICommand {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("login");
        session.removeAttribute("role");
        return "/WEB-INF/view/login.jsp";
    }
}
