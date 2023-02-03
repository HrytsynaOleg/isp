package controller.impl.user;

import controller.testClass.TestDtoUser;
import controller.testClass.TestSession;
import controller.testClass.TestUser;
import dto.DtoUser;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.UserAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserCommandTest {
    RegisterUserCommand registerUserCommand;
    UserService userService;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    User testUser;
    DtoUser dtoUser;

    @BeforeEach
    public void init() {
        userService = mock(UserService.class);
        registerUserCommand = spy(new RegisterUserCommand());
        Whitebox.setInternalState(RegisterUserCommand.class, "service", userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = new TestSession();
        when(request.getSession()).thenReturn(session);
        testUser = TestUser.getAdmin();
        session.setAttribute("loggedUser", testUser);
        when(request.getParameter("login")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("role")).thenReturn("CUSTOMER");
        dtoUser = TestDtoUser.getDtoUser(request);
        dtoUser.setPassword("password");
        dtoUser.setRole("CUSTOMER");
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException, UserAlreadyExistException {

        doNothing().when(registerUserCommand).validateIsUserRegistered("user@test.com");
        when(userService.addUser(dtoUser)).thenReturn(testUser);

        String path = registerUserCommand.process(request, response);

        assertEquals("controller?command=getUserListTable", path);
        assertEquals(getPathName("content.userList"), session.getAttribute("contentPage"));
        assertEquals("info.userAdded", session.getAttribute("info"));
    }

    @Test
    void processIfUserExist() throws DbConnectionException, IncorrectFormatException, UserAlreadyExistException {

        doThrow(new UserAlreadyExistException("alert.userAlreadyRegistered")).when(registerUserCommand).validateIsUserRegistered("user@test.com");
        when(userService.addUser(TestDtoUser.getDtoUser(request))).thenReturn(testUser);

        String path = registerUserCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals(getPathName("content.addUserPage"), session.getAttribute("contentPage"));
        assertEquals("alert.userAlreadyRegistered", session.getAttribute("alert"));
    }

    @Test
    void processIfInputValuesNotValid() throws DbConnectionException, IncorrectFormatException, UserAlreadyExistException {

        doNothing().when(registerUserCommand).validateIsUserRegistered("user@test.com");
        doThrow(new IncorrectFormatException("alert.incorrectEmail")).when(userService).addUser(dtoUser);

        String path = registerUserCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.addUserPage"), session.getAttribute("contentPage"));
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals("alert.incorrectEmail", session.getAttribute("alert"));

    }
}