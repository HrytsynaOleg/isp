package controller.impl.user;

import testClass.TestSession;
import testClass.TestUser;
import entity.User;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UnblockUserCommandTest {
    IUserService userService = mock(IUserService.class);
    UnblockUserCommand command = new UnblockUserCommand(userService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session= new TestSession();
    User testUser;

    @BeforeEach
    void setUp() {

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("user")).thenReturn("25");
        testUser= TestUser.getAdmin();
        session.setAttribute("loggedUser",testUser);
    }

    @Test
    void process() throws DbConnectionException {
        String path = command.process(request, response);
        verify(userService, times(1)).unblockUser(25);
        assertEquals("controller?command=getUserListTable", path);
        assertEquals(getPathName("content.userList"), session.getAttribute("contentPage"));
        assertEquals("info.userStatusChanged", session.getAttribute("info"));
    }

    @Test
    void ifUserServiceThrowsException() throws DbConnectionException {

        doThrow(new DbConnectionException("alert.databaseError")).when(userService).unblockUser(25);
        String path = command.process(request, response);
        verify(userService, times(1)).unblockUser(25);
        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.userList"), session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));

    }
}