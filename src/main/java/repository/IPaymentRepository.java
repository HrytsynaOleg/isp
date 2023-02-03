package repository;

import entity.Payment;
import enums.PaymentType;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;

import java.util.List;
import java.util.Map;

public interface IPaymentRepository {
    Payment addPayment(Payment payment) throws DbConnectionException, NotEnoughBalanceException;
    List<Payment> getPaymentsListByUser(int userId, PaymentType type, Map<String,String> parameters) throws DbConnectionException;
    Integer getPaymentsCountByUserId(int userId, PaymentType type) throws DbConnectionException;
}
