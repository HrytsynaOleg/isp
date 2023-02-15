package controller.impl.table;

import controller.testClass.TestDtoTable;
import controller.testClass.TestSession;
import controller.testClass.TestUser;
import dto.DtoTable;
import entity.Service;
import entity.User;
import enums.PaymentType;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IServicesService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ServicesListPageCommandTest {
    IServicesService serviceService = mock(IServicesService.class);
    DtoTablesService dtoTableService = mock(DtoTablesService.class);
    ServicesListPageCommand command = new ServicesListPageCommand(serviceService, dtoTableService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoTable dtoTable = TestDtoTable.getTable();
    List<Service> serviceList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        when(dtoTableService.getDtoTable("table.services")).thenReturn(dtoTable);
        IntStream.range(1, 5).forEach(e -> {
            Service service = new Service(e,"ServiceName"+e,"ServiceDescription"+e);
            serviceList.add(service);
        });
    }

    @Test
    void process() throws DbConnectionException {
        when(serviceService.getServicesList(dtoTable)).thenReturn(serviceList);
        when(serviceService.getServicesCount(dtoTable)).thenReturn(4);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentServicesListPage.jsp", session.getAttribute("contentPage"));
        assertEquals(serviceList, session.getAttribute("tableData"));
    }
    @Test
    void processIfDatabaseError() throws DbConnectionException {

        doThrow(new DbConnectionException("alert.databaseError")).when(serviceService).getServicesList(dtoTable);
        when(serviceService.getServicesCount(dtoTable)).thenReturn(4);

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