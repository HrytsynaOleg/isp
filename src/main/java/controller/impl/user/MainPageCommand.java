package controller.impl.user;

import controller.ICommand;
import dto.DtoTable;
import entity.User;
import entity.UserTariff;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.ITariffsService;
import service.IUserService;
import service.impl.DtoTablesService;
import service.impl.TariffsService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

import static controller.manager.PathNameManager.*;

public class MainPageCommand implements ICommand {
    private static final ITariffsService tariffService = new TariffsService();
    private static final IUserService userService = new UserService();
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole userRole = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoTable dtoTable = tableService.getDtoTable("table.user.dashboardTariffs");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            int recordCount = tariffService.getActiveTariffsUserCount(loggedUser.getId());
            dtoTable.getPagination().setFromRequest(request,recordCount);
            List<UserTariff> tariffs = tariffService.getActiveTariffsUserList(loggedUser.getId(), dtoTable);
            BigDecimal monthTotal = tariffService.calcMonthTotalUserExpenses(loggedUser.getId());
            BigDecimal monthProfit = tariffService.calcMonthTotalProfit();
            Integer usersTotal = userService.getTotalUsersCount();
            User user = userService.getUserByLogin(loggedUser.getEmail());

            session.setAttribute("tableData", tariffs);
            session.setAttribute("loggedUser", user);
            session.setAttribute("usersTotal", usersTotal);
            session.setAttribute("monthProfitTotal", monthProfit);
            session.setAttribute("monthTotal", monthTotal);
            tableService.updateSessionDtoTable(session,dtoTable);


        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }

        if (userRole != null) {
            session.setAttribute("contentPage", userRole.getDashboard());
            return userRole.getMainPage();
        }
        session.setAttribute("contentPage", null);
        return getPathName("page.login");
    }
}
