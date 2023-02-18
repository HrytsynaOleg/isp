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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUserCommandTest {

    IUserService userService= mock(IUserService.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    LoginUserCommand loginUserCommand = new LoginUserCommand(userService);
    HttpSession session= new TestSession();
    User testUser= TestUser.getCustomer();

    @BeforeEach
    public void init() {
        when(request.getParameter("login")).thenReturn("test@mail.com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);
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
    void ifLoginFailedThrowNoSuchElementException() throws Exception {

        when(userService.getUser("test@mail.com", "password")).thenThrow(new NoSuchElementException("alert.userNotFound"));

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
    void ifLoginFailedThrowDbConnectionException() throws Exception {

        when(userService.getUser("test@mail.com", "password")).thenThrow(new DbConnectionException("alert.databaseError"));

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