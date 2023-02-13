package controller.impl.tariff;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import exceptions.TariffAlreadySubscribedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ITariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class SubscribeTariffCommandTest {
    ITariffsService tariffsService = mock(ITariffsService.class);
    SubscribeTariffCommand command = new SubscribeTariffCommand(tariffsService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getCustomer();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.CUSTOMER);
        session.setAttribute("loggedUser", testUser);
        when(request.getParameter("tariffId")).thenReturn("10");
    }

    @Test
    void process() throws TariffAlreadySubscribedException, DbConnectionException, NotEnoughBalanceException {

        doNothing().when(tariffsService).subscribeTariff(10,25);

        String path = command.process(request, response);

        assertEquals("controller?command=tariffsUserList", path);
        assertEquals("info.tariffUpdated", session.getAttribute("info"));
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException, TariffAlreadySubscribedException, NotEnoughBalanceException {

        doThrow(new DbConnectionException("alert.databaseError")).when(tariffsService).subscribeTariff(10,25);

        String path = command.process(request, response);

        assertEquals("controller?command=tariffsUserList", path);
        assertEquals("alert.databaseError", session.getAttribute("alert"));
    }

    @Test
    void processIfTariffAlreadySubscribed() throws DbConnectionException, TariffAlreadySubscribedException, NotEnoughBalanceException {

        doThrow(new TariffAlreadySubscribedException("alert.tariffAlreadySubscribed")).when(tariffsService).subscribeTariff(10,25);

        String path = command.process(request, response);

        assertEquals("controller?command=tariffsUserList", path);
        assertEquals("alert.tariffAlreadySubscribed", session.getAttribute("alert"));
    }

    @Test
    void processIfNoEnoughBalance() throws DbConnectionException, TariffAlreadySubscribedException, NotEnoughBalanceException {

        doThrow(new NotEnoughBalanceException("alert.notEnoughBalance")).when(tariffsService).subscribeTariff(10,25);

        String path = command.process(request, response);

        assertEquals("controller?command=tariffsUserList", path);
        assertEquals("alert.notEnoughBalance", session.getAttribute("alert"));
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