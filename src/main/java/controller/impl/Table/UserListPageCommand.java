package controller.impl.Table;

import controller.ICommand;
import dto.DtoTablePagination;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IUserService;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static controller.manager.PathNameManager.getPathName;

public class UserListPageCommand implements ICommand {
    private static final IUserService service = new UserService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        DtoTablePagination tablePagination = (DtoTablePagination) session.getAttribute("tablePagination");

        if (tablePagination == null) tablePagination = new DtoTablePagination();

        try {
            Integer usersCount = service.getUsersCount();
            tablePagination.setFromRequest(request,usersCount);
            List<User> users = service.getUsersList(tablePagination.getStartRow()-1, tablePagination.getRowsPerPage());

            session.setAttribute("tableData", users);
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
