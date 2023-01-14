package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import dto.DtoTableHead;
import dto.DtoTablePagination;
import dto.DtoTableSearch;
import entity.Tariff;
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

public class TariffsListPageCommand implements ICommand {
    private static final ITariffsService service = new TariffsService();
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        DtoTable dtoTable = tableService.getTable("table.tariffs");
        DtoTablePagination tablePagination = dtoTable.getPagination();
        DtoTableHead tableHead = dtoTable.getHead();
        DtoTableSearch tableSearch = dtoTable.getSearch();

        if (request.getParameter("searchBy") != null) {
            int searchBy = Integer.parseInt(request.getParameter("searchBy"));
            tableSearch.setFromRequest(request);
            if (searchBy == 0) {
                tableSearch.setSearchCriteria("");
            }
        }
        try {
            Integer tariffsCount;
            tableHead.setFromRequest(request);
            List<Tariff> services = new ArrayList<>();
            if (tableSearch.getSearchColumn() == 0) {
                tariffsCount = service.getTariffsCount();
                tablePagination.setFromRequest(request, tariffsCount);
                if (tariffsCount > 0) services = service.getTariffsList(tablePagination.getStartRow() - 1,
                        tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder());
            } else {
                tariffsCount = service.getFindTariffsCount(tableSearch.getSearchColumn(), tableSearch.getSearchCriteria());
                tablePagination.setFromRequest(request, tariffsCount);
                if (tariffsCount > 0) services = service.getFindTariffsList(tablePagination.getStartRow() - 1,
                        tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder(),
                        tableSearch.getSearchColumn(), tableSearch.getSearchCriteria());
            }

            session.setAttribute("tableData", services);
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
            session.setAttribute("contentPage", getPathName("content.tariffsList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
