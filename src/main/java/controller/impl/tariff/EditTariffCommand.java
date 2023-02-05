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

public class EditTariffCommand implements ICommand {
    private static final ITariffsService service = DependencyManager.tariffService;

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoTariff dtoTariff = (DtoTariff) session.getAttribute("editTariff");
        dtoTariff.setName(request.getParameter("name"));
        dtoTariff.setDescription(request.getParameter("description"));
        dtoTariff.setPrice(request.getParameter("price"));
        dtoTariff.setStatus(request.getParameter("status"));
        dtoTariff.setPeriod(request.getParameter("period"));

        try {
            service.updateTariff(dtoTariff);

        } catch (DbConnectionException | IncorrectFormatException  e) {
            session.setAttribute("contentPage", getPathName("content.editTariff"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.tariffUpdated");
        session.setAttribute("contentPage", getPathName("content.tariffsList"));

        return "controller?command=tariffsList";
    }

}
