package service.impl;

import dao.IPaymentDao;
import dao.IUserTariffDao;
import dao.impl.PaymentDao;
import dao.impl.UserTariffDaoImpl;
import entity.Payment;
import entity.Tariff;
import entity.UserTariff;
import enums.IncomingPaymentType;
import enums.PaymentType;
import enums.SortOrder;
import enums.SubscribeStatus;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import service.IPaymentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PaymentService implements IPaymentService {
    IPaymentDao paymentDao = new PaymentDao();
    IUserTariffDao userTariffsDao=new UserTariffDaoImpl();

    @Override
    public void addIncomingPayment(int userId, BigDecimal value) throws DbConnectionException, NotEnoughBalanceException {

        paymentDao.addIncomingPayment(userId, value, IncomingPaymentType.PAYMENT.getName());

        List<Tariff> tariffs = userTariffsDao.getUserActiveTariffList(userId).stream()
                .filter(e -> e.getSubscribe().equals(SubscribeStatus.PAUSED)).toList();

        for (Tariff tariff: tariffs) {
            LocalDate date = tariff.getPeriod().getNexDate(LocalDate.now());
            String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", tariff.getService().getName(),
                    tariff.getName(), date);
            if (paymentDao.addWithdrawPayment(userId, tariff.getPrice(), userTariffWithdrawDescription)) {
                int userTariffId = userTariffsDao.getUserTariffId(tariff.getId(),userId);
                userTariffsDao.setUserTariffStatus(userTariffId, SubscribeStatus.ACTIVE);
                userTariffsDao.setUserTariffEndDate(userTariffId,date);

            } else throw new NotEnoughBalanceException("alert.notEnoughBalance");
        }

    }
    @Override
    public void extendExpiredUserTariffs() throws DbConnectionException, NotEnoughBalanceException {
        List<UserTariff> tariffs = userTariffsDao.getExpiredUserActiveTariffList();
        for (UserTariff userTariff: tariffs) {
            String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", userTariff.getTariff().getService().getName(),
                    userTariff.getTariff().getName(), userTariff.getDateEnd());
            if (paymentDao.addWithdrawPayment(userTariff.getUser().getId(), userTariff.getTariff().getPrice(), userTariffWithdrawDescription)) {
                userTariffsDao.setUserTariffStatus(userTariff.getId(), SubscribeStatus.ACTIVE);
                LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
                userTariffsDao.setUserTariffEndDate(userTariff.getId(),date);

            } else {
                userTariffsDao.setUserTariffStatus(userTariff.getId(), SubscribeStatus.PAUSED);
                throw new NotEnoughBalanceException("alert.notEnoughBalance");
            }
        }
    }


    @Override
        public List<Payment> getPaymentsListByUserId (Integer limit, Integer total, Integer sortColumn, SortOrder
        sortOrder,int userId, PaymentType type) throws DbConnectionException {
            List<Payment> payments;

            try {
                payments = paymentDao.getPaymentsListByUser(limit, total, sortColumn, sortOrder.toString(), userId, type);

            } catch (DbConnectionException e) {
                throw new DbConnectionException(e);
            }
            return payments;
        }

        @Override
        public Integer getPaymentsCountByUserId ( int userId, PaymentType type) throws DbConnectionException {
            return paymentDao.getPaymentsCountByUserId(userId, type);
        }

    }
