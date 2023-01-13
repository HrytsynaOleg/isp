package controller.impl.tariff;

import controller.ICommand;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.RelatedRecordsExistException;
import service.IServicesService;
import service.impl.ServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class DeleteTariffCommand implements ICommand {
    private static final IServicesService service = new ServicesService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        String serviceId = request.getParameter("serviceId");

        try {

            service.deleteService(Integer.parseInt(serviceId));

        } catch (DbConnectionException | IncorrectFormatException | RelatedRecordsExistException e) {
            session.setAttribute("contentPage", getPathName("content.servicesList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.serviceDeleted");
        session.setAttribute("contentPage", getPathName("content.servicesList"));

        return "controller?command=servicesList";
    }

}
