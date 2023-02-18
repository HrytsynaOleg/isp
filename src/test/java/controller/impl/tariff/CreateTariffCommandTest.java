package controller.impl.tariff;

import testClass.TestSession;
import testClass.TestTariff;
import testClass.TestUser;
import dto.DtoTariff;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ITariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CreateTariffCommandTest {
    ITariffsService tariffsService = mock(ITariffsService.class);
    CreateTariffCommand command = new CreateTariffCommand(tariffsService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    DtoTariff dtoTariff;
    User testUser = TestUser.getAdmin();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("name")).thenReturn("TestTariff");
        when(request.getParameter("description")).thenReturn("TestTariffDescription");
        when(request.getParameter("service")).thenReturn("2");
        when(request.getParameter("price")).thenReturn("100");
        when(request.getParameter("period")).thenReturn("MONTH");
        dtoTariff = new DtoTariff("", "TestTariff", "TestTariffDescription",
                "2", "ACTIVE", "100", "MONTH");
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);

        when(tariffsService.addTariff(dtoTariff)).thenReturn(TestTariff.getTestTariff());

        String path = command.process(request, response);

        assertEquals("controller?command=tariffsList", path);
        assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("info.tariffAdded", session.getAttribute("info"));
        assertNull(session.getAttribute("addTariff"));
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException, IncorrectFormatException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);

        doThrow(new DbConnectionException("alert.databaseError")).when(tariffsService).addTariff(dtoTariff);
        String path = command.process(request, response);

        assertEquals(dtoTariff,session.getAttribute("addTariff"));
        assertEquals("fragments/contentAddTariffPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals("admin.jsp", path);
    }

    @Test
    void processIfIncorrectFormat() throws DbConnectionException, IncorrectFormatException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);

        doThrow(new IncorrectFormatException("alert.emptyNameField")).when(tariffsService).addTariff(dtoTariff);
        String path = command.process(request, response);

        assertEquals(dtoTariff,session.getAttribute("addTariff"));
        assertEquals("fragments/contentAddTariffPage.jsp", session.getAttribute("contentPage"));
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