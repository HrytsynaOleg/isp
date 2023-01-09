package controller.impl.User;

import controller.ICommand;
import dto.DtoUser;
import dto.builder.DtoUserBuilder;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.UserAlreadyExistException;
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
        User loggedUser = (User) session.getAttribute("loggedUser");

        String login = request.getParameter("login");

        DtoUserBuilder builder = new DtoUserBuilder();
        builder.setUserEmail(login);
        builder.setUserPassword(request.getParameter("password"));
        builder.setUserConfirmPassword(request.getParameter("confirm"));
        builder.setUserName(request.getParameter("name"));
        builder.setUserLastName(request.getParameter("lastName"));
        builder.setUserPhone(request.getParameter("phone"));
        builder.setUserAdress(request.getParameter("address"));
        builder.setUserRole(request.getParameter("role"));

        DtoUser user = builder.build();

        try {
            validateIsUserRegistered(login);
            service.addUser(user);

        } catch (DbConnectionException | UserAlreadyExistException | IncorrectFormatException e) {
            session.setAttribute("user", user);
            session.setAttribute("contentPage", getPathName("content.addUserPage"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("user", null);
        session.setAttribute("info", "info.profileUpdate");
        session.setAttribute("contentPage", getPathName("content.userList"));
        return loggedUser.getRole().getMainPage();
    }
    private void validateIsUserRegistered (String login) throws DbConnectionException, UserAlreadyExistException {
        if (service.isUserExist(login)) throw new UserAlreadyExistException("alert.userAlreadyRegistered");
    }
}
