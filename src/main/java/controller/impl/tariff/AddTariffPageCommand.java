package controller.impl.tariff;

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

import java.util.ArrayList;
import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class AddTariffPageCommand implements ICommand {
    private final IServicesService serviceService;

    public AddTariffPageCommand(IServicesService serviceService) {
        this.serviceService = serviceService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws DbConnectionException {
        HttpSession session = request.getSession();
        UserRole userRole = (UserRole) session.getAttribute("role");
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (userRole == null || loggedUser == null) {
            session.invalidate();
            return getPathName("page.login");
        }

        try {
            List<DtoService> dtoServiceList = new ArrayList<>();
            List<Service> serviceList = serviceService.getAllServicesList();
            for (Service item : serviceList) {
                DtoService dtoService = new DtoService(String.valueOf(item.getId()), item.getName(), item.getDescription());
                dtoServiceList.add(dtoService);
            }
            session.setAttribute("servicesList", dtoServiceList);
            session.setAttribute("contentPage", getPathName("content.addTariff"));
        } catch (DbConnectionException e) {
            session.setAttribute("contentPage", userRole.getDashboard());
            session.setAttribute("alert", e.getMessage());
        }
            return userRole.getMainPage();
    }
}
