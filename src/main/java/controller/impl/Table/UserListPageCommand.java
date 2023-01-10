package controller.impl.Table;

import controller.ICommand;
import dto.DtoTablePagination;
import dto.DtoTableHead;
import dto.DtoTableSearch;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IUserService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;
import static controller.manager.TableHeadManager.getColumns;

public class UserListPageCommand implements ICommand {
    private static final IUserService service = new UserService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        DtoTablePagination tablePagination = (DtoTablePagination) session.getAttribute("tablePagination");
        DtoTableHead tableHead = (DtoTableHead) session.getAttribute("tableHead");
        DtoTableSearch tableSearch = (DtoTableSearch) session.getAttribute("tableSearch");

        if (tablePagination == null) tablePagination = new DtoTablePagination();
        if (tableHead == null) tableHead = DtoTableHead.build(getColumns("table.users"));
        if (tableSearch == null) tableSearch = new DtoTableSearch();
        if (request.getParameter("searchBy") != null) {
            int searchBy = Integer.parseInt(request.getParameter("searchBy"));
            if (searchBy == 0) {
                tableSearch.setSearchColumn(0);
                tableSearch.setSearchCriteria("");
            }
        }

        try {
            Integer usersCount;

            tableHead.setFromRequest(request);

            List<User> users= new ArrayList<>();
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
        } catch (DbConnectionException e) {
            session.setAttribute("alert", "alert.databaseError");
        }
        if (user != null) {
            session.setAttribute("contentPage", getPathName("content.userList"));
            return user.getMainPage();
        }
        return getPathName("page.login");
    }
}
