package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import dto.DtoTablePagination;
import dto.DtoTableHead;
import dto.DtoTableSearch;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IUserService;
import service.impl.DtoTablesService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;


public class UserListPageCommand implements ICommand {
    private static final IUserService service = new UserService();
    private static final DtoTablesService tableService = DtoTablesService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();

        DtoTable dtoTable = tableService.getTable("table.users");
        DtoTablePagination tablePagination = dtoTable.getPagination();
        DtoTableHead tableHead = dtoTable.getHead();
        DtoTableSearch tableSearch = dtoTable.getSearch();

        try {
            Integer usersCount;
            List<User> users = new ArrayList<>();

            if (request.getParameter("searchBy") != null) {
                int searchBy = Integer.parseInt(request.getParameter("searchBy"));
                tableSearch.setFromRequest(request);
                if (searchBy == 0) {
                    tableSearch.setSearchCriteria("");
                }
            }

            tableHead.setFromRequest(request);

            if (tableSearch.getSearchColumn() == 0) {
                usersCount = service.getUsersCount();
                tablePagination.setFromRequest(request, usersCount);
                if (usersCount > 0) users = service.getUsersList(tablePagination.getStartRow() - 1,
                        tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder());
            } else {
                usersCount = service.getFindUsersCount(tableSearch.getSearchColumn(), tableSearch.getSearchCriteria());
                tablePagination.setFromRequest(request, usersCount);
                if (usersCount > 0) users = service.getFindUsersList(tablePagination.getStartRow() - 1,
                        tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder(),
                        tableSearch.getSearchColumn(), tableSearch.getSearchCriteria());
            }

            session.setAttribute("tableData", users);
            session.setAttribute("tableHead", tableHead);
            session.setAttribute("tableSearch", tableSearch);
            session.setAttribute("tablePagination", tablePagination);

            dtoTable.setHead(tableHead);
            dtoTable.setPagination(tablePagination);
            dtoTable.setSearch(tableSearch);
            tableService.addTable(dtoTable);

        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        } catch (IllegalArgumentException e) {
            session.setAttribute("alert", e.getMessage());
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.userList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
