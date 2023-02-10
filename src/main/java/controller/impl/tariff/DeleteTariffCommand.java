package controller.impl.tariff;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.RelatedRecordsExistException;
import service.ITariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class DeleteTariffCommand implements ICommand {
    private final ITariffsService tariffService;

    public DeleteTariffCommand(ITariffsService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        String tariffId = request.getParameter("tariffId");

        try {

            tariffService.deleteTariff(Integer.parseInt(tariffId));

        } catch (DbConnectionException | RelatedRecordsExistException e) {
            session.setAttribute("contentPage", getPathName("content.tariffsList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.tariffDeleted");
        session.setAttribute("contentPage", getPathName("content.tariffsList"));

        return "controller?command=tariffsList";
    }

}
