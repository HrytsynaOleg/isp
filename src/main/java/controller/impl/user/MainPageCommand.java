package controller.impl.user;

import controller.ICommand;
import dependecies.DependencyManager;
import dto.DtoTable;
import entity.User;
import entity.UserTariff;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.ITariffsService;
import service.IUserService;
import service.impl.DtoTablesService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

import static settings.properties.PathNameManager.*;

public class MainPageCommand implements ICommand {
    private static final ITariffsService tariffService = DependencyManager.tariffService;
    private static final IUserService userService = DependencyManager.userService;
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        UserRole userRole = (UserRole) session.getAttribute("role");
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
            session.setAttribute("alert", e.getMessage());
        }

        if (userRole != null) {
            session.setAttribute("contentPage", userRole.getDashboard());
            return userRole.getMainPage();
        }
        session.invalidate();
        return getPathName("page.login");
    }
}
