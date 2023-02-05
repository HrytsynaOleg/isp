package service.impl;

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

    private final ITariffRepository tariffsRepo;
    private final IUserTariffRepository userTariffRepo;
    private final IUserRepository userRepo;
    private static final IPaymentRepository paymentsDao = new PaymentRepository();
    private static final IValidatorService validator = new ValidatorService();
    private static final Logger logger = LogManager.getLogger(TariffsService.class);

    public TariffsService(ITariffRepository tariffsRepo, IUserTariffRepository userTariffRepo, IUserRepository userRepo) {
        this.tariffsRepo = tariffsRepo;
        this.userTariffRepo = userTariffRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException {
        Tariff tariff;
        try {
            tariff = tariffsRepo.getTariffById(id);
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
            if (tariffsRepo.isTariffNameExist(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            Tariff tariff = MapperService.toTariff(dtoTariff);
            newTariff = tariffsRepo.addTariff(tariff);
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
            oldTariff = tariffsRepo.getTariffById(Integer.parseInt(dtoTariff.getId()));

            if (tariffsRepo.isTariffNameExist(dtoTariff.getName()) && !oldTariff.getName().equals(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");

            subscribers = userTariffRepo.getTariffSubscribersList(oldTariff.getId());

            for (UserTariff userTariff : subscribers) {
                unsubscribeTariff(userTariff.getTariff().getId(), userTariff.getUser().getId());
            }
            newTariff = MapperService.toTariff(dtoTariff);
            tariffsRepo.updateTariff(newTariff);

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
            List<UserTariff> tariffSubscribersList = userTariffRepo.getTariffSubscribersList(tariffId);

            if (tariffSubscribersList.size() > 0)
                throw new RelatedRecordsExistException("alert.tariffSubscribersExist");
            tariffsRepo.deleteTariff(tariffId);

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
            tariffs = tariffsRepo.getTariffsList(parameters);

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
            tariffs = tariffsRepo.getTariffsList(parameters);
            for (Tariff tariff : tariffs) {
                UserTariff userTariff = userTariffRepo.getUserTariff(tariff.getId(), userId);
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
            userTariffs = userTariffRepo.getUserActiveTariffList(userId, parameters);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return userTariffs;
    }

    @Override
    public int getActiveTariffsUserCount(int userId) throws DbConnectionException {
        int recordsCount;
        try {
            recordsCount = userTariffRepo.getUserActiveTariffCount(userId);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
        return recordsCount;
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws DbConnectionException {
        List<Tariff> tariffs;
        try {
            tariffs = tariffsRepo.getPriceTariffsList();

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
            return tariffsRepo.getTariffsCount(parameters);

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

            if (userTariffRepo.userTariffCount(tariffId, userId) > 0)
                throw new TariffAlreadySubscribedException("alert.tariffAlreadySubscribed");

            Tariff tariff = tariffsRepo.getTariffById(tariffId);
            User user = userRepo.getUserById(userId);
            LocalDate dateEnd = tariff.getPeriod().getNexDate(LocalDate.now());
            UserTariff userTariff = new UserTariff(0, user, tariff,
                    SubscribeStatus.ACTIVE, LocalDate.now(), dateEnd);

            if (!tariff.getStatus().equals(TariffStatus.ACTIVE))
                throw new TariffAlreadySubscribedException("alert.tariffForbidden");

            int serviceId = tariff.getService().getId();

            List<UserTariff> userTariffListByService = userTariffRepo.getUserTariffListByService(serviceId, userId);

            if (userTariffListByService.size() > 0) {
                UserTariff userTariffByService = userTariffListByService.stream().findFirst().get();

                unsubscribeTariff(userTariffByService.getTariff().getId(),userId);

            }
            userTariff = userTariffRepo.addUserTariff(userTariff);
            logger.info(String.format("User id - %s subscribed on tariff %s %s", userId,
                    userTariff.getTariff().getService().getName(), userTariff.getTariff().getName()));

            LocalDate endDate = userTariff.getDateEnd();
            String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", tariff.getService().getName(),
                    tariff.getName(), endDate);
            Payment withdraw = new Payment(0, user, userTariff.getTariff().getPrice(), new Date(), PaymentType.OUT, userTariffWithdrawDescription);
            try {
                paymentsDao.addPayment(withdraw);
            } catch (NotEnoughBalanceException e) {
                userTariff.setSubscribeStatus(SubscribeStatus.PAUSED);
                userTariffRepo.updateUserTariff(userTariff);
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
            UserTariff userTariff = userTariffRepo.getUserTariff(tariff, userId);
            User user = userRepo.getUserById(userId);
            if (userTariff.getSubscribeStatus().equals(SubscribeStatus.ACTIVE)) {
                BigDecimal returnValue = userTariff.calcMoneyBackValue();
                if (returnValue.compareTo(BigDecimal.ZERO) > 0) {
                    Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                    paymentsDao.addPayment(moneyBackPayment);
                }
            }
            userTariffRepo.deleteUserTariff(userTariff.getId());
            logger.info(String.format("User id - %s unsubscribed on tariff %s %s", userId,
                    userTariff.getTariff().getService().getName(), userTariff.getTariff().getName()));


        } catch (SQLException e) {
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

            List<UserTariff> tariffs = userTariffRepo.getUserActiveTariffList(userId, null);

            return tariffs.stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.ACTIVE))
                    .map(e -> e.getTariff().getPeriod().calcMonthTotal(e.getTariff().getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public BigDecimal calcMonthTotalProfit() throws DbConnectionException {
        try {
            List<UserTariff> tariffs = userTariffRepo.getAllActiveTariffList();

            return tariffs.stream()
                    .map(e -> e.getTariff().getPeriod().calcMonthTotal(e.getTariff().getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch ( SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

}


