package controller.impl.user;

import testClass.TestSession;
import testClass.TestUser;
import dto.DtoTable;
import entity.User;
import entity.UserTariff;
import enums.UserRole;
import exceptions.DbConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ITariffsService;
import service.IUserService;
import service.impl.DtoTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MainPageCommandTest {

    IUserService userService = mock(IUserService.class);
    ITariffsService tariffService = mock(ITariffsService.class);
    DtoTablesService dtoTablesService = DtoTablesService.getInstance();
    MainPageCommand mainPageCommand = new MainPageCommand(tariffService, userService, dtoTablesService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session= new TestSession();
    User testUser;
    List<UserTariff> testList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void processWithAdminRole() throws DbConnectionException {
        testUser = TestUser.getAdmin();
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("role", UserRole.ADMIN);
        DtoTable dtoTable = dtoTablesService.getDtoTable("table.user.dashboardTariffs");
        when(tariffService.getActiveTariffsUserCount(25)).thenReturn(5);
        when(tariffService.getActiveTariffsUserList(25, dtoTable)).thenReturn(testList);
        when(tariffService.calcMonthTotalUserExpenses(25)).thenReturn(BigDecimal.valueOf(100));
        when(tariffService.calcMonthTotalProfit()).thenReturn(BigDecimal.valueOf(2000));
        when(userService.getTotalUsersCount()).thenReturn(12);
        when(userService.getUserByLogin(testUser.getEmail())).thenReturn(testUser);

        String path = mainPageCommand.process(request, response);

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

        when(tariffService.getActiveTariffsUserCount(25)).thenReturn(5);
        when(tariffService.getActiveTariffsUserList(25, dtoTable)).thenReturn(testList);
        when(tariffService.calcMonthTotalUserExpenses(25)).thenReturn(BigDecimal.valueOf(100));
        when(tariffService.calcMonthTotalProfit()).thenReturn(BigDecimal.valueOf(2000));
        when(userService.getTotalUsersCount()).thenReturn(15);
        when(userService.getUserByLogin(testUser.getEmail())).thenReturn(testUser);

        String path = mainPageCommand.process(request, response);

        assertEquals("customer.jsp", path);
        assertEquals("fragments/contentUserDashboardPage.jsp", session.getAttribute("contentPage"));
        assertEquals(testUser, session.getAttribute("loggedUser"));
        assertEquals(testList, session.getAttribute("tableData"));
        assertEquals(dtoTable.getHead(), session.getAttribute("tableHead"));
        assertEquals(dtoTable.getSearch(), session.getAttribute("tableSearch"));
        assertEquals(dtoTable.getPagination(), session.getAttribute("tablePagination"));
        assertEquals(15, session.getAttribute("usersTotal"));
        assertEquals(BigDecimal.valueOf(2000), session.getAttribute("monthProfitTotal"));
        assertEquals(BigDecimal.valueOf(100), session.getAttribute("monthTotal"));

    }

    @Test
    void processIfDatabaseError() throws DbConnectionException {
        testUser = TestUser.getCustomer();
        session.setAttribute("loggedUser", testUser);
        session.setAttribute("role", UserRole.CUSTOMER);
        doThrow(new DbConnectionException("alert.databaseError")).when(tariffService).getActiveTariffsUserCount(25);

        String path = mainPageCommand.process(request, response);

        assertEquals("customer.jsp", path);
        assertEquals("fragments/contentUserDashboardPage.jsp", session.getAttribute("contentPage"));
        assertEquals("alert.databaseError", session.getAttribute("alert"));
        assertEquals(testUser, session.getAttribute("loggedUser"));
    }


}