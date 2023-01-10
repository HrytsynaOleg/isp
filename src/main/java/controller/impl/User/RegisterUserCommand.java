package controller.impl.User;

import controller.ICommand;
import dto.DtoTableHead;
import dto.DtoTablePagination;
import dto.DtoUser;
import dto.builder.DtoUserBuilder;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.UserAlreadyExistException;
import service.IUserService;
import service.impl.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;
import static controller.manager.TableHeadManager.getColumns;

public class RegisterUserCommand implements ICommand {
    private static final IUserService service = new UserService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        DtoTablePagination tablePagination = (DtoTablePagination) session.getAttribute("tablePagination");
        DtoTableHead tableHead = (DtoTableHead) session.getAttribute("tableHead");

        if (tablePagination == null) tablePagination = new DtoTablePagination();
        if (tableHead == null) tableHead = DtoTableHead.build(getColumns("table.users"));

        String login = request.getParameter("login");

        DtoUserBuilder builder = new DtoUserBuilder();
        builder.setUserEmail(login);
        builder.setUserPassword(request.getParameter("password"));
        builder.setUserConfirmPassword(request.getParameter("confirm"));
        builder.setUserName(request.getParameter("name"));
        builder.setUserLastName(request.getParameter("lastName"));
        builder.setUserPhone(request.getParameter("phone"));
        builder.setUserAdress(request.getParameter("address"));
        builder.setUserRole(request.getParameter("role"));

        DtoUser dtoUser = builder.build();

        try {
            validateIsUserRegistered(login);
            service.addUser(dtoUser);
            Integer usersCount = service.getUsersCount();
            tablePagination.setFromRequest(request,usersCount);
            tableHead.setFromRequest(request);
            List<User> users = service.getUsersList(tablePagination.getStartRow()-1,
                    tablePagination.getRowsPerPage(), tableHead.getSortColumn(),tableHead.getSortOrder());
            session.setAttribute("tableData", users);
            session.setAttribute("tableHead", tableHead);
            session.setAttribute("tablePagination", tablePagination);

        } catch (DbConnectionException | UserAlreadyExistException | IncorrectFormatException e) {
            session.setAttribute("user", dtoUser);
            session.setAttribute("contentPage", getPathName("content.addUserPage"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("user", null);
        session.setAttribute("info", "info.userAdded");
        session.setAttribute("contentPage", getPathName("content.userList"));

        return loggedUser.getRole().getMainPage();
    }
    private void validateIsUserRegistered (String login) throws DbConnectionException, UserAlreadyExistException {
        if (service.isUserExist(login)) throw new UserAlreadyExistException("alert.userAlreadyRegistered");
    }
}
