package controller.impl.service;

import controller.ICommand;
import dto.DtoService;
import entity.Service;
import entity.User;
import exceptions.DbConnectionException;
import dependecies.DependencyManager;
import service.IServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class EditServicePageCommand implements ICommand {
    private static final IServicesService service = DependencyManager.serviceService;

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
