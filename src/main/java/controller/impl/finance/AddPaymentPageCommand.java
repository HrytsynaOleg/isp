package controller.impl.finance;

import controller.ICommand;
import entity.User;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class AddPaymentPageCommand implements ICommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserRole userRole = (UserRole) session.getAttribute("role");
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (userRole == null || loggedUser == null) {
            session.invalidate();
            return getPathName("page.login");
        }

        session.setAttribute("contentPage", getPathName("content.addPayment"));

        return loggedUser.getRole().getMainPage();
    }

}
