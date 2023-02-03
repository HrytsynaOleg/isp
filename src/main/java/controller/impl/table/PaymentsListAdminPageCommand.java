package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import entity.Payment;
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

public class PaymentsListAdminPageCommand implements ICommand {
    private static final IPaymentService service = new PaymentService();
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole userRole = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        DtoTable dtoTable = tableService.getDtoTable("table.admin.payments");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer paymentsCount;
            List<Payment> payments = new ArrayList<>();
            paymentsCount = service.getPaymentsCountByUserId(0, PaymentType.IN);
            dtoTable.getPagination().setFromRequest(request, paymentsCount);
            if (paymentsCount > 0)
                payments = service.getPaymentsListByUserId(dtoTable, 0, PaymentType.IN);

            session.setAttribute("tableData", payments);
            tableService.updateSessionDtoTable(session,dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
            session.setAttribute("contentPage", getPathName("content.dashboard"));
            return userRole.getMainPage();
        }
        if (userRole != null) {
            session.setAttribute("contentPage", getPathName("content.paymentsAdminList"));
            return userRole.getMainPage();
        }
        session.invalidate();
        return getPathName("page.login");
    }
}
