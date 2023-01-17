package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import dto.DtoTableHead;
import dto.DtoTablePagination;
import dto.DtoTableSearch;
import entity.Payment;
import entity.Tariff;
import entity.User;
import enums.FileFormat;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IPaymentService;
import service.ITariffsService;
import service.impl.DtoTablesService;
import service.impl.PaymentService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;

public class PaymentsListUserPageCommand implements ICommand {
    private static final IPaymentService service = new PaymentService();
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        User loggedUser = (User) session.getAttribute("loggedUser");

        DtoTable dtoTable = tableService.getTable("table.user.payments");
        DtoTablePagination tablePagination = dtoTable.getPagination();
        DtoTableHead tableHead = dtoTable.getHead();
        DtoTableSearch tableSearch = dtoTable.getSearch();

//        if (request.getParameter("searchBy") != null) {
//            int searchBy = Integer.parseInt(request.getParameter("searchBy"));
//            tableSearch.setFromRequest(request);
//            if (searchBy == 0) {
//                tableSearch.setSearchCriteria("");
//            }
//
//        }
        try {
            Integer paymentsCount;
            tableHead.setFromRequest(request);
            List<Payment> payments = new ArrayList<>();
            paymentsCount = service.getIncomingPaymentsCountByUserId(loggedUser.getId());
            tablePagination.setFromRequest(request, paymentsCount);
            if (paymentsCount > 0) payments = service.getIncomingPaymentsListByUserId(tablePagination.getStartRow() - 1,
                    tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder(), loggedUser.getId());


            session.setAttribute("tableData", payments);
            session.setAttribute("tableHead", tableHead);
            session.setAttribute("tableSearch", tableSearch);
            session.setAttribute("tablePagination", tablePagination);

            dtoTable.setHead(tableHead);
            dtoTable.setPagination(tablePagination);
            dtoTable.setSearch(tableSearch);
            tableService.addTable(dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
            session.setAttribute("contentPage", getPathName("content.userDashboard"));
            return user.getMainPage();
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.paymentsUserList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
