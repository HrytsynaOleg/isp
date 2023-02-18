package controller.impl.user;

import testClass.TestDtoUser;
import testClass.TestSession;
import testClass.TestUser;
import dto.DtoUser;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserCommandTest {
    IUserService userService=mock(IUserService.class);
    RegisterUserCommand registerUserCommand =new RegisterUserCommand(userService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session= new TestSession();
    User testUser= TestUser.getAdmin();
    DtoUser dtoUser;

    @BeforeEach
    public void init() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("loggedUser", testUser);
        when(request.getParameter("login")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("role")).thenReturn("CUSTOMER");
        dtoUser = TestDtoUser.getDtoUser(request);
        dtoUser.setPassword("password");
        dtoUser.setRole("CUSTOMER");
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException {

        when(userService.isUserExist("user@test.com")).thenReturn(false);
        when(userService.addUser(dtoUser)).thenReturn(testUser);

        String path = registerUserCommand.process(request, response);

        assertEquals("controller?command=getUserListTable", path);
        assertEquals(getPathName("content.userList"), session.getAttribute("contentPage"));
        assertEquals("info.userAdded", session.getAttribute("info"));
        assertNull(session.getAttribute("user"));
    }

    @Test
    void processIfUserExist() throws DbConnectionException {

        when(userService.isUserExist("user@test.com")).thenReturn(true);
        String path = registerUserCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals(getPathName("content.addUserPage"), session.getAttribute("contentPage"));
        assertEquals("alert.userAlreadyRegistered", session.getAttribute("alert"));
    }

    @Test
    void processIfInputValuesNotValid() throws DbConnectionException, IncorrectFormatException {

        when(userService.isUserExist("user@test.com")).thenReturn(false);
        doThrow(new IncorrectFormatException("alert.incorrectEmail")).when(userService).addUser(dtoUser);

        String path = registerUserCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.addUserPage"), session.getAttribute("contentPage"));
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals("alert.incorrectEmail", session.getAttribute("alert"));

    }
    @Test
    void processIfDatabaseError() throws DbConnectionException, IncorrectFormatException {

        when(userService.isUserExist("user@test.com")).thenReturn(false);
        doThrow(new DbConnectionException("alert.alert.databaseError")).when(userService).addUser(dtoUser);

        String path = registerUserCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.addUserPage"), session.getAttribute("contentPage"));
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals("alert.alert.databaseError", session.getAttribute("alert"));

    }
}