package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import entity.Tariff;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.ITariffsService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class TariffsListPageCommand implements ICommand {
    private final ITariffsService tariffService;
    private final DtoTablesService tableService;

    public TariffsListPageCommand(ITariffsService tariffService, DtoTablesService tableService) {
        this.tariffService = tariffService;
        this.tableService = tableService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        DtoTable dtoTable = tableService.getDtoTable("table.tariffs");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer tariffsCount;
            List<Tariff> tariffList = new ArrayList<>();
            tariffsCount = tariffService.getTariffsCount(dtoTable);
            dtoTable.getPagination().setFromRequest(request, tariffsCount);
            if (tariffsCount > 0) tariffList = tariffService.getTariffsList(dtoTable);

            session.setAttribute("tableData", tariffList);
            tableService.updateSessionDtoTable(session,dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.tariffsList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
