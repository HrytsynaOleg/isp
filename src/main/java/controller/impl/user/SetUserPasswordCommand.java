package controller.impl.user;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class SetUserPasswordCommand implements ICommand {
    private final IUserService userService;

    public SetUserPasswordCommand(IUserService userService) {
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
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        try {
            userService.setUserPassword(loggedUser.getId(),password, confirm);
        }
        catch (DbConnectionException | IncorrectFormatException e) {
            session.setAttribute("contentPage", getPathName("content.profile"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }

        session.setAttribute("info", "info.userPasswordChanged");
        session.setAttribute("contentPage", getPathName("content.profile"));

        return "controller?command=profilePage";

    }

}
