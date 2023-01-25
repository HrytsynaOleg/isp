package controller.impl.user;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import dto.DtoTable;
import entity.User;
import entity.UserTariff;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import service.impl.DtoTablesService;
import service.impl.TariffsService;
import service.impl.UserService;

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

class MainPageCommandTest {
    MainPageCommand mainPageCommand;
    UserService userService;
    TariffsService tariffService;
    DtoTablesService dtoTablesService;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    User testUser;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        tariffService = mock(TariffsService.class);
        dtoTablesService = DtoTablesService.getInstance();
        mainPageCommand = new MainPageCommand();
        Whitebox.setInternalState(MainPageCommand.class, "userService", userService);
        Whitebox.setInternalState(MainPageCommand.class, "tariffService", tariffService);
        Whitebox.setInternalState(MainPageCommand.class, "tableService", dtoTablesService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = new TestSession();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void processWithAdminRole() throws DbConnectionException {
        testUser = TestUser.getAdmin();
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("role", UserRole.ADMIN);
        DtoTable dtoTable = dtoTablesService.getDtoTable("table.user.dashboardTariffs");
        List<UserTariff> testList= getTestTariffList();
        when(tariffService.getActiveTariffsUserCount(25)).thenReturn(5);
        when(tariffService.getActiveTariffsUserList(25,dtoTable)).thenReturn(testList);
        when(tariffService.calcMonthTotalUserExpenses(25)).thenReturn(BigDecimal.valueOf(100));
        when(tariffService.calcMonthTotalProfit()).thenReturn(BigDecimal.valueOf(2000));
        when(userService.getTotalUsersCount()).thenReturn(12);
        when(userService.getUserByLogin(testUser.getEmail())).thenReturn(testUser);

        String path = mainPageCommand.process(request,response);

        assertEquals("admin.jsp", path);
        assertEquals("fragments/contentAdminDashboardPage.jsp", session.getAttribute("contentPage"));
        assertEquals(testUser, session.getAttribute("loggedUser"));
        assertEquals(testList, session.getAttribute("tableData"));
        assertEquals(dtoTable.getHead(), session.getAttribute("tableHead"));
        assertEquals(dtoTable.getSearch(), session.getAttribute("tableSearch"));
        assertEquals(dtoTable.getPagination(), session.getAttribute("tablePagination"));
        assertEquals(12, session.getAttribute("usersTotal"));
        assertEquals(BigDecimal.valueOf(2000), session.getAttribute("monthProfitTotal"));
        assertEquals(BigDecimal.valueOf(100), session.getAttribute("monthTotal"));

    }

    @Test
    void processWithUserRole() throws DbConnectionException {
        testUser = TestUser.getCustomer();
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("role", UserRole.CUSTOMER);
        DtoTable dtoTable = dtoTablesService.getDtoTable("table.user.dashboardTariffs");
        List<UserTariff> testList= getTestTariffList();
        when(tariffService.getActiveTariffsUserCount(25)).thenReturn(5);
        when(tariffService.getActiveTariffsUserList(25,dtoTable)).thenReturn(testList);
        when(tariffService.calcMonthTotalUserExpenses(25)).thenReturn(BigDecimal.valueOf(100));
        when(tariffService.calcMonthTotalProfit()).thenReturn(BigDecimal.valueOf(2000));
        when(userService.getTotalUsersCount()).thenReturn(12);
        when(userService.getUserByLogin(testUser.getEmail())).thenReturn(testUser);

        String path = mainPageCommand.process(request,response);

        assertEquals("customer.jsp", path);
        assertEquals("fragments/contentUserDashboardPage.jsp", session.getAttribute("contentPage"));
        assertEquals(testUser, session.getAttribute("loggedUser"));
        assertEquals(testList, session.getAttribute("tableData"));
        assertEquals(dtoTable.getHead(), session.getAttribute("tableHead"));
        assertEquals(dtoTable.getSearch(), session.getAttribute("tableSearch"));
        assertEquals(dtoTable.getPagination(), session.getAttribute("tablePagination"));
        assertEquals(12, session.getAttribute("usersTotal"));
        assertEquals(BigDecimal.valueOf(2000), session.getAttribute("monthProfitTotal"));
        assertEquals(BigDecimal.valueOf(100), session.getAttribute("monthTotal"));

    }

    @Test
    void processIfError() throws DbConnectionException {
        testUser = TestUser.getCustomer();
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("role", UserRole.CUSTOMER);
        DtoTable dtoTable = dtoTablesService.getDtoTable("table.user.dashboardTariffs");
        List<UserTariff> testList= getTestTariffList();
        doThrow(new DbConnectionException("alert.databaseError")).when(tariffService).getActiveTariffsUserCount(25);

        String path = mainPageCommand.process(request,response);

        assertEquals("customer.jsp", path);
        assertEquals("fragments/contentUserDashboardPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals(testUser, session.getAttribute("loggedUser"));
        assertNull(session.getAttribute("tableData"));
        assertNull(session.getAttribute("tableHead"));
        assertNull(session.getAttribute("tableSearch"));
        assertNull(session.getAttribute("tablePagination"));
        assertNull(session.getAttribute("usersTotal"));
        assertNull(session.getAttribute("monthProfitTotal"));
        assertNull(session.getAttribute("monthTotal"));

    }

    private List<UserTariff> getTestTariffList() {
        List<UserTariff> result = new ArrayList<>();
        IntStream.range(1,6).forEach(e->{
            UserTariff tariff = new UserTariff(e,null,null,null,null,null);
            result.add(tariff);
        });
        return result;
    }


}