package service.impl;

import dto.DtoTable;
import dto.DtoTariff;
import entity.Tariff;
import entity.User;
import entity.UserTariff;
import enums.SubscribeStatus;
import exceptions.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import service.MapperService;
import service.ValidatorService;
import testClass.TestDtoTable;
import testClass.TestTariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ITariffRepository;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import testClass.TestUser;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TariffsServiceTest {
    IUserRepository userRepo = mock(IUserRepository.class);
    IUserTariffRepository userTariffRepo = mock(IUserTariffRepository.class);
    ITariffRepository tariffRepo = mock(ITariffRepository.class);
    TariffsService service = new TariffsService(tariffRepo, userTariffRepo, userRepo);
    Tariff testTariff;

    @BeforeEach
    void setUp() {
        testTariff = TestTariff.getTestTariff();
    }

    @Test
    void getTariff() throws SQLException, DbConnectionException {
        when(tariffRepo.getTariffById(5)).thenReturn(testTariff);
        Tariff result = service.getTariff(5);
        assertEquals(testTariff, result);
    }

    @Test
    void getTariffIfNoSuchElementException() throws SQLException {
        doThrow(new NoSuchElementException()).when(tariffRepo).getTariffById(5);
        assertThrows(NoSuchElementException.class, () -> service.getTariff(5), "alert.notFoundTariff");
    }

    @Test
    void getTariffIfDatabaseError() throws SQLException {
        doThrow(new SQLException()).when(tariffRepo).getTariffById(5);
        assertThrows(DbConnectionException.class, () -> service.getTariff(5), "alert.databaseError");
    }

    @Test
    void addTariff() throws SQLException, DbConnectionException, IncorrectFormatException {
        DtoTariff dtoTariff = MapperService.toDtoTariff(testTariff);
        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> ignored = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toTariff(dtoTariff)).thenReturn(testTariff);
            when(tariffRepo.isTariffNameExist("TestTariff")).thenReturn(false);
            when(tariffRepo.addTariff(testTariff)).thenReturn(testTariff);

            Tariff result = service.addTariff(dtoTariff);

            assertEquals(testTariff, result);
        }
    }

    @Test
    void addTariffIfNameExist() throws SQLException {
        DtoTariff dtoTariff = MapperService.toDtoTariff(testTariff);
        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> ignored = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toTariff(dtoTariff)).thenReturn(testTariff);
            when(tariffRepo.isTariffNameExist("TestTariff")).thenReturn(true);
            when(tariffRepo.addTariff(testTariff)).thenReturn(testTariff);

            assertThrows(IncorrectFormatException.class, () -> service.addTariff(dtoTariff));
        }
    }
    @Test
    void addTariffIfValidatorException() throws SQLException {
        DtoTariff dtoTariff = MapperService.toDtoTariff(testTariff);
        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> validator = Mockito.mockStatic(ValidatorService.class)) {
            validator.when(() -> ValidatorService.validateEmptyString("TestTariff", "alert.emptyNameField"))
                    .thenThrow(new IncorrectFormatException("alert.passwordNotMatch"));
            mapper.when(() -> MapperService.toTariff(dtoTariff)).thenReturn(testTariff);
            when(tariffRepo.isTariffNameExist("TestTariff")).thenReturn(false);
            when(tariffRepo.addTariff(testTariff)).thenReturn(testTariff);

            assertThrows(IncorrectFormatException.class, () -> service.addTariff(dtoTariff));
        }
    }
    @Test
    void addTariffIfDatabaseError() throws SQLException {
        DtoTariff dtoTariff = MapperService.toDtoTariff(testTariff);
        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> ignored = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toTariff(dtoTariff)).thenReturn(testTariff);
            when(tariffRepo.isTariffNameExist("TestTariff")).thenReturn(false);
            doThrow(new SQLException()).when(tariffRepo).addTariff(testTariff);

            assertThrows(DbConnectionException.class, () -> service.addTariff(dtoTariff));
        }
    }

    @Test
    void updateTariff() throws SQLException, DbConnectionException, IncorrectFormatException {
        DtoTariff dtoTariff = MapperService.toDtoTariff(testTariff);
        Tariff oldTariff = TestTariff.getTestTariff();
        Tariff newTariff = TestTariff.getTestTariff();
        newTariff.setName("NewTestTariff");
        List<UserTariff> subscribers = new ArrayList<>();
        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> ignored = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toTariff(dtoTariff)).thenReturn(newTariff);
            when(tariffRepo.getTariffById(5)).thenReturn(oldTariff);
            when(tariffRepo.isTariffNameExist("TestTariff")).thenReturn(false);
            when(userTariffRepo.getTariffSubscribersList(5)).thenReturn(subscribers);
            doNothing().when(tariffRepo).updateTariff(newTariff,oldTariff,subscribers);

            Tariff result = service.updateTariff(dtoTariff);

            assertEquals(newTariff, result);
        }
    }

    @Test
    void deleteTariff() throws SQLException, DbConnectionException, RelatedRecordsExistException {
        List<UserTariff> subscribers = new ArrayList<>();
        when(userTariffRepo.getTariffSubscribersList(5)).thenReturn(subscribers);
        doNothing().when(tariffRepo).deleteTariff(5);
        service.deleteTariff(5);
        verify(tariffRepo, times(1)).deleteTariff(5);
    }

    @Test
    void getTariffsList() throws SQLException, DbConnectionException {
        List<Tariff> tariffList = getTestTariffList();
        tariffList.add(testTariff);
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(tariffRepo.getTariffsList(parameters)).thenReturn(tariffList);

        List<Tariff> result = service.getTariffsList(dtoTable);

        assertEquals(tariffList, result);
    }

    @Test
    void getTariffsUserList() throws SQLException, DbConnectionException {
        User user = TestUser.getCustomer();
        List<Tariff> tariffList = getTestTariffList();
        Map<Integer,UserTariff> userTariffMap = new HashMap<>();
        IntStream.range(1,5).forEach(e->{
           UserTariff item = new UserTariff(e,user,tariffList.get(e-1), SubscribeStatus.UNSUBSCRIBE,null,null);
            userTariffMap.put(e,item);
        });

        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(tariffRepo.getTariffsList(parameters)).thenReturn(tariffList);
        when(userTariffRepo.getUserTariff(1,10)).thenReturn(Optional.of(userTariffMap.get(1)));
        when(userTariffRepo.getUserTariff(2,10)).thenReturn(Optional.of(userTariffMap.get(2)));
        when(userTariffRepo.getUserTariff(3,10)).thenReturn(Optional.of(userTariffMap.get(3)));
        when(userTariffRepo.getUserTariff(4,10)).thenReturn(Optional.of(userTariffMap.get(4)));

        List<Tariff> result = service.getTariffsUserList(10,dtoTable);

        assertEquals(tariffList, result);
    }

    @Test
    void getActiveTariffsUserList() throws SQLException, DbConnectionException {
        User user = TestUser.getCustomer();
        List<UserTariff> tariffList = getTestUserTariffList(user);
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(userTariffRepo.getUserActiveTariffList(10,parameters)).thenReturn(tariffList);

        List<UserTariff> result = service.getActiveTariffsUserList(10,dtoTable);

        assertEquals(tariffList, result);
    }

    @Test
    void getActiveTariffsUserCount() throws SQLException, DbConnectionException {
        when(userTariffRepo.getUserActiveTariffCount(10)).thenReturn(5);

        int result = service.getActiveTariffsUserCount(10);

        assertEquals(5, result);
    }

    @Test
    void getPriceTariffsList() throws SQLException, DbConnectionException {
        List<Tariff> tariffList = getTestTariffList();
        when(tariffRepo.getPriceTariffsList()).thenReturn(tariffList);

        List<Tariff> result = service.getPriceTariffsList();

        assertEquals(tariffList, result);
    }

    @Test
    void getTariffsCount() throws SQLException, DbConnectionException {
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(tariffRepo.getTariffsCount(parameters)).thenReturn(5);

        Integer result = service.getTariffsCount(dtoTable);

        assertEquals(5, result);
    }

    @Test
    void subscribeTariff() throws SQLException, TariffAlreadySubscribedException, DbConnectionException, NotEnoughBalanceException {
        User user = TestUser.getCustomer();
        user.setBalance(BigDecimal.valueOf(200));
        when(userTariffRepo.userTariffCount(5,25)).thenReturn(0);
        when(tariffRepo.getTariffById(5)).thenReturn(testTariff);
        when(userRepo.getUserById(25)).thenReturn(user);
        List<UserTariff> userTariffList = getTestUserTariffList(user);
        when(userTariffRepo.getUserTariffListByService(2,25)).thenReturn(userTariffList);
        LocalDate dateEnd = testTariff.getPeriod().getNexDate(LocalDate.now());
        UserTariff newUserTariff = new UserTariff(0, user, testTariff, SubscribeStatus.ACTIVE, LocalDate.now(), dateEnd);
        Optional<UserTariff> oldUserTariff = userTariffList.stream().findFirst();
        doNothing().when(tariffRepo).subscribeTariff(testTariff,newUserTariff,oldUserTariff);

        service.subscribeTariff(5,25);

        verify(tariffRepo, times(1)).getTariffById(5);
        verify(userTariffRepo, times(1)).userTariffCount(5,25);
        verify(userRepo, times(1)).getUserById(25);
        verify(userTariffRepo, times(1)).getUserTariffListByService(2,25);
        verify(tariffRepo, times(1)).subscribeTariff(testTariff,newUserTariff,oldUserTariff);
    }

    @Test
    void unsubscribeTariff() throws SQLException, DbConnectionException {
        User user = TestUser.getCustomer();
        LocalDate dateEnd = testTariff.getPeriod().getNexDate(LocalDate.now());
        UserTariff userTariff = new UserTariff(0, user, testTariff, SubscribeStatus.ACTIVE, LocalDate.now(), dateEnd);
        when(userTariffRepo.getUserTariff(5,25)).thenReturn(Optional.of(userTariff));
        doNothing().when(tariffRepo).unsubscribeTariff(userTariff);

        service.unsubscribeTariff(5,25);

        verify(tariffRepo, times(1)).unsubscribeTariff(userTariff);
    }

    @Test
    void calcMonthTotalUserExpenses() throws SQLException, DbConnectionException {
        User user = TestUser.getCustomer();
        List<UserTariff> tariffs = getTestUserTariffList(user);
        when(userTariffRepo.getUserActiveTariffList(25,new HashMap<>())).thenReturn(tariffs);

        BigDecimal result = service.calcMonthTotalUserExpenses(25);

        assertEquals(BigDecimal.valueOf(400), result);
    }

    @Test
    void calcMonthTotalProfit() throws SQLException, DbConnectionException {
        User user = TestUser.getCustomer();
        List<UserTariff> tariffs = getTestUserTariffList(user);
        when(userTariffRepo.getAllActiveTariffList()).thenReturn(tariffs);

        BigDecimal result = service.calcMonthTotalProfit();

        assertEquals(BigDecimal.valueOf(400), result);
    }

    private List<Tariff> getTestTariffList(){
        List<Tariff> tariffList = new ArrayList<>();
        IntStream.range(1,5).forEach(e->{
            Tariff item = TestTariff.getTestTariff();
            item.setId(e);
            tariffList.add(item);
        });
        return tariffList;
    }

    private List<UserTariff> getTestUserTariffList(User user){
        List<UserTariff> tariffList = new ArrayList<>();
        IntStream.range(1,5).forEach(e->{
            Tariff tariff = TestTariff.getTestTariff();
            tariff.setId(e);
            UserTariff userTariff = new UserTariff(e,user,tariff, SubscribeStatus.ACTIVE,null,null);
            tariffList.add(userTariff);
        });
        return tariffList;
    }
}