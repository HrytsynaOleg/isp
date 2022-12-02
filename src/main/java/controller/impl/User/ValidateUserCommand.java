package controller.impl.User;

import controller.ICommand;
import entity.User;
import exeptions.DbConnectionExeption;
import service.IUserService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidateUserCommand implements ICommand {
    private static final IUserService service = new UserService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("login");
        String responseText = "";
        try {
            User user = service.validateUser(userName);
            if (user != null) {
                responseText = " You login as: " + userName;
                request.setAttribute("response", responseText);
                return "hello.jsp";
            }
            responseText = " User not exist!";
        } catch (DbConnectionExeption ex) {
            responseText = "Database error: " + ex.getMessage();
        }
        request.setAttribute("response", responseText);
        return "badlogin.jsp";
    }
}
