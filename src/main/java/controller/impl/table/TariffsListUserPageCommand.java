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
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;

public class TariffsListUserPageCommand implements ICommand {
    private static final ITariffsService service = new TariffsService();
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoTable dtoTable = tableService.getDtoTable("table.user.tariffs");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer tariffsCount;
            List<Tariff> tariffs = new ArrayList<>();
            tariffsCount= service.getTariffsCount(dtoTable);
            dtoTable.getPagination().setFromRequest(request, tariffsCount);

            if (tariffsCount>0) {
                tariffs = service.getTariffsUserList(loggedUser.getId(), dtoTable);
            }

            session.setAttribute("tableData", tariffs);
            session.setAttribute("formatList", FileFormat.getFileFormatList());
            tableService.updateSessionDtoTable(session,dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.tariffsUserList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
