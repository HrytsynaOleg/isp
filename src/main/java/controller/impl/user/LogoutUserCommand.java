package controller.impl.user;

import controller.ICommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static controller.manager.PathNameManager.*;

public class LogoutUserCommand implements ICommand {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        return getPathName("page.login");
    }
}
