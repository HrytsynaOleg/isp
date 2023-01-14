package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import dto.DtoTableHead;
import dto.DtoTablePagination;
import dto.DtoTableSearch;
import entity.Tariff;
import entity.User;
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

        DtoTable dtoTable = tableService.getTable("table.user.tariffs");
        DtoTablePagination tablePagination = dtoTable.getPagination();
        DtoTableHead tableHead = dtoTable.getHead();
        DtoTableSearch tableSearch = dtoTable.getSearch();

        if (request.getParameter("searchBy") != null) {
            int searchBy = Integer.parseInt(request.getParameter("searchBy"));
            if (searchBy == 0) {
                tableSearch.setSearchColumn(0);
                tableSearch.setSearchCriteria("");
            }
        }
        try {
            Integer tariffsCount;
            tableHead.setFromRequest(request);
            List<Tariff> tariffs = new ArrayList<>();
            if (tableSearch.getSearchColumn() == 0) {
                tariffsCount = service.getTariffsCount();
                tablePagination.setFromRequest(request, tariffsCount);
                if (tariffsCount > 0) tariffs = service.getTariffsUserList(tablePagination.getStartRow() - 1,
                        tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder(), loggedUser.getId());
            } else {
                tariffsCount = service.getFindTariffsCount(tableSearch.getSearchColumn(), tableSearch.getSearchCriteria());
                tablePagination.setFromRequest(request, tariffsCount);
                if (tariffsCount > 0) tariffs = service.getFindTariffsUserList(tablePagination.getStartRow() - 1,
                        tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder(),
                        tableSearch.getSearchColumn(), tableSearch.getSearchCriteria(), loggedUser.getId());
            }

            session.setAttribute("tableData", tariffs);
            session.setAttribute("tableHead", tableHead);
            session.setAttribute("tableSearch", tableSearch);
            session.setAttribute("tablePagination", tablePagination);
            dtoTable.setHead(tableHead);
            dtoTable.setPagination(tablePagination);
            dtoTable.setSearch(tableSearch);
            tableService.addTable(dtoTable);

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
