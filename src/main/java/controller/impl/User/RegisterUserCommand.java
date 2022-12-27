package controller.impl.User;

import controller.ICommand;
import dto.DtoUser;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
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
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        String name = request.getParameter("name");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String role = request.getParameter("role");

        DtoUser user = new DtoUser(login,password,confirm,name,lastName,phone,address,role);
        try {
            if (service.isUserExist(login)) {
                session.setAttribute("user", user);
                session.setAttribute("response", "User already exist");
                return getPathName("page.register");
            }
        } catch (DbConnectionException e) {
            session.setAttribute("errorText", e.getMessage());
            return getPathName("page.error");
        }

        User loggedUser;

        try {
            loggedUser = service.addUser(user);
        } catch (IncorrectFormatException e) {
            session.setAttribute("user", user);
            session.setAttribute("response", e.getMessage());
            return getPathName("page.register");
        }
        catch (DbConnectionException e) {
            session.setAttribute("errorText", e.getMessage());
            return getPathName("page.error");
        }
        session.setAttribute("loggedUser", loggedUser);
        session.setAttribute("role", loggedUser.getRole());
        session.setAttribute("user", null);
        return getPathName(loggedUser.getRole().getMainPage());
    }
}
