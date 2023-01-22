package dao;

import entity.Payment;
import enums.PaymentType;
import exceptions.DbConnectionException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IPaymentDao {
    void addIncomingPayment(int userId, BigDecimal value, String description) throws DbConnectionException;
    List<Payment> getPaymentsListByUser(int userId, PaymentType type, Map<String,String> parameters) throws DbConnectionException;
    Integer getPaymentsCountByUserId(int userId, PaymentType type) throws DbConnectionException;
    boolean addWithdrawPayment(int userId, BigDecimal value, String description) throws DbConnectionException;
}
