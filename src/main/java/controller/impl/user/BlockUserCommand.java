package controller.impl.user;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import service.IUserService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class BlockUserCommand implements ICommand {
    private static final IUserService service = new UserService();
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws DbConnectionException {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        int userId = Integer.parseInt(request.getParameter("user"));
        try {
            service.blockUser(userId);
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
