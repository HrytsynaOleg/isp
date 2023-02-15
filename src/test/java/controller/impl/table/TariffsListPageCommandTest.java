package controller.impl.table;

import controller.testClass.TestDtoTable;
import controller.testClass.TestService;
import controller.testClass.TestSession;
import controller.testClass.TestUser;
import dto.DtoTable;
import entity.Service;
import entity.Tariff;
import entity.User;
import enums.BillingPeriod;
import enums.TariffStatus;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ITariffsService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class TariffsListPageCommandTest {
    ITariffsService tariffsService = mock(ITariffsService.class);
    DtoTablesService dtoTableService = mock(DtoTablesService.class);
    TariffsListPageCommand command = new TariffsListPageCommand(tariffsService, dtoTableService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoTable dtoTable = TestDtoTable.getTable();
    List<Tariff> tariffList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        when(dtoTableService.getDtoTable("table.tariffs")).thenReturn(dtoTable);
        IntStream.range(1, 5).forEach(e -> {
            Tariff tariff = new Tariff(e, TestService.getTestService(),"TariffName"+e,
                    "TariffDescription"+e,new BigDecimal(e), BillingPeriod.MONTH, TariffStatus.ACTIVE);
            tariffList.add(tariff);
        });
    }

    @Test
    void process() throws DbConnectionException {
        when(tariffsService.getTariffsList(dtoTable)).thenReturn(tariffList);
        when(tariffsService.getTariffsCount(dtoTable)).thenReturn(4);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
        assertEquals(tariffList, session.getAttribute("tableData"));
    }
    @Test
    void processIfDatabaseError() throws DbConnectionException {

        doThrow(new DbConnectionException("alert.databaseError")).when(tariffsService).getTariffsList(dtoTable);
        when(tariffsService.getTariffsCount(dtoTable)).thenReturn(4);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
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