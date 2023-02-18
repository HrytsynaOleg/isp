package controller.impl.service;

import testClass.TestSession;
import testClass.TestUser;
import dto.DtoService;
import entity.Service;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditServiceCommandTest {
    IServicesService servicesService = mock(IServicesService.class);
    EditServiceCommand command = new EditServiceCommand(servicesService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoService dtoService = new DtoService("","TestServiceName","TestServicedDescription");
    DtoService newDtoService = new DtoService("","NewTestServiceName","NewTestServicedDescription");
    Service service = new Service(2,"NewTestServiceName","NewTestServicedDescription");

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("editService", dtoService);
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException {
        when(request.getParameter("name")).thenReturn("NewTestServiceName");
        when(request.getParameter("description")).thenReturn("NewTestServicedDescription");
        when(servicesService.updateService(newDtoService)).thenReturn(service);

        String path = command.process(request, response);

        assertEquals("controller?command=servicesList", path);
        assertEquals("info.serviceUpdated", session.getAttribute("info"));
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException, IncorrectFormatException {
        when(request.getParameter("name")).thenReturn("NewTestServiceName");
        when(request.getParameter("description")).thenReturn("NewTestServicedDescription");
        doThrow(new DbConnectionException("alert.databaseError")).when(servicesService).updateService(dtoService);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentEditServicePage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals(newDtoService, session.getAttribute("editService"));
    }
    @Test
    void processIfIncorrectFormat() throws DbConnectionException, IncorrectFormatException {
        when(request.getParameter("name")).thenReturn("NewTestServiceName");
        when(request.getParameter("description")).thenReturn("NewTestServicedDescription");
        doThrow(new IncorrectFormatException("alert.emptyNameField")).when(servicesService).updateService(dtoService);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentEditServicePage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.emptyNameField", session.getAttribute("alert"));
        assertEquals(newDtoService, session.getAttribute("editService"));
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