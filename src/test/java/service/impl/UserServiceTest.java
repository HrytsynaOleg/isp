package service.impl;

import controller.testClass.TestDtoTable;
import controller.testClass.TestUser;
import dto.DtoTable;
import dto.DtoUser;
import entity.User;
import entity.UserTariff;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import service.IEmailService;
import service.MapperService;
import service.SecurityService;
import service.ValidatorService;
import settings.Regex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    IUserRepository userRepo = mock(IUserRepository.class);
    IUserTariffRepository userTariffRepo = mock(IUserTariffRepository.class);
    SecurityService securityService = mock(SecurityService.class);
    IEmailService emailService = mock(IEmailService.class);
    UserService service = new UserService(userRepo, userTariffRepo, securityService, emailService);
    User testUser = TestUser.getCustomer();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getUser() throws SQLException, DbConnectionException {
        String userName = "customer";
        String password = "password";
        when(userRepo.getUserByLogin(userName)).thenReturn(testUser);
        when(securityService.isPasswordVerify(password, testUser.getPassword())).thenReturn(true);

        User result = service.getUser(userName, password);

        assertEquals(testUser, result);
    }

    @Test
    void getUserIfPasswordDoesNotMatch() throws SQLException {
        String userName = "customer";
        String password = "password";
        when(userRepo.getUserByLogin(userName)).thenReturn(testUser);
        when(securityService.isPasswordVerify(password, testUser.getPassword())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.getUser(userName, password), "alert.notFoundUser");
    }

    @Test
    void getUserIfUserNotFound() throws SQLException {
        String userName = "customer";
        String password = "password";
        doThrow(new NoSuchElementException()).when(userRepo).getUserByLogin(userName);
        when(securityService.isPasswordVerify(password, testUser.getPassword())).thenReturn(true);

        assertThrows(NoSuchElementException.class, () -> service.getUser(userName, password), "alert.notFoundUser");
    }

    @Test
    void getUserIfDatabaseError() throws SQLException {
        String userName = "customer";
        String password = "password";
        doThrow(new SQLException()).when(userRepo).getUserByLogin(userName);
        when(securityService.isPasswordVerify(password, testUser.getPassword())).thenReturn(true);
        assertThrows(DbConnectionException.class, () -> service.getUser(userName, password), "alert.databaseError");
    }

    @Test
    void getUserByLogin() throws SQLException, DbConnectionException {
        String userName = "customer";
        when(userRepo.getUserByLogin(userName)).thenReturn(testUser);
        User result = service.getUserByLogin(userName);
        assertEquals(testUser, result);
    }

    @Test
    void getUserByLoginIfUserNotFound() throws SQLException {
        String userName = "customer";
        doThrow(new NoSuchElementException()).when(userRepo).getUserByLogin(userName);
        assertThrows(NoSuchElementException.class, () -> service.getUserByLogin(userName), "alert.notFoundUser");
    }

    @Test
    void getUserByLoginIfDatabaseError() throws SQLException {
        String userName = "customer";
        doThrow(new SQLException()).when(userRepo).getUserByLogin(userName);
        assertThrows(DbConnectionException.class, () -> service.getUserByLogin(userName), "alert.databaseError");
    }

    @Test
    void blockUser() throws SQLException, DbConnectionException {
        List<UserTariff> userTariffList = new ArrayList<>();
        when(userRepo.getUserById(25)).thenReturn(testUser);
        when(userTariffRepo.getSubscribedUserTariffList(25)).thenReturn(userTariffList);
        doNothing().when(userRepo).blockUser(testUser, userTariffList);
        service.blockUser(25);
        verify(userRepo, times(1)).blockUser(testUser, userTariffList);
        verify(userRepo, times(1)).getUserById(25);
        verify(userTariffRepo, times(1)).getSubscribedUserTariffList(25);
    }

    @Test
    void blockUserIfDatabaseError() throws SQLException {
        List<UserTariff> userTariffList = new ArrayList<>();
        doThrow(new SQLException()).when(userRepo).getUserById(25);
        doNothing().when(userRepo).blockUser(testUser, userTariffList);
        assertThrows(DbConnectionException.class, () -> service.blockUser(25), "alert.databaseError");
    }

    @Test
    void unblockUser() throws SQLException, DbConnectionException {
        List<UserTariff> userTariffList = new ArrayList<>();
        when(userRepo.getUserById(25)).thenReturn(testUser);
        when(userTariffRepo.getBlockedUserTariffList(25)).thenReturn(userTariffList);
        doNothing().when(userRepo).unblockUser(testUser, userTariffList);
        service.unblockUser(25);
        verify(userRepo, times(1)).unblockUser(testUser, userTariffList);
        verify(userRepo, times(1)).getUserById(25);
        verify(userTariffRepo, times(1)).getBlockedUserTariffList(25);
    }

    @Test
    void unblockUserIfDatabaseError() throws SQLException {
        List<UserTariff> userTariffList = new ArrayList<>();
        doThrow(new SQLException()).when(userRepo).getUserById(25);
        doNothing().when(userRepo).unblockUser(testUser, userTariffList);
        assertThrows(DbConnectionException.class, () -> service.unblockUser(25), "alert.databaseError");
    }

    @Test
    void setUserPassword() throws SQLException, DbConnectionException, IncorrectFormatException {
        String password = "password";
        String confirm = "password";
        String hash = "passwordHash";
        try (MockedStatic<ValidatorService> validator = Mockito.mockStatic(ValidatorService.class)) {
            when(userRepo.getUserById(25)).thenReturn(testUser);
            doNothing().when(userRepo).setUserPassword(testUser, hash);
            when(securityService.getPasswordHash(password)).thenReturn(hash);

            service.setUserPassword(25, password, confirm);

            verify(userRepo, times(1)).setUserPassword(testUser, hash);
            verify(userRepo, times(1)).getUserById(25);
            verify(securityService, times(1)).getPasswordHash(password);
        }
    }

    @Test
    void setUserPasswordIfPasswordIncorrect() throws SQLException {
        String password = "password";
        String confirm = "password";
        String hash = "passwordHash";
        try (MockedStatic<ValidatorService> validator = Mockito.mockStatic(ValidatorService.class)) {
            validator.when(() -> ValidatorService.validateString(password, Regex.PASSWORD_REGEX, "alert.incorrectPassword"))
                    .thenThrow(new IncorrectFormatException("alert.incorrectPassword"));
            when(userRepo.getUserById(25)).thenReturn(testUser);
            doNothing().when(userRepo).setUserPassword(testUser, hash);
            when(securityService.getPasswordHash(password)).thenReturn(hash);

            assertThrows(IncorrectFormatException.class, () -> service.setUserPassword(25, password, confirm), "alert.incorrectPassword");
        }
    }

    @Test
    void setUserPasswordIfPasswordNotMatch() throws SQLException {
        String password = "password";
        String confirm = "password";
        String hash = "passwordHash";
        try (MockedStatic<ValidatorService> validator = Mockito.mockStatic(ValidatorService.class)) {
            validator.when(() -> ValidatorService.validateConfirmPassword(password, confirm, "alert.passwordNotMatch"))
                    .thenThrow(new IncorrectFormatException("alert.passwordNotMatch"));
            when(userRepo.getUserById(25)).thenReturn(testUser);
            doNothing().when(userRepo).setUserPassword(testUser, hash);
            when(securityService.getPasswordHash(password)).thenReturn(hash);

            assertThrows(IncorrectFormatException.class, () -> service.setUserPassword(25, password, confirm), "alert.passwordNotMatch");
        }
    }

    @Test
    void setUserPasswordIfDatabaseError() throws SQLException {
        String password = "password";
        String confirm = "password";
        String hash = "passwordHash";
        try (MockedStatic<ValidatorService> validator = Mockito.mockStatic(ValidatorService.class)) {
            when(userRepo.getUserById(25)).thenReturn(testUser);
            doThrow(new SQLException()).when(userRepo).setUserPassword(testUser, hash);
            when(securityService.getPasswordHash(password)).thenReturn(hash);

            assertThrows(DbConnectionException.class, () -> service.setUserPassword(25, password, confirm), "alert.databaseError");
        }
    }

    @Test
    void getUsersList() throws SQLException, DbConnectionException {
        List<User> userList = new ArrayList<>();
        userList.add(TestUser.getAdmin());
        userList.add(TestUser.getCustomer());
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(userRepo.getUsersList(parameters)).thenReturn(userList);

        List<User> result = service.getUsersList(dtoTable);

        assertEquals(userList, result);
    }

    @Test
    void getUsersListIfDatabaseError() throws SQLException {
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        doThrow(new SQLException()).when(userRepo).getUsersList(parameters);

        assertThrows(DbConnectionException.class, () -> service.getUsersList(dtoTable), "alert.databaseError");
    }

    @Test
    void getUsersCount() throws SQLException, DbConnectionException {
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(userRepo.getUsersCount(parameters)).thenReturn(12);

        int result = service.getUsersCount(dtoTable);

        assertEquals(12,result);
    }
    @Test
    void getUsersCountIfDatabaseError() throws SQLException {
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        doThrow(new SQLException()).when(userRepo).getUsersCount(parameters);

        assertThrows(DbConnectionException.class, () -> service.getUsersCount(dtoTable), "alert.databaseError");
    }

    @Test
    void getTotalUsersCount() throws SQLException, DbConnectionException {
        when(userRepo.getUsersCount(null)).thenReturn(12);
        int result = service.getTotalUsersCount();
        assertEquals(12,result);
    }
    @Test
    void getTotalUsersCountIfDatabaseError() throws SQLException{
        doThrow(new SQLException()).when(userRepo).getUsersCount(null);
        assertThrows(DbConnectionException.class, () -> service.getTotalUsersCount(), "alert.databaseError");
    }

    @Test
    void isUserExist() throws SQLException, DbConnectionException {
        String userName = "username";
        when(userRepo.getUserByLogin(userName)).thenReturn(testUser);

        boolean result = service.isUserExist(userName);

        assertTrue(result);
    }
    @Test
    void isUserExistNoSuchElementException() throws SQLException, DbConnectionException {
        String userName = "username";
        when(userRepo.getUserByLogin(userName)).thenReturn(testUser);
        doThrow(new NoSuchElementException()).when(userRepo).getUserByLogin(userName);

        boolean result = service.isUserExist(userName);

        assertFalse(result);
    }
    @Test
    void isUserExistIfDatabaseError() throws SQLException{
        String userName = "username";
        when(userRepo.getUserByLogin(userName)).thenReturn(testUser);
        doThrow(new SQLException()).when(userRepo).getUserByLogin(userName);

        assertThrows(DbConnectionException.class, () -> service.isUserExist(userName), "alert.databaseError");
    }

    @Test
    void addUser() throws SQLException, DbConnectionException, IncorrectFormatException {
        DtoUser dtoUser = new DtoUser();
        User newUser = TestUser.getCustomer();
        newUser.setPassword("hash");

        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> validator = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toUser(dtoUser)).thenReturn(testUser);
            when(securityService.getPasswordHash("")).thenReturn("hash");
            when(userRepo.addUser(testUser)).thenReturn(newUser);
            doNothing().when(emailService).sendEmail("test@mail.com","ISP Registration","Your password: hash");

            User result = service.addUser(dtoUser);

            assertEquals(newUser,result);
        }
    }

    @Test
    void updateUser() throws SQLException, DbConnectionException, IncorrectFormatException {
        DtoUser dtoUser = new DtoUser();
        User newUser = TestUser.getCustomer();

        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> validator = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toUser(dtoUser)).thenReturn(newUser);
            doNothing().when(userRepo).updateUserProfile(testUser);

            User result = service.updateUser(dtoUser);

            assertEquals(newUser,result);
        }
    }
}