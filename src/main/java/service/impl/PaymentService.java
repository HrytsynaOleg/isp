package service.impl;

import dependecies.DependencyManager;
import repository.IPaymentRepository;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import repository.impl.PaymentRepository;
import repository.impl.UserTariffRepositoryImpl;
import dto.DtoTable;
import entity.Payment;
import entity.Tariff;
import entity.User;
import entity.UserTariff;
import enums.IncomingPaymentType;
import enums.PaymentType;
import enums.SubscribeStatus;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IPaymentService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentService implements IPaymentService {
    private static final Logger logger = LogManager.getLogger(PaymentService.class);
    private static final IPaymentRepository paymentRepo = new PaymentRepository();
    private static final IUserTariffRepository userTariffRepo =DependencyManager.userTariffRepo;
    private static final IUserRepository userRepo = DependencyManager.userRepo;

    @Override
    public void addIncomingPayment(int userId, BigDecimal value) throws DbConnectionException, NotEnoughBalanceException {

        User user ;
        try {
            user = userRepo.getUserById(userId);
        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }

        Payment payment = new Payment(0, user, value, new Date(), PaymentType.IN, IncomingPaymentType.PAYMENT.getName());

        paymentRepo.addPayment(payment);

        List<Tariff> tariffs = null;
        try {
            tariffs = userTariffRepo.getUserActiveTariffList(userId,null).stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.PAUSED))
                    .map(UserTariff::getTariff)
                    .toList();
        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }

        for (Tariff tariff: tariffs) {
            LocalDate date = tariff.getPeriod().getNexDate(LocalDate.now());
            String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", tariff.getService().getName(),
                    tariff.getName(), date);

            Payment withdraw = new Payment(0, user, tariff.getPrice(), new Date(), PaymentType.OUT, userTariffWithdrawDescription);

            try {
                paymentRepo.addPayment(withdraw);
                UserTariff userTariff = userTariffRepo.getUserTariff(tariff.getId(),userId);
                userTariff.setSubscribeStatus(SubscribeStatus.ACTIVE);
                userTariff.setDateEnd(date);
                userTariffRepo.updateUserTariff(userTariff);

            } catch (NotEnoughBalanceException e) {
                throw new NotEnoughBalanceException("alert.notEnoughBalance");
            } catch (SQLException e) {
                throw new DbConnectionException("alert.databaseError");
            }

        }

    }
    @Override
    public void extendExpiredUserTariffs() throws DbConnectionException, NotEnoughBalanceException {
        try {
            List<UserTariff> tariffs = userTariffRepo.getAllExpiredUserActiveTariffList();

            for (UserTariff userTariff : tariffs) {
                String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", userTariff.getTariff().getService().getName(),
                        userTariff.getTariff().getName(), userTariff.getDateEnd());
                User user;
                try {
                    user = userRepo.getUserById(userTariff.getUser().getId());
                } catch (SQLException e) {
                    throw new DbConnectionException("alert.databaseError");
                }
                Payment withdraw = new Payment(0, user, userTariff.getTariff().getPrice(), new Date(), PaymentType.OUT, userTariffWithdrawDescription);
                try {
                    paymentRepo.addPayment(withdraw);
                    userTariff.setSubscribeStatus(SubscribeStatus.ACTIVE);
                    LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
                    userTariff.setDateEnd(date);
                    userTariffRepo.updateUserTariff(userTariff);
                } catch (NotEnoughBalanceException e) {
                    throw new NotEnoughBalanceException("alert.notEnoughBalance");
                }
            }
        }
        catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
        public List<Payment> getPaymentsListByUserId (DtoTable dtoTable, int userId, PaymentType type) throws DbConnectionException {
            List<Payment> payments;

            try {
                Map<String,String> parameters = dtoTable.buildQueryParameters();
                payments = paymentRepo.getPaymentsListByUser(userId, type, parameters);

            } catch (DbConnectionException e) {
                logger.error(e.getMessage());
                throw new DbConnectionException(e);
            }
            return payments;
        }

        @Override
        public Integer getPaymentsCountByUserId ( int userId, PaymentType type) throws DbConnectionException {
            return paymentRepo.getPaymentsCountByUserId(userId, type);
        }

    }
