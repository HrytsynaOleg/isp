package controller.impl.user;

import controller.ICommand;
import dto.DtoUser;
import dto.builder.DtoUserBuilder;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.UserAlreadyExistException;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class RegisterUserCommand implements ICommand {
    private final IUserService userService;

    public RegisterUserCommand(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            session.invalidate();
            return getPathName("page.login");
        }

        String login = request.getParameter("login");

        DtoUserBuilder builder = new DtoUserBuilder();
        builder.setUserEmail(login);
        builder.setUserPassword(request.getParameter("password"));
        builder.setUserName(request.getParameter("name"));
        builder.setUserLastName(request.getParameter("lastName"));
        builder.setUserPhone(request.getParameter("phone"));
        builder.setUserAdress(request.getParameter("address"));
        builder.setUserRole(request.getParameter("role"));

        DtoUser dtoUser = builder.build();

        try {
            if (userService.isUserExist(login)) throw new UserAlreadyExistException("alert.userAlreadyRegistered");
            userService.addUser(dtoUser);

        } catch (DbConnectionException | IncorrectFormatException | UserAlreadyExistException e) {
            session.setAttribute("user", dtoUser);
            session.setAttribute("contentPage", getPathName("content.addUserPage"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.removeAttribute("user");
        session.setAttribute("info", "info.userAdded");
        session.setAttribute("contentPage", getPathName("content.userList"));

        return "controller?command=getUserListTable";
    }
}
