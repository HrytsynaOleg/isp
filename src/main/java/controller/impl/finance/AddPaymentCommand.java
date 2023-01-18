package controller.impl.finance;

import controller.ICommand;
import dto.DtoTariff;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.NotEnoughBalanceException;
import service.IPaymentService;
import service.ITariffsService;
import service.impl.PaymentService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;

import static controller.manager.PathNameManager.getPathName;

public class AddPaymentCommand implements ICommand {
    private static final IPaymentService service = new PaymentService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        int userId= loggedUser.getId();
        BigDecimal value = new BigDecimal(request.getParameter("paymentValue"));

        try {

            service.addIncomingPayment(userId, value);

        } catch (DbConnectionException | NotEnoughBalanceException e) {
            session.setAttribute("contentPage", getPathName("content.userDashboard"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.paymentReceived");
        session.setAttribute("contentPage", getPathName("content.userDashboard"));

        return "controller?command=paymentsUserList";
    }

}
