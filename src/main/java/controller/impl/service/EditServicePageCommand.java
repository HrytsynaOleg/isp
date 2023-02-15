package controller.impl.service;

import controller.ICommand;
import dto.DtoService;
import entity.Service;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class EditServicePageCommand implements ICommand {
    private final IServicesService serviceService;

    public EditServicePageCommand(IServicesService serviceService) {
        this.serviceService = serviceService;
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
        String serviceId = request.getParameter("serviceId");

        try {
            Service serviceEdit = serviceService.getService(Integer.parseInt(serviceId));
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
