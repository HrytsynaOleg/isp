package controller.impl.User;

import controller.ICommand;
import entity.User;
import enums.UserRole;
import exeptions.DbConnectionExeption;
import service.IUserService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

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
                HttpSession session = request.getSession();
                session.setAttribute("login", user.getEmail());
                session.setAttribute("role", user.getRole());
                if (user.getRole().equals(UserRole.USER))
                    return "/WEB-INF/view/user_page.jsp";
                else
                    return "/WEB-INF/view/admin_page.jsp";
            }
        } catch (DbConnectionExeption ex) {
            responseText = "Database error: " + ex.getMessage();
        } catch (NoSuchElementException ex) {
            responseText = "User not found";
        }
        request.setAttribute("response", responseText);
        return "/WEB-INF/view/login.jsp";
    }
}
