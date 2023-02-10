package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import entity.Payment;
import enums.PaymentType;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IPaymentService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class WithdrawListAdminPageCommand implements ICommand {
    private final IPaymentService paymentService;
    private final DtoTablesService tableService;

    public WithdrawListAdminPageCommand(IPaymentService paymentService, DtoTablesService tableService) {
        this.paymentService = paymentService;
        this.tableService = tableService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        DtoTable dtoTable = tableService.getDtoTable("table.admin.withdraw");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer paymentsCount;
            List<Payment> payments = new ArrayList<>();
            paymentsCount = paymentService.getPaymentsCountByUserId(0, PaymentType.OUT);
            dtoTable.getPagination().setFromRequest(request, paymentsCount);
            if (paymentsCount > 0) payments = paymentService.getPaymentsListByUserId(dtoTable, 0, PaymentType.OUT);

            session.setAttribute("tableData", payments);
            tableService.updateSessionDtoTable(session,dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
            session.setAttribute("contentPage", getPathName("content.dashboard"));
            return user.getMainPage();
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.withdrawAdminList"));
            return user.getMainPage();
        }
        session.invalidate();
        return getPathName("page.login");
    }
}
