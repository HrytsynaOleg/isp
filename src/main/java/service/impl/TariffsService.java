package service.impl;

import dao.IPaymentDao;
import dao.IServiceDao;
import dao.ITariffDao;
import dao.IUserTariffDao;
import dao.impl.PaymentDao;
import dao.impl.ServiceDaoImpl;
import dao.impl.TariffDaoImpl;
import dao.impl.UserTariffDaoImpl;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import entity.UserTariff;
import enums.*;
import exceptions.*;
import service.ITariffsService;
import service.IValidatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class TariffsService implements ITariffsService {

    private static final ITariffDao tariffsDao = new TariffDaoImpl();
    private static final IUserTariffDao userTariffsDao = new UserTariffDaoImpl();
    private static final IServiceDao servicesDao = new ServiceDaoImpl();
    private static final IPaymentDao paymentsDao = new PaymentDao();
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

        validator.validateEmptyString(dtoTariff.getName(), "Name must be not empty");
        validator.validateEmptyString(dtoTariff.getDescription(), "Description must be not empty");

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
            tariff = tariffsDao.getTariffById(Integer.parseInt(dtoTariff.getId()));
            List<UserTariff> subscribers = userTariffsDao.getTariffSubscribersList(tariff.getId());
            for (UserTariff userTariff : subscribers) {
                unsubscribeTariff(userTariff.getTariff().getId(), userTariff.getUser().getId());
            }
            tariffsDao.updateTariff(dtoTariff);
            for (UserTariff userTariff : subscribers) {
                if (tariff.getStatus().equals(TariffStatus.ACTIVE)) {
                    try {
                        subscribeTariff(userTariff.getTariff().getId(), userTariff.getUser().getId());
                    } catch (TariffAlreadySubscribedException | NotEnoughBalanceException ignored) {
                    }
                }
            }

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariff;
    }

    @Override
    public void deleteTariff(int tariffId) throws DbConnectionException, RelatedRecordsExistException {

        List<UserTariff> tariffSubscribersList = userTariffsDao.getTariffSubscribersList(tariffId);
        if (tariffSubscribersList.size() > 0) throw new RelatedRecordsExistException("alert.tariffSubscribersExist");

        tariffsDao.deleteTariff(tariffId);
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
    public List<Tariff> getActiveTariffsUserList(int userId) throws DbConnectionException {
        List<Tariff> tariffs;
        try {
            tariffs = userTariffsDao.getUserActiveTariffList(userId);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return tariffs;
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws DbConnectionException {
        List<Tariff> tariffs;
        try {
            tariffs = tariffsDao.getPriceTariffsList();

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
            tariffsDao.setTariffStatus(tariff, status);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    @Override
    public void subscribeTariff(int tariffId, int userId) throws DbConnectionException, TariffAlreadySubscribedException, NotEnoughBalanceException {
        if (userTariffsDao.userTariffCount(tariffId, userId) > 0)
            throw new TariffAlreadySubscribedException("alert.tariffAlreadySubscribed");
        Tariff tariff = tariffsDao.getTariffById(tariffId);
        if (!tariff.getStatus().equals(TariffStatus.ACTIVE))
            throw new TariffAlreadySubscribedException("alert.tariffForbidden");
        int serviceId = tariff.getService().getId();
        List<Tariff> tariffListByService = userTariffsDao.getUserTariffListByService(serviceId, userId);
        if (tariffListByService.size() > 0) {
            for (Tariff item : tariffListByService) {
                int userTariffId = userTariffsDao.getUserTariffId(item.getId(), userId);
                userTariffsDao.deleteUserTariff(userTariffId);
            }
        }
        int userTariffId = userTariffsDao.addUserTariff(tariffId, userId);
        LocalDate endDate = userTariffsDao.getUserTariffEndDate(userTariffId);
        String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", tariff.getService().getName(),
                tariff.getName(), endDate);
        if (!paymentsDao.addWithdrawPayment(userId, tariff.getPrice(), userTariffWithdrawDescription)) {
            userTariffsDao.setUserTariffStatus(userTariffId, SubscribeStatus.PAUSED);
            throw new NotEnoughBalanceException("alert.notEnoughBalance");
        }
    }

    @Override
    public void unsubscribeTariff(int tariff, int userId) throws DbConnectionException {
        Tariff tariffObj = tariffsDao.getTariffById(tariff);
        Integer userTariffId = userTariffsDao.getUserTariffId(tariff, userId);
        if (userTariffsDao.getUserTariffStatus(userTariffId).equals(SubscribeStatus.ACTIVE)) {
            LocalDateTime endDate = userTariffsDao.getUserTariffEndDate(userTariffId).atStartOfDay();
            BigDecimal moneyBackPeriod = BigDecimal.valueOf(Duration.between(LocalDate.now().atStartOfDay(), endDate).toDays() - 1);
            BigDecimal priceForDay = tariffObj.getPrice().divide(BigDecimal.valueOf(tariffObj.getPeriod().getDivider()), RoundingMode.HALF_UP);
            BigDecimal returnValue = moneyBackPeriod.compareTo(new BigDecimal(0)) > 0 ? priceForDay.multiply(moneyBackPeriod) : new BigDecimal(0);
            if (returnValue.compareTo(new BigDecimal(0)) > 0)
                paymentsDao.addIncomingPayment(userId, returnValue, IncomingPaymentType.MONEYBACK.getName());
        }
        userTariffsDao.deleteUserTariff(userTariffId);
    }

    @Override
    public void setTariffPrice(int tariff, String price) throws DbConnectionException {
        try {
            tariffsDao.setTariffPrice(tariff, price);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }


}

