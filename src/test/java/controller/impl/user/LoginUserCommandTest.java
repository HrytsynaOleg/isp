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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUserCommandTest {
    LoginUserCommand loginUserCommand;
    UserService userService;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    User testUser;

    @BeforeEach
    public void init() {
        userService = mock(UserService.class);
        loginUserCommand = new LoginUserCommand(userService);
        Whitebox.setInternalState(LoginUserCommand.class, "service", userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getParameter("login")).thenReturn("test@mail.com");
        when(request.getParameter("password")).thenReturn("password");
        session = new TestSession();
        when(request.getSession()).thenReturn(session);
        testUser = TestUser.getCustomer();
    }

    @Test
    void testLoginSuccess() throws Exception {

        when(userService.getUser("test@mail.com", "password")).thenReturn(testUser);

        String path = loginUserCommand.process(request, response);

        verify(userService).getUser("test@mail.com", "password");
        assertEquals("controller?command=mainPage", path);
        assertEquals(testUser, userService.getUser("test@mail.com", "password"));
        assertEquals(testUser.getRole(), session.getAttribute("role"));
        assertEquals(testUser, session.getAttribute("loggedUser"));
        assertEquals(testUser.getRole().getDashboard(), session.getAttribute("contentPage"));
        assertNull(session.getAttribute("userLogin"));
    }

    @Test
    void ifLoginFailedThrowNoSuchElementExeption() throws Exception {

        when(userService.getUser("test@mail.com", "password")).thenThrow(NoSuchElementException.class);

        String path = loginUserCommand.process(request, response);
        assertEquals("login.jsp", path);
        assertThrows(NoSuchElementException.class, () -> userService.getUser("test@mail.com", "password"));
        assertNull(session.getAttribute("role"));
        assertNull(session.getAttribute("loggedUser"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals(request.getParameter("login"), session.getAttribute("userLogin"));
        assertEquals("alert.userNotFound", session.getAttribute("alert"));
    }

    @Test
    void ifLoginFailedReturnNullUser() throws Exception {

        when(userService.getUser("test@mail.com", "password")).thenReturn(null);

        String path = loginUserCommand.process(request, response);
        assertEquals("login.jsp", path);
        assertNull(session.getAttribute("role"));
        assertNull(session.getAttribute("loggedUser"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals(request.getParameter("login"), session.getAttribute("userLogin"));
        assertEquals("alert.userNotFound", session.getAttribute("alert"));
    }

    @Test
    void ifLoginFailedThrowDbConnectionException() throws Exception {

        when(userService.getUser("test@mail.com", "password")).thenThrow(DbConnectionException.class);

        String path = loginUserCommand.process(request, response);
        assertEquals("login.jsp", path);
        assertThrows(DbConnectionException.class, () -> userService.getUser("test@mail.com", "password"));
        assertNull(session.getAttribute("role"));
        assertNull(session.getAttribute("loggedUser"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals(request.getParameter("login"), session.getAttribute("userLogin"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
    }


}