package service.impl;

import dao.IServiceDao;
import dao.impl.ServiceDaoImpl;
import dto.DtoService;
import dto.DtoUser;
import entity.Service;
import entity.User;
import entity.builder.UserBuilder;
import enums.SortOrder;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import service.IServicesService;
import service.IValidatorService;
import settings.Regex;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class ServicesService implements IServicesService {

    private static final IServiceDao servicesDao = new ServiceDaoImpl();
    private static final IValidatorService validator = new ValidatorService();

    public Service getService(int id) throws DbConnectionException, NoSuchElementException {
        Service service;
        try {
            service = servicesDao.getServiceById(id);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }
        return service;
    }

    public Service addService(DtoService dtoService) throws DbConnectionException, IncorrectFormatException {
        validator.validateEmptyString(dtoService.getName(), "Name must be not empty");
        validator.validateEmptyString(dtoService.getDescription(), "Description must be not empty");

        Service service = mapDtoToService(dtoService);
        try {
            int serviceId = servicesDao.addService(service);
            service.setId(serviceId);
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return service;
    }

    public Service updateService(DtoService dtoService) throws DbConnectionException {

        Service service;
        try {
            servicesDao.updateService(dtoService);
            service = servicesDao.getServiceById(Integer.parseInt(dtoService.getId()));
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return service;
    }

    public List<Service> getServicesList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder) throws DbConnectionException {
        List<Service> services;

        try {
            services = servicesDao.getServicesList(limit, total, sortColumn, sortOrder.toString());

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return services;
    }

    public List<Service> getFindServicesList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria) throws DbConnectionException {
        List<Service> services;

        try {
            services = servicesDao.getFindServicesList(limit, total, sortColumn, sortOrder.toString(), field, criteria);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return services;
    }

    public Integer getServicesCount() throws DbConnectionException {
        try {
            return servicesDao.getServicesCount();

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    public Integer getFindServicesCount(int field, String criteria) throws DbConnectionException {
        try {
            return servicesDao.getFindServicesCount(field, criteria);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    private Service mapDtoToService(DtoService dtoService) {

        return new Service(Integer.parseInt(dtoService.getId()),
                dtoService.getName(), dtoService.getDescription());
    }

}

