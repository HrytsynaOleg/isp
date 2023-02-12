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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

import static settings.properties.PathNameManager.*;

public class MainPageCommand implements ICommand {
    private final ITariffsService tariffService;
    private final IUserService userService;
    private final DtoTablesService tableService;

    public MainPageCommand(ITariffsService tariffService, IUserService userService, DtoTablesService tableService) {
        this.tariffService = tariffService;
        this.userService = userService;
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

        try {
            DtoTable dtoTable = tableService.getDtoTable("table.user.dashboardTariffs");
            dtoTable.getSearch().setFromRequest(request);
            dtoTable.getHead().setFromRequest(request);
            int recordCount = tariffService.getActiveTariffsUserCount(loggedUser.getId());
            dtoTable.getPagination().setFromRequest(request, recordCount);
            List<UserTariff> tariffs = tariffService.getActiveTariffsUserList(loggedUser.getId(), dtoTable);
            BigDecimal monthTotal = tariffService.calcMonthTotalUserExpenses(loggedUser.getId());
            BigDecimal monthProfit = tariffService.calcMonthTotalProfit();
            Integer usersTotal = userService.getTotalUsersCount();
            User user = userService.getUserByLogin(loggedUser.getEmail());
            tableService.updateSessionDtoTable(session, dtoTable);
            session.setAttribute("tableData", tariffs);
            session.setAttribute("loggedUser", user);
            session.setAttribute("usersTotal", usersTotal);
            session.setAttribute("monthProfitTotal", monthProfit);
            session.setAttribute("monthTotal", monthTotal);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", e.getMessage());
        }
        session.setAttribute("contentPage", userRole.getDashboard());
        return userRole.getMainPage();
    }
}

