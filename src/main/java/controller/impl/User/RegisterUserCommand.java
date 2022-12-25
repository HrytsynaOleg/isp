package controller.impl.User;

import controller.ICommand;
import dto.DtoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class RegisterUserCommand implements ICommand {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        DtoUser user = new DtoUser(request.getParameter("login"));
        session.setAttribute("user", user);
        return getPathName("page.register");
    }
}
