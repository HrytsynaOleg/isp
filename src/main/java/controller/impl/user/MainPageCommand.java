package controller.impl.user;

import controller.ICommand;
import dto.DtoTable;
import entity.User;
import entity.UserTariff;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.ITariffsService;
import service.impl.DtoTablesService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

import static controller.manager.PathNameManager.*;

public class MainPageCommand implements ICommand {
    private static final ITariffsService service = new TariffsService();
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoTable dtoTable = tableService.getDtoTable("table.user.dashboardTariffs");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            int recordCount = service.getActiveTariffsUserCount(loggedUser.getId());
            dtoTable.getPagination().setFromRequest(request,recordCount);
            List<UserTariff> tariffs = service.getActiveTariffsUserList(loggedUser.getId(), dtoTable);
            BigDecimal monthTotal = service.calcMonthTotalExpenses(tariffs);

            session.setAttribute("tableData", tariffs);
            session.setAttribute("monthTotal", monthTotal);
            tableService.updateSessionDtoTable(session,dtoTable);


        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }

        if (user != null) {
            session.setAttribute("contentPage", user.getDashboard());
            return user.getMainPage();
        }
        session.setAttribute("contentPage", null);
        return getPathName("page.login");
    }
}
