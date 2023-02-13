package controller.impl.tariff;

import controller.ICommand;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.ITariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class UnsubscribeTariffCommand implements ICommand {
    private final ITariffsService tariffService;

    public UnsubscribeTariffCommand(ITariffsService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserRole userRole = (UserRole) session.getAttribute("role");
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (userRole == null || loggedUser == null) {
            session.invalidate();
            return getPathName("page.login");
        }
        int tariffId = Integer.parseInt(request.getParameter("tariffId"));

        try {

            tariffService.unsubscribeTariff(tariffId, loggedUser.getId());

        } catch (DbConnectionException e) {
            session.setAttribute("alert", e.getMessage());
            return "controller?command=tariffsUserList";
        }
        session.setAttribute("info", "info.tariffUpdated");

        return "controller?command=tariffsUserList";
    }

}
