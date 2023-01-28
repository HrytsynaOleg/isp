package service.impl;

import dao.*;
import dao.impl.*;
import dto.DtoTable;
import dto.DtoTariff;
import entity.*;
import enums.*;
import exceptions.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.ITariffsService;
import service.IValidatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TariffsService implements ITariffsService {

    private static final ITariffDao tariffsDao = new TariffDaoImpl();
    private static final IUserTariffDao userTariffsDao = new UserTariffDaoImpl();
    private static final IUserDao userDao = new UserDaoImpl();
    private static final IServiceDao servicesDao = new ServiceDaoImpl();
    private static final IPaymentDao paymentsDao = new PaymentDao();
    private static final IValidatorService validator = new ValidatorService();
    private static final Logger logger = LogManager.getLogger(TariffsService.class);

    @Override
    public Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException {
        Tariff tariff;
        try {
            tariff = tariffsDao.getTariffById(id);

        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
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
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
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
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return tariff;
    }

    @Override
    public void deleteTariff(int tariffId) throws DbConnectionException, RelatedRecordsExistException {
        try {
            List<UserTariff> tariffSubscribersList = userTariffsDao.getTariffSubscribersList(tariffId);
            if (tariffSubscribersList.size() > 0)
                throw new RelatedRecordsExistException("alert.tariffSubscribersExist");
            tariffsDao.deleteTariff(tariffId);

        } catch (DbConnectionException e) {
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

        } catch (DbConnectionException e) {
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

        } catch (DbConnectionException e) {
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

        } catch (DbConnectionException e) {
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

        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void setTariffStatus(int tariff, String status) throws DbConnectionException {
        try {
            tariffsDao.setTariffStatus(tariff, status);
            logger.info(String.format("Tariff status changed to %s", status));

        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
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
                BigDecimal returnValue = calcMoneyBackValue(userTariffByService);
                if (returnValue.compareTo(BigDecimal.ZERO) > 0) {
                    Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                    paymentsDao.addPayment(moneyBackPayment);
                }
                userTariffsDao.deleteUserTariff(userTariffByService.getId());
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


        } catch (DbConnectionException e) {
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
                BigDecimal returnValue = calcMoneyBackValue(userTariff);
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
        try {
            tariffsDao.setTariffPrice(tariff, price);

        } catch (DbConnectionException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
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

    private BigDecimal calcMoneyBackValue(UserTariff userTariff) {
        Tariff tariff = userTariff.getTariff();
        long duration = Duration.between(LocalDate.now().atStartOfDay(), userTariff.getDateEnd().atStartOfDay()).toDays() - 1;
        BigDecimal moneyBackPeriod = BigDecimal.valueOf(duration);
        BigDecimal priceForDay = tariff.getPrice().divide(BigDecimal.valueOf(tariff.getPeriod().getDivider()), RoundingMode.HALF_UP);
        return moneyBackPeriod.compareTo(BigDecimal.ZERO) > 0 ? priceForDay.multiply(moneyBackPeriod) : BigDecimal.ZERO;
    }

}


