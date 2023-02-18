package controller.impl.tariff;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddTariffPageCommandTest {
    IServicesService serviceService = mock(IServicesService.class);
    AddTariffPageCommand command = new AddTariffPageCommand(serviceService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    List<Service> servicesList = new ArrayList<>();
    List<DtoService> dtoServiceList = new ArrayList<>();
    User testUser = TestUser.getAdmin();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);

        IntStream.range(1, 5).forEach(e -> {
            Service item = new Service(e, "Service_" + e, "Description_" + e);
            servicesList.add(item);
            DtoService dtoItem = new DtoService(String.valueOf(item.getId()), item.getName(), item.getDescription());
            dtoServiceList.add(dtoItem);
        });
    }

    @Test
    void process() throws DbConnectionException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        when(serviceService.getAllServicesList()).thenReturn(servicesList);
        String path = command.process(request, response);
        assertEquals(dtoServiceList, session.getAttribute("servicesList"));
        assertEquals("fragments/contentAddTariffPage.jsp", session.getAttribute("contentPage"));
        assertEquals("admin.jsp", path);
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        doThrow(new DbConnectionException("alert.databaseError")).when(serviceService).getAllServicesList();
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertEquals("fragments/contentAdminDashboardPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals("admin.jsp", path);
    }

    @Test
    void processIfLoggedUserIsNull() throws DbConnectionException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", null);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }

    @Test
    void processIfUserRoleIsNull() throws DbConnectionException {
        session.setAttribute("role", null);
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }
}