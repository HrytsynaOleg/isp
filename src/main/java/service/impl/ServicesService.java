package service.impl;

import repository.IServicesRepository;
import repository.ITariffRepository;
import dto.DtoService;
import dto.DtoTable;
import entity.Service;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.RelatedRecordsExistException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IServicesService;
import service.MapperService;
import service.ValidatorService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ServicesService implements IServicesService {

    private final IServicesRepository servicesRepo;
    private final ITariffRepository tariffsRepo;
    private static final Logger logger = LogManager.getLogger(ServicesService.class);

    public ServicesService(IServicesRepository servicesRepo, ITariffRepository tariffsRepo) {
        this.servicesRepo = servicesRepo;
        this.tariffsRepo = tariffsRepo;
    }

    public Service getService(int id) throws DbConnectionException, NoSuchElementException {
        try {
            return servicesRepo.getServiceById(id);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        } catch (NoSuchElementException e) {
            logger.error(e);
            throw new NoSuchElementException("alert.notFoundService");
        }
    }

    public Service addService(DtoService dtoService) throws DbConnectionException, IncorrectFormatException {

        ValidatorService.validateEmptyString(dtoService.getName(), "alert.emptyNameField");
        ValidatorService.validateEmptyString(dtoService.getDescription(), "alert.emptyDescriptionField");

        try {
            Service service = MapperService.toService(dtoService);
            if (servicesRepo.isServiceNameExist(dtoService.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            int serviceId = servicesRepo.addService(service);
            service.setId(serviceId);
            return service;
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void deleteService(int id) throws DbConnectionException, RelatedRecordsExistException {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("whereColumn", "services_id");
        parameters.put("whereValue", String.valueOf(id));

        try {
            int tariffsCount = tariffsRepo.getTariffsCount(parameters);
            if (tariffsCount > 0) throw new RelatedRecordsExistException("alert.relatedRecordsExist");
            servicesRepo.deleteService(id);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    public Service updateService(DtoService dtoService) throws DbConnectionException, IncorrectFormatException {

        ValidatorService.validateEmptyString(dtoService.getName(), "alert.emptyNameField");
        ValidatorService.validateEmptyString(dtoService.getDescription(), "alert.emptyDescriptionField");

        try {
            Service service = MapperService.toService(dtoService);
            if (servicesRepo.isServiceNameExist(dtoService.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            servicesRepo.updateService(service);
            service = servicesRepo.getServiceById(Integer.parseInt(dtoService.getId()));
            return service;
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Service> getServicesList(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return servicesRepo.getServicesList(parameters);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Service> getAllServicesList() throws DbConnectionException {
        try {
            return servicesRepo.getServicesList(null);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public Integer getServicesCount(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return servicesRepo.getServicesCount(parameters);
        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }
    }
}

