package service.impl;

import dao.IServiceDao;
import dao.ITariffDao;
import dao.impl.ServiceDaoImpl;
import dao.impl.TariffDaoImpl;
import dto.DtoService;
import entity.Service;
import enums.SortOrder;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.RelatedRecordsExistException;
import service.IServicesService;
import service.IValidatorService;
import java.util.List;
import java.util.NoSuchElementException;

public class ServicesService implements IServicesService {

    private static final IServiceDao servicesDao = new ServiceDaoImpl();
    private static final ITariffDao tariffsDao = new TariffDaoImpl();
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

        Service service = new Service(0,
                dtoService.getName(), dtoService.getDescription());
        try {
            int serviceId = servicesDao.addService(service);
            service.setId(serviceId);
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return service;
    }

    @Override
    public void deleteService(int id) throws DbConnectionException, RelatedRecordsExistException {
        int tariffsCount = tariffsDao.getFindTariffsCount(2, String.valueOf(id));

        if (tariffsCount>0) throw new RelatedRecordsExistException("alert.relatedRecordsExist");

        try {
            servicesDao.deleteService(id);
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    public Service updateService(DtoService dtoService) throws DbConnectionException, IncorrectFormatException {

        validator.validateEmptyString(dtoService.getName(), "Name must be not empty");
        validator.validateEmptyString(dtoService.getDescription(), "Description must be not empty");

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

    @Override
    public List<Service> getAllServicesList() throws DbConnectionException {
        List<Service> services;

        try {
            services = servicesDao.getServicesList();

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

}

