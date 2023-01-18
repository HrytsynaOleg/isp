package dao;

import entity.Payment;
import enums.PaymentType;
import exceptions.DbConnectionException;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentDao {
    void addIncomingPayment(int userId, BigDecimal value, String description) throws DbConnectionException;
    List<Payment> getPaymentsListByUser(Integer limit, Integer total, Integer sort, String order, int userId, PaymentType type) throws DbConnectionException;
    Integer getPaymentsCountByUserId(int userId, PaymentType type) throws DbConnectionException;
    boolean addWithdrawPayment(int userId, BigDecimal value, String description) throws DbConnectionException;
}
