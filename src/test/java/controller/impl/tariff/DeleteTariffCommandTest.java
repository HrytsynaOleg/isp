package controller.impl.tariff;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.RelatedRecordsExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ITariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class DeleteTariffCommandTest {
    ITariffsService tariffsService = mock(ITariffsService.class);
    DeleteTariffCommand command = new DeleteTariffCommand(tariffsService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("tariffId")).thenReturn("2");
    }

    @Test
    void process() throws DbConnectionException, RelatedRecordsExistException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);
        doNothing().when(tariffsService).deleteTariff(2);

        String path = command.process(request, response);

        assertEquals("controller?command=tariffsList", path);
        assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("info.tariffDeleted", session.getAttribute("info"));
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException, RelatedRecordsExistException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);

        doThrow(new DbConnectionException("alert.databaseError")).when(tariffsService).deleteTariff(2);

        String path = command.process(request, response);

        assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals("admin.jsp", path);
    }

    @Test
    void processIfRelatedRecordsExist() throws DbConnectionException, RelatedRecordsExistException {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", testUser);

        doThrow(new RelatedRecordsExistException("alert.tariffSubscribersExist")).when(tariffsService).deleteTariff(2);

        String path = command.process(request, response);

        assertEquals("fragments/contentTariffsListPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.tariffSubscribersExist", session.getAttribute("alert"));
        assertEquals("admin.jsp", path);
    }
    @Test
    void processIfLoggedUserIsNull()  {
        session.setAttribute("role", UserRole.ADMIN);
        session.setAttribute("loggedUser", null);
        String path = command.process(request, response);
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }

    @Test
    void processIfUserRoleIsNull()  {
        session.setAttribute("role", null);
        session.setAttribute("loggedUser", testUser);
        String path = command.process(request, response);
        assertNull(session.getAttribute("contentPage"));
        assertEquals("login.jsp", path);
    }

}