package service.impl;

import dto.DtoService;
import entity.Service;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import repository.IServicesRepository;
import repository.ITariffRepository;
import service.MapperService;
import service.ValidatorService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServicesServiceTest {
    IServicesRepository servicesRepo = mock(IServicesRepository.class);
    ITariffRepository tariffsRepo = mock(ITariffRepository.class);
    ServicesService service = new ServicesService(servicesRepo, tariffsRepo);
    Service testService;

    @BeforeEach
    void setUp() {
        testService = new Service(10, "TestService", "TestServiceDescription");
    }

    @Test
    void getService() throws SQLException, DbConnectionException {
        when(servicesRepo.getServiceById(10)).thenReturn(testService);

        Service result = service.getService(10);

        assertEquals(testService, result);
    }

    @Test
    void addService() throws SQLException, DbConnectionException, IncorrectFormatException {
        DtoService dtoService = new DtoService("", "TestService", "TestServiceDescription");
        Service newService = new Service(0, "TestService", "TestServiceDescription");
        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> ignored = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toService(dtoService)).thenReturn(newService);
            when(servicesRepo.isServiceNameExist("TestService")).thenReturn(false);
            when(servicesRepo.addService(newService)).thenReturn(10);

            Service result = service.addService(dtoService);

            assertEquals(testService, result);
        }
    }

    @Test
    void deleteService() {
    }

    @Test
    void updateService() {
    }

    @Test
    void getServicesList() {
    }

    @Test
    void getAllServicesList() {
    }

    @Test
    void getServicesCount() {
    }
}