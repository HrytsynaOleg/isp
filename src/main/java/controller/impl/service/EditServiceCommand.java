package controller.impl.service;

import controller.ICommand;
import dto.DtoService;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import service.IServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class EditServiceCommand implements ICommand {
    private final IServicesService serviceService;

    public EditServiceCommand(IServicesService serviceService) {
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
        DtoService dtoService = (DtoService) session.getAttribute("editService");
        dtoService.setName(request.getParameter("name"));
        dtoService.setDescription(request.getParameter("description"));

        try {

            serviceService.updateService(dtoService);

        } catch (DbConnectionException | IncorrectFormatException e) {
            session.setAttribute("contentPage", getPathName("content.editService"));
            session.setAttribute("alert", e.getMessage());
            session.setAttribute("editService", dtoService);
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.serviceUpdated");
        session.setAttribute("contentPage", getPathName("content.servicesList"));

        return "controller?command=servicesList";
    }

}
