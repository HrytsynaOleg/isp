package controller.impl.tariff;

import controller.ICommand;
import dependecies.DependencyManager;
import dto.DtoTariff;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import service.ITariffsService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class CreateTariffCommand implements ICommand {
    private static final ITariffsService service = DependencyManager.tariffService;

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoTariff dtoTariff = new DtoTariff("",request.getParameter("name"),request.getParameter("description"),
                request.getParameter("service"),"ACTIVE",request.getParameter("price"),request.getParameter("period"));

        try {

            service.addTariff(dtoTariff);

        } catch (DbConnectionException | IncorrectFormatException e) {
            session.setAttribute("addTariff", dtoTariff);
            session.setAttribute("contentPage", getPathName("content.addTariff"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.tariffAdded");
        session.setAttribute("contentPage", getPathName("content.tariffsList"));

        return "controller?command=tariffsList";
    }

}
