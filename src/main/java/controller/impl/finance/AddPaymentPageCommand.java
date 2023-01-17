package controller.impl.finance;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import service.IPaymentService;
import service.impl.PaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static controller.manager.PathNameManager.getPathName;

public class AddPaymentPageCommand implements ICommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        session.setAttribute("contentPage", getPathName("content.addPayment"));

        return loggedUser.getRole().getMainPage();
    }

}
