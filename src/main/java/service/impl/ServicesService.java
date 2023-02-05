package service.impl;

import dependecies.DependencyManager;
import repository.IServicesRepository;
import repository.ITariffRepository;
import repository.impl.TariffRepositoryImpl;
import dto.DtoService;
import dto.DtoTable;
import entity.Service;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.RelatedRecordsExistException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IServicesService;
import service.IValidatorService;
import service.MapperService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ServicesService implements IServicesService {

    private final IServicesRepository servicesRepo;
    private static final ITariffRepository tariffsRepo = DependencyManager.tariffRepo;
    private static final IValidatorService validator = new ValidatorService();
    private static final Logger logger = LogManager.getLogger(ServicesService.class);

    public ServicesService(IServicesRepository servicesRepo) {
        this.servicesRepo = servicesRepo;
    }

    public Service getService(int id) throws DbConnectionException, NoSuchElementException {
        Service service;
        try {
            service = servicesRepo.getServiceById(id);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        } catch (NoSuchElementException e) {
            logger.error(e);
            throw new NoSuchElementException("alert.notFoundService");
        }
        return service;
    }

    public Service addService(DtoService dtoService) throws DbConnectionException, IncorrectFormatException {

        validator.validateEmptyString(dtoService.getName(), "alert.emptyNameField");
        validator.validateEmptyString(dtoService.getDescription(), "alert.emptyDescriptionField");

        Service service = MapperService.toService(dtoService);

        try {
            if (servicesRepo.isServiceNameExist(dtoService.getName())) throw new IncorrectFormatException("alert.nameAlreadyExist");
            int serviceId = servicesRepo.addService(service);
            service.setId(serviceId);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return service;
    }

    @Override
    public void deleteService(int id) throws DbConnectionException, RelatedRecordsExistException {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("whereColumn","services_id");
        parameters.put("whereValue",String.valueOf(id));

        try {
            int tariffsCount = tariffsRepo.getTariffsCount(parameters);
            if (tariffsCount>0) throw new RelatedRecordsExistException("alert.relatedRecordsExist");

            servicesRepo.deleteService(id);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    public Service updateService(DtoService dtoService) throws DbConnectionException, IncorrectFormatException {

        validator.validateEmptyString(dtoService.getName(), "Name must be not empty");
        validator.validateEmptyString(dtoService.getDescription(), "Description must be not empty");

        Service service=MapperService.toService(dtoService);
        try {
            if (servicesRepo.isServiceNameExist(dtoService.getName())) throw new IncorrectFormatException("alert.nameAlreadyExist");
            servicesRepo.updateService(service);
            service = servicesRepo.getServiceById(Integer.parseInt(dtoService.getId()));
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return service;
    }
    @Override
    public List<Service> getServicesList(DtoTable dtoTable) throws DbConnectionException {
        List<Service> services;

        try {
            Map<String,String> parameters = dtoTable.buildQueryParameters();
            services = servicesRepo.getServicesList(parameters);

        } catch ( SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return services;
    }

    @Override
    public List<Service> getAllServicesList() throws DbConnectionException {
        List<Service> services;

        try {
            services = servicesRepo.getServicesList(null);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return services;
    }

    @Override
    public Integer getServicesCount(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String,String> parameters = dtoTable.buildQueryParameters();
            return servicesRepo.getServicesCount(parameters);

        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }
    }


}

