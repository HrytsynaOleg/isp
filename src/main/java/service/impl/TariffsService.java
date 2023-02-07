package service.impl;

import repository.*;
import dto.DtoTable;
import dto.DtoTariff;
import entity.*;
import enums.*;
import exceptions.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.ITariffsService;
import service.MapperService;
import service.ValidatorService;
import settings.Regex;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TariffsService implements ITariffsService {

    private final ITariffRepository tariffsRepo;
    private final IUserTariffRepository userTariffRepo;
    private final IUserRepository userRepo;
    private final IPaymentRepository paymentsRepo;
    private static final Logger logger = LogManager.getLogger(TariffsService.class);

    public TariffsService(ITariffRepository tariffsRepo, IUserTariffRepository userTariffRepo, IUserRepository userRepo, IPaymentRepository paymentsRepo) {
        this.tariffsRepo = tariffsRepo;
        this.userTariffRepo = userTariffRepo;
        this.userRepo = userRepo;
        this.paymentsRepo = paymentsRepo;
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

        ValidatorService.validateEmptyString(dtoTariff.getName(), "alert.emptyNameField");
        ValidatorService.validateEmptyString(dtoTariff.getDescription(), "alert.emptyDescriptionField");
        ValidatorService.validateEmptyString(dtoTariff.getPrice(), "alert.emptyPriceField");
        ValidatorService.validateString(dtoTariff.getPrice(), Regex.DECIMAL_NUMBER_REGEX, "alert.incorrectPriceField");

        try {
            Tariff newTariff;
            if (tariffsRepo.isTariffNameExist(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            Tariff tariff = MapperService.toTariff(dtoTariff);
            newTariff = tariffsRepo.addTariff(tariff);
            return newTariff;
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }

    }

    @Override
    public Tariff updateTariff(DtoTariff dtoTariff) throws DbConnectionException, IncorrectFormatException {

        ValidatorService.validateEmptyString(dtoTariff.getName(), "alert.emptyNameField");
        ValidatorService.validateEmptyString(dtoTariff.getDescription(), "alert.emptyDescriptionField");
        ValidatorService.validateEmptyString(dtoTariff.getPrice(), "alert.emptyPriceField");
        ValidatorService.validateString(dtoTariff.getPrice(), Regex.DECIMAL_NUMBER_REGEX, "alert.incorrectPriceField");
        try {
            Tariff newTariff = MapperService.toTariff(dtoTariff);
            Tariff oldTariff = tariffsRepo.getTariffById(Integer.parseInt(dtoTariff.getId()));
            List<UserTariff> subscribers = userTariffRepo.getTariffSubscribersList(oldTariff.getId());
            if (tariffsRepo.isTariffNameExist(dtoTariff.getName()) && !oldTariff.getName().equals(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            tariffsRepo.updateTariff(newTariff,oldTariff,subscribers);
            return newTariff;
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
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
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return tariffsRepo.getTariffsList(parameters);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Tariff> getTariffsUserList(int userId, DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            List<Tariff> tariffs = tariffsRepo.getTariffsList(parameters);
            for (Tariff tariff : tariffs) {
                UserTariff userTariff = userTariffRepo.getUserTariff(tariff.getId(), userId);
                SubscribeStatus userTariffStatus = userTariff == null ? SubscribeStatus.UNSUBSCRIBE : userTariff.getSubscribeStatus();
                tariff.setSubscribe(userTariffStatus);
            }
            return tariffs;
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<UserTariff> getActiveTariffsUserList(int userId, DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return userTariffRepo.getUserActiveTariffList(userId, parameters);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public int getActiveTariffsUserCount(int userId) throws DbConnectionException {
        try {
           return userTariffRepo.getUserActiveTariffCount(userId);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws DbConnectionException {
        try {
            return tariffsRepo.getPriceTariffsList();
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
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
            UserTariff newUserTariff = new UserTariff(0, user, tariff, SubscribeStatus.ACTIVE, LocalDate.now(), dateEnd);
            Optional<UserTariff> oldUserTariff = userTariffRepo.getUserTariffListByService(tariff.getService().getId(), userId).stream().findFirst();

            if (!tariff.getStatus().equals(TariffStatus.ACTIVE)) throw new TariffAlreadySubscribedException("alert.tariffForbidden");
            BigDecimal withdrawValue = newUserTariff.getTariff().getPrice();
            if (user.getBalance().compareTo(withdrawValue) < 0) throw new NotEnoughBalanceException();

            tariffsRepo.subscribeTariff(tariff,newUserTariff,oldUserTariff);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }catch (NotEnoughBalanceException e) {
            logger.error(e);
            throw new NotEnoughBalanceException("alert.notEnoughBalance");
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
                    paymentsRepo.addPayment(moneyBackPayment, new ArrayList<>());
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
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

}


