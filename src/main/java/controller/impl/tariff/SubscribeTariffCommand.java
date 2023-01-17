package controller.impl.tariff;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import exceptions.TariffAlreadySubscribedException;
import service.ITariffsService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SubscribeTariffCommand implements ICommand {
    private static final ITariffsService service = new TariffsService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        int tariffId = Integer.parseInt(request.getParameter("tariffId"));

        try {

            service.subscribeTariff(tariffId, loggedUser.getId());

        } catch (DbConnectionException | TariffAlreadySubscribedException | NotEnoughBalanceException e) {
            session.setAttribute("alert", e.getMessage());
            return "controller?command=tariffsUserList";
        }
        session.setAttribute("info", "info.tariffUpdated");

        return "controller?command=tariffsUserList";
    }

}
