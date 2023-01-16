package controller.impl.tariff;

import controller.ICommand;
import dao.IUserTariffDao;
import dao.impl.UserTariffDaoImpl;
import dto.DtoTariff;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.TariffAlreadySubscribedException;
import service.ITariffsService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class SubscribeTariffCommand implements ICommand {
    private static final ITariffsService service = new TariffsService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        int tariffId = Integer.parseInt(request.getParameter("tariffId"));

        try {

            service.subscribeTariff(tariffId, loggedUser.getId());

        } catch (DbConnectionException | TariffAlreadySubscribedException e) {
            session.setAttribute("contentPage", getPathName("content.tariffsUserList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.tariffUpdated");
        session.setAttribute("contentPage", getPathName("content.tariffsUserList"));

        return "controller?command=tariffsUserList";
    }

}