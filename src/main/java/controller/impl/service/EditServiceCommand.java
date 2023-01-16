package controller.impl.service;

import controller.ICommand;
import dto.DtoService;
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

public class EditServiceCommand implements ICommand {
    private static final IServicesService service = new ServicesService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        DtoService dtoService = (DtoService) session.getAttribute("editService");
        dtoService.setName(request.getParameter("name"));
        dtoService.setDescription(request.getParameter("description"));

        try {

            service.updateService(dtoService);

        } catch (DbConnectionException | IncorrectFormatException  e) {
            session.setAttribute("contentPage", getPathName("content.editService"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("info", "info.serviceUpdated");
        session.setAttribute("contentPage", getPathName("content.servicesList"));

        return "controller?command=servicesList";
    }

}