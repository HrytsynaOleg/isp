package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import entity.Service;
import enums.UserRole;
import exceptions.DbConnectionException;
import dependecies.DependencyManager;
import service.IServicesService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class ServicesListPageCommand implements ICommand {
    private static final IServicesService service = DependencyManager.serviceService;
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        DtoTable dtoTable = tableService.getDtoTable("table.services");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer servicesCount;
            List<Service> services = new ArrayList<>();
            servicesCount = service.getServicesCount(dtoTable);
            dtoTable.getPagination().setFromRequest(request, servicesCount);
            if (servicesCount > 0) services = service.getServicesList(dtoTable);

            session.setAttribute("tableData", services);
            tableService.updateSessionDtoTable(session, dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.servicesList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
