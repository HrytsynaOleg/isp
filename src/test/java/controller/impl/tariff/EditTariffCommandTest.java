package controller.impl.tariff;

import testClass.TestService;
import testClass.TestSession;
import testClass.TestUser;
import dto.DtoTariff;
import entity.Tariff;
import entity.User;
import enums.BillingPeriod;
import enums.TariffStatus;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ITariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class EditTariffCommandTest {
    ITariffsService tariffsService = mock(ITariffsService.class);
    EditTariffCommand command = new EditTariffCommand(tariffsService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoTariff oldDtoTariff;
    DtoTariff newDtoTariff;
    Tariff updatedTariff;

    @BeforeEach
    void setUp() {
        oldDtoTariff = new DtoTariff("12", "TestTariff", "TestTariffDescription",
                "2", "ACTIVE", "100", "MONTH");
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("editTariff", oldDtoTariff);

        when(request.getParameter("name")).thenReturn("NewTestTariff");
        when(request.getParameter("description")).thenReturn("NewTestTariffDescription");
        when(request.getParameter("price")).thenReturn("200");
        when(request.getParameter("status")).thenReturn("SUSPENDED");
        when(request.getParameter("period")).thenReturn("DAY");

        newDtoTariff = new DtoTariff("12", "NewTestTariff", "NewTestTariffDescription",
                "2", "SUSPENDED", "200", "DAY");
        updatedTariff = new Tariff(12, TestService.getTestService(),"NewTestTariff","NewTestTariffDescription",
                new BigDecimal(200), BillingPeriod.DAY, TariffStatus.SUSPENDED);
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException {

        when(tariffsService.updateTariff(newDtoTariff)).thenReturn(updatedTariff);

        String path = command.process(request, response);

        assertEquals("controller?command=tariffsList", path);
        assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("info.tariffUpdated", session.getAttribute("info"));
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException, IncorrectFormatException {

        doThrow(new DbConnectionException("alert.databaseError")).when(tariffsService).updateTariff(newDtoTariff);
        String path = command.process(request, response);

        assertEquals("fragments/contentEditTariffPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals("admin.jsp", path);
        assertEquals(newDtoTariff,session.getAttribute("editTariff"));
    }

    @Test
    void processIfIncorrectFormat() throws DbConnectionException, IncorrectFormatException {

        doThrow(new IncorrectFormatException("alert.emptyNameField")).when(tariffsService).updateTariff(newDtoTariff);
        String path = command.process(request, response);

        assertEquals("fragments/contentEditTariffPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.emptyNameField", session.getAttribute("alert"));
        assertEquals("admin.jsp", path);
    }

    @Test
    void processIfLoggedUserIsNull()  {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", null);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }

    @Test
    void processIfUserRoleIsNull()  {
        session.setAttribute("role", null);
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertNull(session.getAttribute("servicesList"));
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }
}