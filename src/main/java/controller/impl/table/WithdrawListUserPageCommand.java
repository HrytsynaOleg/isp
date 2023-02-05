package controller.impl.table;

import controller.ICommand;
import dependecies.DependencyManager;
import dto.DtoTable;
import entity.Payment;
import entity.User;
import enums.PaymentType;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IPaymentService;
import service.impl.DtoTablesService;
import service.impl.PaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class WithdrawListUserPageCommand implements ICommand {
    private static final IPaymentService service = DependencyManager.paymentService;
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoTable dtoTable = tableService.getDtoTable("table.user.withdraw");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer paymentsCount;
            List<Payment> payments = new ArrayList<>();
            paymentsCount = service.getPaymentsCountByUserId(loggedUser.getId(), PaymentType.OUT);
            dtoTable.getPagination().setFromRequest(request, paymentsCount);
            if (paymentsCount > 0) payments = service.getPaymentsListByUserId(dtoTable, loggedUser.getId(), PaymentType.OUT);

            session.setAttribute("tableData", payments);
            tableService.updateSessionDtoTable(session,dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
            session.setAttribute("contentPage", getPathName("content.userDashboard"));
            return user.getMainPage();
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.withdrawUserList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
