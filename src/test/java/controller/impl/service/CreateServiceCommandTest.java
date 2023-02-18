package controller.impl.service;

import testClass.TestService;
import testClass.TestSession;
import testClass.TestUser;
import dto.DtoService;
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

class CreateServiceCommandTest {
    IServicesService servicesService = mock(IServicesService.class);
    CreateServiceCommand command = new CreateServiceCommand(servicesService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoService dtoService = new DtoService("","TestServiceName","TestServicedDescription");


    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException {
        when(request.getParameter("name")).thenReturn("TestServiceName");
        when(request.getParameter("description")).thenReturn("TestServicedDescription");
        when(servicesService.addService(dtoService)).thenReturn(TestService.getTestService());

        String path = command.process(request, response);

        assertEquals("controller?command=servicesList", path);
        assertEquals("info.serviceAdded", session.getAttribute("info"));
    }
    @Test
    void processIfDatabaseError() throws DbConnectionException, IncorrectFormatException {
        when(request.getParameter("name")).thenReturn("TestServiceName");
        when(request.getParameter("description")).thenReturn("TestServicedDescription");
        doThrow(new DbConnectionException("alert.databaseError")).when(servicesService).addService(dtoService);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentAddServicePage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals(dtoService, session.getAttribute("addService"));
    }

    @Test
    void processIfIncorrectFormat() throws DbConnectionException, IncorrectFormatException {
        when(request.getParameter("name")).thenReturn("TestServiceName");
        when(request.getParameter("description")).thenReturn("TestServicedDescription");
        doThrow(new IncorrectFormatException("alert.emptyNameField")).when(servicesService).addService(dtoService);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentAddServicePage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.emptyNameField", session.getAttribute("alert"));
        assertEquals(dtoService, session.getAttribute("addService"));
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