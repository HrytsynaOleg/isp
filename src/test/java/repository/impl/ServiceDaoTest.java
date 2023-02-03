package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import dao.impl.ServiceDao;
import entity.Service;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(MockitoExtension.class)
class ServiceDaoTest {
//    @Mock
    IDao serviceDao = mock(IDao.class);

    Connection connection = mock(Connection.class);

    ServicesRepositoryImpl repository = new ServicesRepositoryImpl(serviceDao);

    Service testService;
    Service newTestService;


    @BeforeAll
    void setUp() {

        testService = new Service(1, "Test", "Service");
        newTestService = new Service(2, "Test", "Service");

    }

    @Test
    void ifAddServiceReturnNull() throws SQLException {

        try (MockedStatic<DbConnectionPool> pool = Mockito.mockStatic(DbConnectionPool.class)) {
//            Whitebox.setInternalState(ServicesRepositoryImpl.class, "serviceDao", serviceDao);
            pool.when(DbConnectionPool::getConnection).thenReturn(connection);
            Optional<Service> res = Optional.empty();
            when(serviceDao.add(connection, testService)).thenReturn(res);
            assertThrows(SQLException.class, () -> {
                repository.addService(testService);
            });
        }

    }

    @Test
    void addService() throws SQLException {


        try (MockedStatic<DbConnectionPool> pool = Mockito.mockStatic(DbConnectionPool.class)) {
//            Whitebox.setInternalState(ServicesRepositoryImpl.class, "serviceDao", serviceDao);
            pool.when(DbConnectionPool::getConnection).thenReturn(connection);
            Optional<Service> res = Optional.of(newTestService);
            when(serviceDao.add(connection, testService)).thenReturn(res);
            assertEquals(2, repository.addService(testService));

        }

    }
}