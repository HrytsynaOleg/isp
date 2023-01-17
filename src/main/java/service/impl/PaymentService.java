package service.impl;

import dao.IPaymentDao;
import dao.impl.PaymentDao;
import entity.Payment;
import entity.User;
import enums.SortOrder;
import exceptions.DbConnectionException;
import service.IPaymentService;

import java.math.BigDecimal;
import java.util.List;

public class PaymentService implements IPaymentService {
    IPaymentDao paymentDao = new PaymentDao();
    @Override
    public void addIncomingPayment(int userId, BigDecimal value) throws DbConnectionException {

        paymentDao.addIncomingPayment(userId, value);
    }

    @Override
    public List<Payment> getIncomingPaymentsListByUserId(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int userId) throws DbConnectionException {
        List<Payment> payments;

        try {
            payments = paymentDao.getIncomingPaymentsListByUser(limit, total, sortColumn, sortOrder.toString(), userId);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return payments;
    }

    @Override
    public Integer getIncomingPaymentsCountByUserId(int userId) throws DbConnectionException {
        return paymentDao.getIncomingPaymentsCountByUserId(userId);
    }
}
