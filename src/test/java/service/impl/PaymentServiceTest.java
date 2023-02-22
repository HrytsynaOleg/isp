package service.impl;

import dto.DtoTable;
import entity.*;
import enums.PaymentType;
import enums.SubscribeStatus;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IPaymentRepository;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import testClass.TestDtoTable;
import testClass.TestPayment;
import testClass.TestTariff;
import testClass.TestUser;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PaymentServiceTest {
    IUserRepository userRepo = mock(IUserRepository.class);
    IUserTariffRepository userTariffRepo = mock(IUserTariffRepository.class);
    IPaymentRepository paymentRepo = mock(IPaymentRepository.class);
    PaymentService service = new PaymentService(paymentRepo, userTariffRepo, userRepo);
    Payment testPayment;
    User testUser;

    @BeforeEach
    void setUp() {
        testUser = TestUser.getCustomer();
    }

    @Test
    void addIncomingPayment() throws SQLException, DbConnectionException {
        testPayment = TestPayment.getTestInPayment(testUser);
        testPayment.setId(0);
        List<UserTariff> pausedTariffs = new ArrayList<>();
        IntStream.range(1, 5).forEach(e -> {
            Tariff tariff = TestTariff.getTestTariff();
            UserTariff userTariff = new UserTariff(e, testUser, tariff, SubscribeStatus.PAUSED, null, null);
            pausedTariffs.add(userTariff);
        });
        when(userRepo.getUserById(25)).thenReturn(testUser);
        when(userTariffRepo.getUserActiveTariffList(25, new HashMap<>())).thenReturn(pausedTariffs);
        when(paymentRepo.addPayment(testPayment, pausedTariffs)).thenReturn(testPayment);

        service.addIncomingPayment(25, BigDecimal.valueOf(100));

        verify(paymentRepo, times(1)).addPayment(testPayment, pausedTariffs);
    }

    @Test
    void extendExpiredUserTariffs() throws SQLException, NotEnoughBalanceException, DbConnectionException {
        String userTariffWithdrawDescription = "TestServiceName tariff TestTariff is subscribed";
        testPayment = TestPayment.getTestOutPayment(testUser, userTariffWithdrawDescription);
        testPayment.setId(0);
        List<UserTariff> expiredTariffs = new ArrayList<>();
        IntStream.range(1, 5).forEach(e -> {
            Tariff tariff = TestTariff.getTestTariff();
            UserTariff userTariff = new UserTariff(e, testUser, tariff, SubscribeStatus.ACTIVE, null, LocalDate.now().minusDays(1));
            expiredTariffs.add(userTariff);

        });
        when(userTariffRepo.getAllExpiredUserActiveTariffList()).thenReturn(expiredTariffs);
        when(userRepo.getUserById(25)).thenReturn(testUser);
        when(paymentRepo.addWithdraw(testPayment, expiredTariffs.get(0))).thenReturn(testPayment);
        when(paymentRepo.addWithdraw(testPayment, expiredTariffs.get(1))).thenReturn(testPayment);
        when(paymentRepo.addWithdraw(testPayment, expiredTariffs.get(2))).thenReturn(testPayment);
        doThrow(new NotEnoughBalanceException()).when(paymentRepo).addWithdraw(testPayment, expiredTariffs.get(3));
        doNothing().when(userTariffRepo).updateUserTariff(expiredTariffs.get(3));

        service.extendExpiredUserTariffs();

        verify(paymentRepo, times(1)).addWithdraw(testPayment, expiredTariffs.get(3));
        verify(paymentRepo, times(1)).addWithdraw(testPayment, expiredTariffs.get(2));
        verify(paymentRepo, times(1)).addWithdraw(testPayment, expiredTariffs.get(1));
        verify(paymentRepo, times(1)).addWithdraw(testPayment, expiredTariffs.get(0));

        assertEquals(SubscribeStatus.PAUSED, expiredTariffs.get(3).getSubscribeStatus());
    }

    @Test
    void getPaymentsListByUserId() throws SQLException, DbConnectionException {
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        List<Payment> paymentList = getTestInPaymentsList(testUser);
        when(paymentRepo.getPaymentsListByUser(25, PaymentType.IN, parameters)).thenReturn(paymentList);

        List<Payment> result = service.getPaymentsListByUserId(dtoTable,25,PaymentType.IN);

        assertEquals(paymentList, result);
    }

    @Test
    void getPaymentsListAllUsers() throws SQLException, DbConnectionException {
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        List<Payment> paymentList = getTestInPaymentsList(testUser);
        when(paymentRepo.getPaymentsListAllUsers(PaymentType.IN, parameters)).thenReturn(paymentList);

        List<Payment> result = service.getPaymentsListAllUsers(dtoTable,PaymentType.IN);

        assertEquals(paymentList, result);
    }

    @Test
    void getPaymentsCountByUserId() throws SQLException, DbConnectionException {
        when(paymentRepo.getPaymentsCountByUserId(25,PaymentType.IN)).thenReturn(12);

        Integer result = service.getPaymentsCountByUserId(25,PaymentType.IN);

        assertEquals(12, result);
    }

    @Test
    void getPaymentsCountAllUsers() throws SQLException, DbConnectionException {
        when(paymentRepo.getPaymentsCountAllUsers(PaymentType.IN)).thenReturn(12);

        Integer result = service.getPaymentsCountAllUsers(PaymentType.IN);

        assertEquals(12, result);
    }

    private List<Payment> getTestInPaymentsList(User user) {
        List<Payment> paymentList = new ArrayList<>();
        IntStream.range(1, 5).forEach(e -> {
            Payment payment = TestPayment.getTestInPayment(user);
            payment.setId(e);
            paymentList.add(payment);
        });
        return paymentList;
    }
}