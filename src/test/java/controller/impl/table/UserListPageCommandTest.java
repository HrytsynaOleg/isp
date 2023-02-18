package controller.impl.table;

import testClass.TestDtoTable;
import testClass.TestSession;
import testClass.TestUser;
import dto.DtoTable;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IUserService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class UserListPageCommandTest {
    IUserService userService = mock(IUserService.class);
    DtoTablesService dtoTableService = mock(DtoTablesService.class);
    UserListPageCommand command = new UserListPageCommand(userService, dtoTableService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoTable dtoTable = TestDtoTable.getTable();
    List<User> userList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        when(dtoTableService.getDtoTable("table.users")).thenReturn(dtoTable);
        IntStream.range(1, 5).forEach(e -> {
            User user = TestUser.getCustomer();
            user.setName("User"+e);
            userList.add(user);
        });
    }

    @Test
    void process() throws DbConnectionException {
        when(userService.getUsersList(dtoTable)).thenReturn(userList);
        when(userService.getUsersCount(dtoTable)).thenReturn(4);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentUserListPage.jsp", session.getAttribute("contentPage"));
        assertEquals(userList, session.getAttribute("tableData"));
    }
    @Test
    void processIfDatabaseError() throws DbConnectionException {

        doThrow(new DbConnectionException("alert.databaseError")).when(userService).getUsersList(dtoTable);
        when(userService.getUsersCount(dtoTable)).thenReturn(4);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentUserListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
    }
    @Test
    void processIfLoggedUserIsNull() {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", null);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }

    @Test
    void processIfUserRoleIsNull() {
        session.setAttribute("role", null);
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }
}