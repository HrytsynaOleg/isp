package controller.impl.finance;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import service.IPaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;

import static settings.properties.PathNameManager.getPathName;

public class AddPaymentCommand implements ICommand {
    private final IPaymentService paymentService;

    public AddPaymentCommand(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        int userId= loggedUser.getId();
        BigDecimal value = new BigDecimal(request.getParameter("paymentValue"));

        try {

            paymentService.addIncomingPayment(userId, value);

        } catch (DbConnectionException  e) {
            session.setAttribute("contentPage", getPathName("content.userDashboard"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.paymentReceived");
        session.setAttribute("contentPage", getPathName("content.userDashboard"));

        return "controller?command=paymentsUserList";
    }

}
