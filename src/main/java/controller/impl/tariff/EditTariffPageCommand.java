package controller.impl.tariff;

import controller.ICommand;
import dto.DtoService;
import entity.Service;
import entity.User;
import exceptions.DbConnectionException;
import service.IServicesService;
import service.impl.ServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;

public class EditTariffPageCommand implements ICommand {
    private static final IServicesService service = new ServicesService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        String serviceId = request.getParameter("serviceId");

        try {
            Service serviceEdit = service.getService(Integer.parseInt(serviceId));
            DtoService dtoService = new DtoService(String.valueOf(serviceEdit.getId()), serviceEdit.getName()
                    , serviceEdit.getDescription());
            session.setAttribute("editService",dtoService);

        } catch (DbConnectionException e) {
            session.setAttribute("contentPage", getPathName("content.servicesList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }

        session.setAttribute("contentPage", getPathName("content.editService"));

        return loggedUser.getRole().getMainPage();
    }

}
