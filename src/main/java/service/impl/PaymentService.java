package service.impl;

import repository.IPaymentRepository;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import dto.DtoTable;
import entity.Payment;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentService implements IPaymentService {
    private static final Logger logger = LogManager.getLogger(PaymentService.class);
    private final IPaymentRepository paymentRepo;
    private final IUserTariffRepository userTariffRepo;
    private final IUserRepository userRepo;

    public PaymentService(IPaymentRepository paymentRepo, IUserTariffRepository userTariffRepo, IUserRepository userRepo) {
        this.paymentRepo = paymentRepo;
        this.userTariffRepo = userTariffRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void addIncomingPayment(int userId, BigDecimal value) throws DbConnectionException {
        try {
            User user = userRepo.getUserById(userId);
            Payment payment = new Payment(0, user, value, new Date(), PaymentType.IN, IncomingPaymentType.PAYMENT.getName());
            List<UserTariff> pausedTariffs = userTariffRepo.getUserActiveTariffList(userId, new HashMap<>()).stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.PAUSED))
                    .toList();
            paymentRepo.addPayment(payment, pausedTariffs);

        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }

    }

    @Override
    public void extendExpiredUserTariffs() throws DbConnectionException {
        try {
            List<UserTariff> tariffs = userTariffRepo.getAllExpiredUserActiveTariffList();
            for (UserTariff userTariff : tariffs) {
                String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", userTariff.getTariff().getService().getName(),
                        userTariff.getTariff().getName(), userTariff.getDateEnd());
                User user = userRepo.getUserById(userTariff.getUser().getId());
                Payment withdraw = new Payment(0, user, userTariff.getTariff().getPrice(), new Date(), PaymentType.OUT, userTariffWithdrawDescription);
                try {
                    paymentRepo.addWithdraw(withdraw, userTariff);
                } catch (NotEnoughBalanceException e) {
                    userTariff.setSubscribeStatus(SubscribeStatus.PAUSED);
                    userTariffRepo.updateUserTariff(userTariff);
                }
            }
        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public List<Payment> getPaymentsListByUserId(DtoTable dtoTable, int userId, PaymentType type) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return paymentRepo.getPaymentsListByUser(userId, type, parameters);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public List<Payment> getPaymentsListAllUsers(DtoTable dtoTable, PaymentType type) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return paymentRepo.getPaymentsListAllUsers(type, parameters);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public Integer getPaymentsCountByUserId(int userId, PaymentType type) throws DbConnectionException {
        try {
            return paymentRepo.getPaymentsCountByUserId(userId, type);
        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public Integer getPaymentsCountAllUsers(PaymentType type) throws DbConnectionException {
        try {
            return paymentRepo.getPaymentsCountAllUsers(type);
        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }
    }

}
