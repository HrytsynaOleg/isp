package controller.impl.service;

import controller.ICommand;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.RelatedRecordsExistException;
import service.IServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;

public class DeleteServiceCommand implements ICommand {
    private final IServicesService serviceService;

    public DeleteServiceCommand(IServicesService serviceService) {
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

            serviceService.deleteService(Integer.parseInt(serviceId));

        } catch (DbConnectionException | RelatedRecordsExistException e) {
            session.setAttribute("contentPage", getPathName("content.servicesList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.serviceDeleted");
        session.setAttribute("contentPage", getPathName("content.servicesList"));

        return "controller?command=servicesList";
    }

}
