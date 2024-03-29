package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import entity.Tariff;
import entity.User;
import enums.FileFormat;
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

public class TariffsListUserPageCommand implements ICommand {
    private final ITariffsService tariffService;
    private final DtoTablesService tableService;

    public TariffsListUserPageCommand(ITariffsService tariffService, DtoTablesService tableService) {
        this.tariffService = tariffService;
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

        DtoTable dtoTable = tableService.getDtoTable("table.user.tariffs");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer tariffsCount;
            List<Tariff> tariffs = new ArrayList<>();
            tariffsCount= tariffService.getTariffsCount(dtoTable);
            dtoTable.getPagination().setFromRequest(request, tariffsCount);

            if (tariffsCount>0) {
                tariffs = tariffService.getTariffsUserList(loggedUser.getId(), dtoTable);
            }

            session.setAttribute("tableData", tariffs);
            session.setAttribute("formatList", FileFormat.getFileFormatList());
            tableService.updateSessionDtoTable(session,dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }
            session.setAttribute("contentPage", getPathName("content.tariffsUserList"));
            return userRole.getMainPage();
    }
}
