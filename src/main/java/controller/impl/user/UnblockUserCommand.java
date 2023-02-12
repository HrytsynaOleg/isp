package controller.impl.user;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class UnblockUserCommand implements ICommand {
    private final IUserService userService;

    public UnblockUserCommand(IUserService userService) {
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
        int userId = Integer.parseInt(request.getParameter("user"));

        try {
            userService.unblockUser(userId);
        }
        catch (DbConnectionException e) {
            session.setAttribute("contentPage", getPathName("content.userList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }

        session.setAttribute("info", "info.userStatusChanged");
        session.setAttribute("contentPage", getPathName("content.userList"));

        return "controller?command=getUserListTable";

    }

}
