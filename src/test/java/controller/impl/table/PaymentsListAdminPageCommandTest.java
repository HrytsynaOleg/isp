package controller.impl.table;

import controller.testClass.TestDtoTable;
import controller.testClass.TestSession;
import controller.testClass.TestUser;
import dto.DtoTable;
import entity.Payment;
import entity.User;
import enums.PaymentType;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IPaymentService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class PaymentsListAdminPageCommandTest {
    IPaymentService paymentService = mock(IPaymentService.class);
    DtoTablesService dtoTableService = mock(DtoTablesService.class);
    PaymentsListAdminPageCommand command = new PaymentsListAdminPageCommand(paymentService, dtoTableService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    DtoTable dtoTable = TestDtoTable.getTable();
    List<Payment> paymentList =new ArrayList<>();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        when(dtoTableService.getDtoTable("table.admin.payments")).thenReturn(dtoTable);
        IntStream.range(1,5).forEach(e->{
            Payment payment = new Payment(e,testUser,new BigDecimal(e), new Date(), PaymentType.IN,"Description");
            paymentList.add(payment);
        });
    }

    @Test
    void process() throws DbConnectionException {
        when(paymentService.getPaymentsListAllUsers(dtoTable,PaymentType.IN)).thenReturn(paymentList);
        when(paymentService.getPaymentsCountAllUsers(PaymentType.IN)).thenReturn(4);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentPaymentsListAdminPage.jsp", session.getAttribute("contentPage"));
        assertEquals(paymentList, session.getAttribute("tableData"));

    }

    @Test
    void processIfDatabaseError() throws DbConnectionException {

        doThrow(new DbConnectionException("alert.databaseError")).when(paymentService).getPaymentsListAllUsers(dtoTable,PaymentType.IN);
        when(paymentService.getPaymentsCountAllUsers(PaymentType.IN)).thenReturn(4);

        String path = command.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentAdminDashboardPage.jsp", session.getAttribute("contentPage"));
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