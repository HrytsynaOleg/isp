package controller.impl.table;

import controller.ICommand;
import dto.DtoTable;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IUserService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static settings.properties.PathNameManager.getPathName;


public class UserListPageCommand implements ICommand {
    private final IUserService userService;
    private final DtoTablesService tableService;

    public UserListPageCommand(IUserService userService, DtoTablesService tableService) {
        this.userService = userService;
        this.tableService = tableService;
    }

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
            usersCount = userService.getUsersCount(dtoTable);
            dtoTable.getPagination().setFromRequest(request, usersCount);
            if (usersCount > 0) users = userService.getUsersList(dtoTable);


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
