package service.impl;

import dao.IServiceDao;
import dao.ITariffDao;
import dao.impl.ServiceDaoImpl;
import dao.impl.TariffDaoImpl;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.SortOrder;
import enums.TariffStatus;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import service.ITariffsService;
import service.IValidatorService;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public class TariffsService implements ITariffsService {

    private static final ITariffDao tariffsDao = new TariffDaoImpl();
    private static final IServiceDao servicesDao = new ServiceDaoImpl();
    private static final IValidatorService validator = new ValidatorService();

    @Override
    public Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException {
        Tariff tariff;
        try {
            tariff = tariffsDao.getTariffById(id);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }
        return tariff;
    }

    @Override
    public Tariff addTariff(DtoTariff dtoTariff) throws DbConnectionException, IncorrectFormatException {

        validator.validateEmptyString(dtoTariff.getName(),"Name must be not empty");
        validator.validateEmptyString(dtoTariff.getDescription(),"Description must be not empty");

        Service service = servicesDao.getServiceById(Integer.parseInt(dtoTariff.getService()));
        Tariff tariff = new Tariff(0, service, dtoTariff.getName(),
                dtoTariff.getDescription(), new BigDecimal(dtoTariff.getPrice()),
                BillingPeriod.valueOf(dtoTariff.getPeriod()), TariffStatus.valueOf(dtoTariff.getStatus()));

        try {
            int tariffId = tariffsDao.addTariff(tariff);
            tariff.setId(tariffId);
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariff;
    }

    @Override
    public Tariff updateTariff(DtoTariff dtoTariff) throws DbConnectionException {

        Tariff tariff;
        try {
            tariffsDao.updateTariff(dtoTariff);
            tariff = tariffsDao.getTariffById(Integer.parseInt(dtoTariff.getId()));
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariff;
    }

    @Override
    public List<Tariff> getTariffsList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder) throws DbConnectionException {
        List<Tariff> tariffs;

        try {
            tariffs = tariffsDao.getTariffsList(limit, total, sortColumn, sortOrder.toString());

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariffs;
    }

    @Override
    public List<Tariff> getTariffsUserList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int userId) throws DbConnectionException {
        List<Tariff> tariffs;
        try {
            tariffs = tariffsDao.getTariffsUserList(limit, total, sortColumn, sortOrder.toString(), userId);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariffs;
    }

    @Override
    public List<Tariff> getFindTariffsList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria) throws DbConnectionException {
        List<Tariff> tariffs;

        try {
            tariffs = tariffsDao.getFindTariffsList(limit, total, sortColumn, sortOrder.toString(), field, criteria);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariffs;
    }

    @Override
    public List<Tariff> getFindTariffsUserList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria, int userId) throws DbConnectionException {
        List<Tariff> tariffs;

        try {
            tariffs = tariffsDao.getFindTariffsUserList(limit, total, sortColumn, sortOrder.toString(), field, criteria, userId);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariffs;
    }

    @Override
    public Integer getTariffsCount() throws DbConnectionException {
        try {
            return tariffsDao.getTariffsCount();

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    @Override
    public Integer getFindTariffsCount(int field, String criteria) throws DbConnectionException {
        try {
            return tariffsDao.getFindTariffsCount(field, criteria);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    @Override
    public void setTariffStatus(int tariff, String status) throws DbConnectionException {
        try {
            tariffsDao.setTariffStatus(tariff,status);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    @Override
    public void setTariffPrice(int tariff, String price) throws DbConnectionException, IncorrectFormatException {
        try {
            tariffsDao.setTariffPrice(tariff,price);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }


}

