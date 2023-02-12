package controller.impl.user;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutUserCommandTest {

    LogoutUserCommand command = new LogoutUserCommand();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser;

    @BeforeEach
    void init() {
        when(request.getSession()).thenReturn(session);
        testUser = TestUser.getCustomer();
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("role", UserRole.CUSTOMER);
    }

    @Test
    void testLogoutUser() {
        String path = command.process(request, response);
        assertEquals("login.jsp", path);
        assertNull(session.getAttribute("loggedUser"));
        assertNull(session.getAttribute("role"));
    }
}