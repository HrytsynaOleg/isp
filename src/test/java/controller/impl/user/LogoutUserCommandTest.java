package controller.impl.user;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogoutUserCommandTest {
    LogoutUserCommand command;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    User testUser;

    @BeforeEach
    void init() {
        command= new LogoutUserCommand();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = new TestSession();
        when(request.getSession()).thenReturn(session);
        testUser = TestUser.getCustomer();
        session.setAttribute("loggedUser",testUser);

    }

    @Test
    void testLogoutUser() {
        String path = command.process(request,response);

        assertEquals("login.jsp", path);

    }
}