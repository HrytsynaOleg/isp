package service.impl;

import dto.DtoService;
import dto.DtoTable;
import entity.Service;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.RelatedRecordsExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import repository.IServicesRepository;
import repository.ITariffRepository;
import service.MapperService;
import service.ValidatorService;
import testClass.TestDtoTable;
import testClass.TestService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void deleteService() throws SQLException, DbConnectionException, RelatedRecordsExistException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("whereValue", "services_id=2");
        when(tariffsRepo.getTariffsCount(parameters)).thenReturn(0);
        doNothing().when(servicesRepo).deleteService(2);

        service.deleteService(2);

        verify(servicesRepo, times(1)).deleteService(2);
    }

    @Test
    void updateService() throws SQLException, DbConnectionException, IncorrectFormatException {
        DtoService dtoService = new DtoService("2", "TestService", "TestServiceDescription");
        Service newService = new Service(2, "TestService", "TestServiceDescription");
        try (MockedStatic<MapperService> mapper = Mockito.mockStatic(MapperService.class);
             MockedStatic<ValidatorService> ignored = Mockito.mockStatic(ValidatorService.class)) {
            mapper.when(() -> MapperService.toService(dtoService)).thenReturn(newService);
            when(servicesRepo.isServiceNameExist("TestService")).thenReturn(false);
            doNothing().when(servicesRepo).updateService(newService);
            when(servicesRepo.getServiceById(2)).thenReturn(newService);

            Service result = service.updateService(dtoService);

            assertEquals(newService, result);
        }
    }

    @Test
    void getServicesList() throws SQLException, DbConnectionException {
        DtoTable dtoTable = TestDtoTable.getTable();
        List<Service> serviceList = getTestServicesList();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(servicesRepo.getServicesList(parameters)).thenReturn(serviceList);

        List<Service> result = service.getServicesList(dtoTable);

        assertEquals(serviceList, result);
    }

    @Test
    void getAllServicesList() throws SQLException, DbConnectionException {
        List<Service> serviceList = getTestServicesList();
        Map<String, String> parameters = new HashMap<>();
        when(servicesRepo.getServicesList(parameters)).thenReturn(serviceList);

        List<Service> result = service.getAllServicesList();

        assertEquals(serviceList, result);
    }

    @Test
    void getServicesCount() throws SQLException, DbConnectionException {
        DtoTable dtoTable = TestDtoTable.getTable();
        Map<String, String> parameters = dtoTable.buildQueryParameters();
        when(servicesRepo.getServicesCount(parameters)).thenReturn(12);

        Integer result = service.getServicesCount(dtoTable);

        assertEquals(12, result);
    }

    private List<Service> getTestServicesList(){
        List<Service> servicesList = new ArrayList<>();
        IntStream.range(1,5).forEach(e->{
            Service item = TestService.getTestService();
            item.setId(e);
            servicesList.add(item);
        });
        return servicesList;
    }
}