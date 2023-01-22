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

        DtoTable dtoTable = tableService.getDtoTable("table.users");
        dtoTable.getSearch().setFromRequest(request);
        dtoTable.getHead().setFromRequest(request);

        try {
            Integer usersCount;
            List<User> users = new ArrayList<>();
            usersCount = service.getUsersCount(dtoTable);
            dtoTable.getPagination().setFromRequest(request, usersCount);
            if (usersCount > 0) users = service.getUsersList(dtoTable);


            session.setAttribute("tableData", users);
            tableService.updateSessionDtoTable(session,dtoTable);

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
