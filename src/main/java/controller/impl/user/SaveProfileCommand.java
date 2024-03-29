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

public class SaveProfileCommand implements ICommand {
    private final IUserService userService;

    public SaveProfileCommand(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws DbConnectionException {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            session.invalidate();
            return getPathName("page.login");
        }
        String login = request.getParameter("login");

        DtoUserBuilder builder = new DtoUserBuilder();
        builder.setUserId(String.valueOf(loggedUser.getId()));
        builder.setUserEmail(login);
        builder.setUserName(request.getParameter("name"));
        builder.setUserLastName(request.getParameter("lastName"));
        builder.setUserPhone(request.getParameter("phone"));
        builder.setUserAdress(request.getParameter("address"));

        DtoUser dtoUser = builder.build();

        try {
            if (!loggedUser.getEmail().equals(login) && userService.isUserExist(login))
                throw new UserAlreadyExistException("alert.userAlreadyRegistered");
            loggedUser = userService.updateUser(dtoUser);
        } catch (IncorrectFormatException | DbConnectionException | UserAlreadyExistException e) {
            session.setAttribute("user", dtoUser);
            session.setAttribute("alert", e.getMessage());
            session.setAttribute("contentPage", getPathName("content.editProfile"));
            session.setAttribute("loggedUser",loggedUser);
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("loggedUser", loggedUser);
        session.setAttribute("role", loggedUser.getRole());
        session.setAttribute("user", null);
        session.setAttribute("info", "info.profileUpdate");
        session.setAttribute("contentPage", getPathName("content.profile"));
        return loggedUser.getRole().getMainPage();
    }

}
