package controller.impl.service;

import testClass.TestSession;
import testClass.TestUser;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.RelatedRecordsExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class DeleteServiceCommandTest {
    IServicesService servicesService = mock(IServicesService.class);
    DeleteServiceCommand command = new DeleteServiceCommand(servicesService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
    }

    @Test
    void process() throws DbConnectionException, RelatedRecordsExistException {
        when(request.getParameter("serviceId")).thenReturn("12");
        doNothing().when(servicesService).deleteService(12);

        String path = command.process(request, response);

        assertEquals("controller?command=servicesList", path);
        assertEquals("info.serviceDeleted", session.getAttribute("info"));
    }
    @Test
    void processIfDatabaseError() throws DbConnectionException, RelatedRecordsExistException {
        when(request.getParameter("serviceId")).thenReturn("12");
        doThrow(new DbConnectionException("alert.databaseError")).when(servicesService).deleteService(12);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentServicesListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
    }
    @Test
    void processIfRelatedRecordsExist() throws DbConnectionException, RelatedRecordsExistException {
        when(request.getParameter("serviceId")).thenReturn("12");
        doThrow(new RelatedRecordsExistException("alert.relatedRecordsExist")).when(servicesService).deleteService(12);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentServicesListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.relatedRecordsExist", session.getAttribute("alert"));
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