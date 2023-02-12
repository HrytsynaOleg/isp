package controller.impl.user;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockUserCommandTest {

    IUserService userService =mock(IUserService.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    BlockUserCommand command =new BlockUserCommand(userService);

    HttpSession session;
    User testUser;

    @BeforeAll
    void init() {
        session = new TestSession();
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("user")).thenReturn("25");
        testUser= TestUser.getAdmin();
        session.setAttribute("loggedUser",testUser);
    }

    @Test
    void process() throws DbConnectionException {

        String path = command.process(request, response);
        verify(userService).blockUser(25);
        assertEquals("controller?command=getUserListTable", path);
        assertEquals(getPathName("content.userList"), session.getAttribute("contentPage"));
        assertEquals("info.userStatusChanged", session.getAttribute("info"));

    }

    @Test
    void ifUserServiceThrowsException() throws DbConnectionException {

        doThrow(new DbConnectionException("alert.databaseError")).when(userService).blockUser(25);
        String path = command.process(request, response);
        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.userList"), session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));

    }
}