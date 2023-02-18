package controller.impl.tariff;

import testClass.TestSession;
import testClass.TestTariff;
import testClass.TestUser;
import dto.DtoService;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import entity.User;
import enums.TariffStatus;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import service.IServicesService;
import service.ITariffsService;
import service.MapperService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditTariffPageCommandTest {
    ITariffsService tariffsService = mock(ITariffsService.class);
    IServicesService servicesService = mock(IServicesService.class);
    EditTariffPageCommand command = new EditTariffPageCommand(tariffsService, servicesService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    List<Service> serviceList = new ArrayList<>();
    List<DtoService> dtoServiceList;
    Tariff tariff = TestTariff.getTestTariff();
    DtoTariff dtoTariff = MapperService.toDtoTariff(tariff);

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        when(request.getParameter("tariffId")).thenReturn("10");
        IntStream.range(1, 5).forEach(e -> {
            Service service = new Service(e, "Name" + e, "Description" + e);
            serviceList.add(service);
        });
        dtoServiceList = MapperService.toDtoServiceList(serviceList);
    }

    @Test
    void process() throws DbConnectionException {

        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class)) {
            when(servicesService.getAllServicesList()).thenReturn(serviceList);
            mapper.when(() -> MapperService.toDtoServiceList(serviceList)).thenReturn(dtoServiceList);
            mapper.when(() -> MapperService.toDtoTariff(tariff)).thenReturn(dtoTariff);
            when(tariffsService.getTariff(10)).thenReturn(tariff);

            String path = command.process(request, response);

            assertEquals("admin.jsp", path);
            assertEquals("fragments/contentEditTariffPage.jsp", session.getAttribute("contentPage"));
            assertEquals(dtoTariff, session.getAttribute("editTariff"));
            assertEquals(TariffStatus.getStatusList(), session.getAttribute("statusList"));
            assertEquals(dtoServiceList, session.getAttribute("servicesList"));
        }
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException {

        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class)) {
            doThrow(new DbConnectionException("alert.databaseError")).when(tariffsService).getTariff(10);
            when(servicesService.getAllServicesList()).thenReturn(serviceList);
            mapper.when(() -> MapperService.toDtoServiceList(serviceList)).thenReturn(dtoServiceList);
            mapper.when(() -> MapperService.toDtoTariff(tariff)).thenReturn(dtoTariff);

            String path = command.process(request, response);

            assertEquals("admin.jsp", path);
            assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
            assertEquals("alert.databaseError", session.getAttribute("alert"));
        }
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