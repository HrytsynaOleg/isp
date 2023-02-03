package controller.impl.service;

import controller.ICommand;
import dto.DtoService;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import resolver.DependencyManager;
import service.IServicesService;
import service.impl.ServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class CreateServiceCommand implements ICommand {

    private static final IServicesService service = DependencyManager.serviceService;

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoService dtoService = new DtoService("", request.getParameter("name"), request.getParameter("description"));

        try {

            service.addService(dtoService);

        } catch (DbConnectionException | IncorrectFormatException e) {
            session.setAttribute("addService", dtoService);
            session.setAttribute("contentPage", getPathName("content.addService"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.serviceAdded");
        session.setAttribute("contentPage", getPathName("content.servicesList"));

        return "controller?command=servicesList";
    }

}
