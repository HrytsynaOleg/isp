package controller.impl.user;

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

public class SaveProfileCommand implements ICommand {
    private static final IUserService service = new UserService();
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws DbConnectionException {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoUserBuilder builder = new DtoUserBuilder();
        builder.setUserId(String.valueOf(loggedUser.getId()));
        builder.setUserEmail(request.getParameter("userEmail"));
        builder.setUserName(request.getParameter("userName"));
        builder.setUserLastName(request.getParameter("userLastName"));
        builder.setUserPhone(request.getParameter("userPhone"));
        builder.setUserAdress(request.getParameter("userAdress"));

        DtoUser dtoUser = builder.build();

        try {
            if (!loggedUser.getEmail().equals(request.getParameter("userEmail")))
                validateIsUserRegistered(request.getParameter("userEmail"));
            loggedUser = service.updateUser(dtoUser);
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

    private void validateIsUserRegistered (String login) throws DbConnectionException, UserAlreadyExistException {
        if (service.isUserExist(login)) throw new UserAlreadyExistException("alert.userAlreadyRegistered");
    }
}
