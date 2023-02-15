package controller.impl.finance;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IPaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddPaymentCommandTest {
    IPaymentService paymentService = mock(IPaymentService.class);
    AddPaymentCommand command = new AddPaymentCommand(paymentService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getCustomer();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.CUSTOMER);
        session.setAttribute("loggedUser", testUser);
    }

    @Test
    void process() throws DbConnectionException {
        when(request.getParameter("paymentValue")).thenReturn("100");
        doNothing().when(paymentService).addIncomingPayment(25, BigDecimal.valueOf(100));

        String path = command.process(request, response);

        assertEquals("controller?command=paymentsUserList", path);
        assertEquals("fragments/contentUserDashboardPage.jsp", session.getAttribute("contentPage"));
        assertEquals("info.paymentReceived", session.getAttribute("info"));

    }
    @Test
    void processIfDatabaseError() throws DbConnectionException {
        when(request.getParameter("paymentValue")).thenReturn("100");
        doThrow(new DbConnectionException("alert.databaseError")).when(paymentService).addIncomingPayment(25, BigDecimal.valueOf(100));

        String path = command.process(request, response);

        assertEquals("customer.jsp", path);
        assertEquals("fragments/contentUserDashboardPage.jsp", session.getAttribute("contentPage"));
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