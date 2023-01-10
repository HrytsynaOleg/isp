package controller.impl.Table;

import controller.ICommand;
import dto.DtoTableHead;
import dto.DtoTablePagination;
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

public class UserFindPageCommand implements ICommand {
    private static final IUserService service = new UserService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        DtoTablePagination tablePagination = (DtoTablePagination) session.getAttribute("tablePagination");
        DtoTableHead tableHead = (DtoTableHead) session.getAttribute("tableHead");
        DtoTableSearch tableSearch = (DtoTableSearch) session.getAttribute("tableSearch");

        int searchBy = Integer.parseInt(request.getParameter("searchBy"));
        String searchString = request.getParameter("searchString");

        if (tablePagination == null) tablePagination = new DtoTablePagination();
        if (tableHead == null) tableHead = DtoTableHead.build(getColumns("table.users"));
        if (tableSearch == null) tableSearch = new DtoTableSearch();

        try {
            if (searchBy == 0) throw new IllegalArgumentException("alert.selectFindField");
            tableHead.setFromRequest(request);
            tableSearch.setFromRequest(request);
            Integer usersCount = service.getFindUsersCount(searchBy, searchString);
            tablePagination.setFromRequest(request, usersCount);
            List<User> users = new ArrayList<>();
            if (usersCount > 0) users = service.getFindUsersList(tablePagination.getStartRow() - 1,
                    tablePagination.getRowsPerPage(), tableHead.getSortColumn(), tableHead.getSortOrder(), searchBy, searchString);

            session.setAttribute("tableData", users);
            session.setAttribute("tableHead", tableHead);
            session.setAttribute("tablePagination", tablePagination);
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
