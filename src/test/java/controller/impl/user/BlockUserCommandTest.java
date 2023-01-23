package controller.impl.user;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlockUserCommandTest {
    BlockUserCommand command;
    UserService userService;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    User testUser;

    @BeforeEach
    void init() {
        userService = mock(UserService.class);
        command = new BlockUserCommand();
        Whitebox.setInternalState(BlockUserCommand.class, "service", userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
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
        verify(userService, times(1)).blockUser(25);
        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.userList"), session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));

    }
}