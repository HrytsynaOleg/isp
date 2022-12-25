package controller.impl.User;

import controller.ICommand;
import dto.DtoUser;
import exceptions.DbConnectionException;
import service.IUserService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class RegisterUserCommand implements ICommand {
    private static final IUserService service = new UserService();
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        String login = request.getParameter("login");
        try {
            if (service.isUserExist(login)) {
                DtoUser user = new DtoUser(login);
                session.setAttribute("user", user);
                session.setAttribute("response", "User already exist");
                return getPathName("page.register");
            }
        } catch (DbConnectionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
