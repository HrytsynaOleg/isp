package service;

import entity.Payment;
import enums.PaymentType;
import enums.SortOrder;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentService {
    void addIncomingPayment(int userId, BigDecimal value) throws DbConnectionException, NotEnoughBalanceException;
    void extendExpiredUserTariffs() throws DbConnectionException, NotEnoughBalanceException;
    List<Payment> getPaymentsListByUserId (Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int userId,PaymentType type) throws DbConnectionException;
    Integer getPaymentsCountByUserId (int userId, PaymentType type) throws DbConnectionException;

}
