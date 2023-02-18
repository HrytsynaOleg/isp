package controller.impl.service;

import testClass.TestSession;
import testClass.TestUser;
import dto.DtoService;
import entity.Service;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditServicePageCommandTest {
    IServicesService servicesService = mock(IServicesService.class);
    EditServicePageCommand command = new EditServicePageCommand(servicesService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoService dtoService = new DtoService("2","TestServiceName","TestServicedDescription");
    Service service = new Service(2,"TestServiceName","TestServicedDescription");

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
    }

    @Test
    void process() throws DbConnectionException {
        when(request.getParameter("serviceId")).thenReturn("2");
        when(servicesService.getService(2)).thenReturn(service);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentEditServicePage.jsp", session.getAttribute("contentPage"));
        assertEquals(dtoService, session.getAttribute("editService"));
    }
    @Test
    void processIfDatabaseError() throws DbConnectionException {
        when(request.getParameter("serviceId")).thenReturn("2");
        doThrow(new DbConnectionException("alert.databaseError")).when(servicesService).getService(2);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentServicesListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
    }
    @Test
    void processIfLoggedUserIsNull() {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", null);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }

    @Test
    void processIfUserRoleIsNull() {
        session.setAttribute("role", null);
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }
}