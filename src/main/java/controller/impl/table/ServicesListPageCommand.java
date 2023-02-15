package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import entity.Service;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IServicesService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class ServicesListPageCommand implements ICommand {
    private final IServicesService serviceService;
    private final DtoTablesService tableService ;

    public ServicesListPageCommand(IServicesService serviceService, DtoTablesService tableService) {
        this.serviceService = serviceService;
        this.tableService = tableService;
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

        DtoTable dtoTable = tableService.getDtoTable("table.services");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer servicesCount;
            List<Service> services = new ArrayList<>();
            servicesCount = serviceService.getServicesCount(dtoTable);
            dtoTable.getPagination().setFromRequest(request, servicesCount);
            if (servicesCount > 0) services = serviceService.getServicesList(dtoTable);

            session.setAttribute("tableData", services);
            tableService.updateSessionDtoTable(session, dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }
            session.setAttribute("contentPage", getPathName("content.servicesList"));
            return userRole.getMainPage();

    }
}
