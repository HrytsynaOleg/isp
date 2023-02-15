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

public class CreateServiceCommand implements ICommand {

    private final IServicesService serviceService;

    public CreateServiceCommand(IServicesService serviceService) {
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

        DtoService dtoService = new DtoService("", request.getParameter("name"), request.getParameter("description"));

        try {
            serviceService.addService(dtoService);

        } catch (DbConnectionException | IncorrectFormatException e) {
            session.setAttribute("addService", dtoService);
            session.setAttribute("contentPage", getPathName("content.addService"));
            session.setAttribute("alert", e.getMessage());
            return userRole.getMainPage();
        }
        session.setAttribute("info", "info.serviceAdded");
        session.setAttribute("contentPage", getPathName("content.servicesList"));

        return "controller?command=servicesList";
    }

}
