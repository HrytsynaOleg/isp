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
import org.mockito.MockSettings;
import service.ITariffsService;
import service.PriceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}