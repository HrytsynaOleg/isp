package controller.impl.user;

import testClass.TestSession;
import testClass.TestUser;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfilePageCommandTest {

    ProfilePageCommand command = new ProfilePageCommand();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session= new TestSession();
    User testUser;

    @BeforeEach
    void init() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void processCustomerRole() {
        testUser = TestUser.getCustomer();
        session.setAttribute("role", testUser.getRole());
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertEquals("customer.jsp", path);
        assertEquals(getPathName("content.profile"), session.getAttribute("contentPage"));
    }

    @Test
    void processAdminRole() {

        testUser = TestUser.getAdmin();
        session.setAttribute("role", testUser.getRole());
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertEquals("admin.jsp", path);

        assertEquals(getPathName("content.profile"), session.getAttribute("contentPage"));
    }

    @Test
    void ifRoleIsNull() {
        testUser = TestUser.getCustomer();
        testUser.setRole(null);
        session.setAttribute("role", testUser.getRole());
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertEquals("login.jsp", path);

        assertNull(session.getAttribute("contentPage"));
    }
}