package controller.impl.tariff;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.Tariff;
import entity.User;
import enums.FileFormat;
import enums.UserRole;
import exceptions.BuildPriceException;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ITariffsService;
import service.PriceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DownloadTariffCommandTest {
    ITariffsService tariffsService = mock(ITariffsService.class);
    PriceService priceService = mock(PriceService.class);
    DownloadTariffCommand command = new DownloadTariffCommand(tariffsService,priceService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    FileFormat fileFormat;
    List<Tariff> tariffList = new ArrayList<>();
    User testUser = TestUser.getCustomer();

    @BeforeEach
    void setUp() {

        when(request.getSession()).thenReturn(session);
        fileFormat = FileFormat.PDF;
        session.setAttribute("role", UserRole.CUSTOMER);
        session.setAttribute("loggedUser", testUser);
    }

    @Test
    void process() throws BuildPriceException, DbConnectionException {

        when(request.getParameter("fileFormat")).thenReturn("PDF");
        when(tariffsService.getPriceTariffsList()).thenReturn(tariffList);
        when(priceService.createPrice(tariffList,fileFormat)).thenReturn("//filepath");

        String path = command.process(request, response);

        assertEquals("download?path=//filepath&format=PDF", path);
        assertEquals("fragments/contentTariffsListUserPage.jsp", session.getAttribute("contentPage"));
    }

    @Test
    void processIfDatabaseError() throws DbConnectionException, BuildPriceException {
        when(request.getParameter("fileFormat")).thenReturn("PDF");
        when(priceService.createPrice(tariffList,fileFormat)).thenReturn("//filepath");

        doThrow(new DbConnectionException("alert.databaseError")).when(tariffsService).getPriceTariffsList();
        String path = command.process(request, response);

        assertEquals("fragments/contentTariffsListUserPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals("customer.jsp", path);
    }

    @Test
    void processIfBuildPriceException() throws DbConnectionException, BuildPriceException {
        when(request.getParameter("fileFormat")).thenReturn("PDF");
        when(tariffsService.getPriceTariffsList()).thenReturn(tariffList);

        doThrow(new BuildPriceException("alert.priceBuildError")).when(priceService).createPrice(tariffList,fileFormat);
        String path = command.process(request, response);

        assertEquals("fragments/contentTariffsListUserPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.priceBuildError", session.getAttribute("alert"));
        assertEquals("customer.jsp", path);
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