package service.impl;

import dao.impl.ServiceDao;
import repository.*;
import repository.impl.*;
import dto.DtoTable;
import dto.DtoTariff;
import entity.*;
import enums.*;
import exceptions.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.ITariffsService;
import service.IValidatorService;
import service.MapperService;
import settings.Regex;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TariffsService implements ITariffsService {

    private static final ITariffRepository tariffsDao = new TariffRepositoryImpl();
    private static final IUserTariffRepository userTariffsDao = new UserTariffRepositoryImpl();
    private static final IUserRepository userDao = new UserRepositoryImpl();
    private static final IServicesRepository servicesDao = new ServicesRepositoryImpl(new ServiceDao());
    private static final IPaymentRepository paymentsDao = new PaymentRepository();
    private static final IValidatorService validator = new ValidatorService();
    private static final Logger logger = LogManager.getLogger(TariffsService.class);

    @Override
    public Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException {
        Tariff tariff;
        try {
            tariff = tariffsDao.getTariffById(id);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        } catch (NoSuchElementException e) {
            logger.error(e);
            throw new NoSuchElementException("alert.notFoundTariff");
        }
        return tariff;
    }

    @Override
    public Tariff addTariff(DtoTariff dtoTariff) throws DbConnectionException, IncorrectFormatException {

        Tariff newTariff;

        validator.validateEmptyString(dtoTariff.getName(), "alert.emptyNameField");
        validator.validateEmptyString(dtoTariff.getDescription(), "alert.emptyDescriptionField");
        validator.validateEmptyString(dtoTariff.getPrice(), "alert.emptyPriceField");
        validator.validateString(dtoTariff.getPrice(), Regex.DECIMAL_NUMBER_REGEX, "alert.incorrectPriceField");
        try {
            if (tariffsDao.isTariffNameExist(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            Tariff tariff = MapperService.toTariff(dtoTariff);

            newTariff = tariffsDao.addTariff(tariff);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return newTariff;
    }

    @Override
    public Tariff updateTariff(DtoTariff dtoTariff) throws DbConnectionException, IncorrectFormatException {

        validator.validateEmptyString(dtoTariff.getName(), "alert.emptyNameField");
        validator.validateEmptyString(dtoTariff.getDescription(), "alert.emptyDescriptionField");
        validator.validateEmptyString(dtoTariff.getPrice(), "alert.emptyPriceField");
        validator.validateString(dtoTariff.getPrice(), Regex.DECIMAL_NUMBER_REGEX, "alert.incorrectPriceField");

        Tariff newTariff;
        List<UserTariff> subscribers;
        Tariff oldTariff;
        try {
            oldTariff = tariffsDao.getTariffById(Integer.parseInt(dtoTariff.getId()));

            if (tariffsDao.isTariffNameExist(dtoTariff.getName()) && !oldTariff.getName().equals(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");

            subscribers = userTariffsDao.getTariffSubscribersList(oldTariff.getId());

            for (UserTariff userTariff : subscribers) {
                unsubscribeTariff(userTariff.getTariff().getId(), userTariff.getUser().getId());
            }
            newTariff = MapperService.toTariff(dtoTariff);
            tariffsDao.updateTariff(newTariff);

            for (UserTariff userTariff : subscribers) {
                if (newTariff.getStatus().equals(TariffStatus.ACTIVE)) {
                    try {
                        subscribeTariff(userTariff.getTariff().getId(), userTariff.getUser().getId());
                    } catch (TariffAlreadySubscribedException | NotEnoughBalanceException ignored) {
                    }
                }
            }

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return newTariff;
    }

    @Override
    public void deleteTariff(int tariffId) throws DbConnectionException, RelatedRecordsExistException {
        try {
            List<UserTariff> tariffSubscribersList = userTariffsDao.getTariffSubscribersList(tariffId);

            if (tariffSubscribersList.size() > 0)
                throw new RelatedRecordsExistException("alert.tariffSubscribersExist");
            tariffsDao.deleteTariff(tariffId);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Tariff> getTariffsList(DtoTable dtoTable) throws DbConnectionException {
        List<Tariff> tariffs;

        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            tariffs = tariffsDao.getTariffsList(parameters);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return tariffs;
    }

    @Override
    public List<Tariff> getTariffsUserList(int userId, DtoTable dtoTable) throws DbConnectionException {
        List<Tariff> tariffs;
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            tariffs = tariffsDao.getTariffsList(parameters);
            for (Tariff tariff : tariffs) {
                UserTariff userTariff = userTariffsDao.getUserTariff(tariff.getId(), userId);
                SubscribeStatus userTariffStatus = userTariff == null ? SubscribeStatus.UNSUBSCRIBE : userTariff.getSubscribeStatus();
                tariff.setSubscribe(userTariffStatus);
            }

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return tariffs;
    }

    @Override
    public List<UserTariff> getActiveTariffsUserList(int userId, DtoTable dtoTable) throws DbConnectionException {
        List<UserTariff> userTariffs;
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            userTariffs = userTariffsDao.getUserActiveTariffList(userId, parameters);
        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return userTariffs;
    }

    @Override
    public int getActiveTariffsUserCount(int userId) throws DbConnectionException {
        int recordsCount;
        try {
            recordsCount = userTariffsDao.getUserActiveTariffCount(userId);
        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return recordsCount;
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws DbConnectionException {
        List<Tariff> tariffs;
        try {
            tariffs = tariffsDao.getPriceTariffsList();

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return tariffs;
    }

    @Override
    public Integer getTariffsCount(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return tariffsDao.getTariffsCount(parameters);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void setTariffStatus(int tariff, String status) throws DbConnectionException {
//        try {
////            tariffsDao.setTariffStatus(tariff, status);
//            logger.info(String.format("Tariff status changed to %s", status));
//
//        } catch (DbConnectionException e) {
//            logger.error(e);
//            throw new DbConnectionException("alert.databaseError");
//        }
    }

    @Override
    public void subscribeTariff(int tariffId, int userId) throws DbConnectionException, TariffAlreadySubscribedException, NotEnoughBalanceException {
        try {

            if (userTariffsDao.userTariffCount(tariffId, userId) > 0)
                throw new TariffAlreadySubscribedException("alert.tariffAlreadySubscribed");

            Tariff tariff = tariffsDao.getTariffById(tariffId);
            User user = userDao.getUserById(userId);

            if (!tariff.getStatus().equals(TariffStatus.ACTIVE))
                throw new TariffAlreadySubscribedException("alert.tariffForbidden");

            int serviceId = tariff.getService().getId();

            List<UserTariff> userTariffListByService = userTariffsDao.getUserTariffListByService(serviceId, userId);

            if (userTariffListByService.size() > 0) {
                UserTariff userTariffByService = userTariffListByService.stream().findFirst().get();

                unsubscribeTariff(userTariffByService.getTariff().getId(),userId);

            }
            UserTariff userTariff = userTariffsDao.addUserTariff(tariffId, userId);
            logger.info(String.format("User id - %s subscribed on tariff %s %s", userId,
                    userTariff.getTariff().getService().getName(), userTariff.getTariff().getName()));

            LocalDate endDate = userTariff.getDateEnd();
            String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", tariff.getService().getName(),
                    tariff.getName(), endDate);
            Payment withdraw = new Payment(0, user, userTariff.getTariff().getPrice(), new Date(), PaymentType.OUT, userTariffWithdrawDescription);
            try {
                paymentsDao.addPayment(withdraw);
            } catch (NotEnoughBalanceException e) {
                userTariffsDao.setUserTariffStatus(userTariff.getId(), SubscribeStatus.PAUSED);
                throw new NotEnoughBalanceException("alert.notEnoughBalance");
            }

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void unsubscribeTariff(int tariff, int userId) throws DbConnectionException {
        try {
            UserTariff userTariff = userTariffsDao.getUserTariff(tariff, userId);
            User user = userDao.getUserById(userId);
            if (userTariff.getSubscribeStatus().equals(SubscribeStatus.ACTIVE)) {
                BigDecimal returnValue = userTariff.calcMoneyBackValue();
                if (returnValue.compareTo(BigDecimal.ZERO) > 0) {
                    Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                    paymentsDao.addPayment(moneyBackPayment);
                }
            }
            userTariffsDao.deleteUserTariff(userTariff.getId());
            logger.info(String.format("User id - %s unsubscribed on tariff %s %s", userId,
                    userTariff.getTariff().getService().getName(), userTariff.getTariff().getName()));


        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        } catch (NotEnoughBalanceException ignored) {

        }
    }

    @Override
    public void setTariffPrice(int tariff, String price) throws DbConnectionException {
//        try {
//            tariffsDao.setTariffPrice(tariff, price);
//
//        } catch (DbConnectionException e) {
//            logger.error(e);
//            throw new DbConnectionException("alert.databaseError");
//        }
    }

    @Override
    public BigDecimal calcMonthTotalUserExpenses(int userId) throws DbConnectionException {
        try {
            List<UserTariff> tariffs = userTariffsDao.getUserActiveTariffList(userId, null);

            return tariffs.stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.ACTIVE))
                    .map(e -> e.getTariff().getPeriod().calcMonthTotal(e.getTariff().getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public BigDecimal calcMonthTotalProfit() throws DbConnectionException {
        try {
            List<UserTariff> tariffs = userTariffsDao.getAllActiveTariffList();

            return tariffs.stream()
                    .map(e -> e.getTariff().getPeriod().calcMonthTotal(e.getTariff().getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

}


