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

    private final ITariffRepository tariffRepo;
    private final IUserTariffRepository userTariffRepo;
    private final IUserRepository userRepo;
    private static final Logger logger = LogManager.getLogger(TariffsService.class);

    public TariffsService(ITariffRepository tariffRepo, IUserTariffRepository userTariffRepo, IUserRepository userRepo) {
        this.tariffRepo = tariffRepo;
        this.userTariffRepo = userTariffRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException {
        try {
            return tariffRepo.getTariffById(id);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        } catch (NoSuchElementException e) {
            logger.error(e);
            throw new NoSuchElementException("alert.notFoundTariff");
        }
    }

    @Override
    public Tariff addTariff(DtoTariff dtoTariff) throws DbConnectionException, IncorrectFormatException {

        ValidatorService.validateEmptyString(dtoTariff.getName(), "alert.emptyNameField");
        ValidatorService.validateEmptyString(dtoTariff.getDescription(), "alert.emptyDescriptionField");
        ValidatorService.validateEmptyString(dtoTariff.getPrice(), "alert.emptyPriceField");
        ValidatorService.validateString(dtoTariff.getPrice(), Regex.DECIMAL_NUMBER_REGEX, "alert.incorrectPriceField");

        try {
            if (tariffRepo.isTariffNameExist(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            Tariff tariff = MapperService.toTariff(dtoTariff);
            return tariffRepo.addTariff(tariff);
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
            Tariff oldTariff = tariffRepo.getTariffById(Integer.parseInt(dtoTariff.getId()));
            List<UserTariff> subscribers = userTariffRepo.getTariffSubscribersList(oldTariff.getId());
            if (tariffRepo.isTariffNameExist(dtoTariff.getName()) && !oldTariff.getName().equals(dtoTariff.getName()))
                throw new IncorrectFormatException("alert.nameAlreadyExist");
            tariffRepo.updateTariff(newTariff,oldTariff,subscribers);
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
            tariffRepo.deleteTariff(tariffId);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Tariff> getTariffsList(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return tariffRepo.getTariffsList(parameters);
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Tariff> getTariffsUserList(int userId, DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            List<Tariff> tariffs = tariffRepo.getTariffsList(parameters);
            for (Tariff tariff : tariffs) {
                Optional<UserTariff> userTariff = userTariffRepo.getUserTariff(tariff.getId(), userId);
                SubscribeStatus userTariffStatus = userTariff.isEmpty() ? SubscribeStatus.UNSUBSCRIBE : userTariff.get().getSubscribeStatus();
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
            return tariffRepo.getPriceTariffsList();
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public Integer getTariffsCount(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return tariffRepo.getTariffsCount(parameters);

        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void setTariffStatus(int tariff, String status) {
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
            Tariff tariff = tariffRepo.getTariffById(tariffId);
            User user = userRepo.getUserById(userId);
            LocalDate dateEnd = tariff.getPeriod().getNexDate(LocalDate.now());
            UserTariff newUserTariff = new UserTariff(0, user, tariff, SubscribeStatus.ACTIVE, LocalDate.now(), dateEnd);
            Optional<UserTariff> oldUserTariff = userTariffRepo.getUserTariffListByService(tariff.getService().getId(), userId).stream().findFirst();

            if (!tariff.getStatus().equals(TariffStatus.ACTIVE)) throw new TariffAlreadySubscribedException("alert.tariffForbidden");
            BigDecimal withdrawValue = newUserTariff.getTariff().getPrice();
            if (user.getBalance().compareTo(withdrawValue) < 0) throw new NotEnoughBalanceException();

            tariffRepo.subscribeTariff(tariff,newUserTariff,oldUserTariff);

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
            Optional<UserTariff> userTariff = userTariffRepo.getUserTariff(tariff, userId);
            if (userTariff.isPresent()) tariffRepo.unsubscribeTariff(userTariff.get());
        } catch (SQLException e) {
            logger.error(e);
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void setTariffPrice(int tariff, String price) {
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

            List<UserTariff> tariffs = userTariffRepo.getUserActiveTariffList(userId, new HashMap<>());

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


