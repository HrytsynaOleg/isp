package repository;

import entity.Payment;
import entity.Tariff;
import entity.UserTariff;
import enums.PaymentType;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IPaymentRepository {
    Payment addPayment(Payment payment,List<UserTariff> pausedTariffs) throws NotEnoughBalanceException, SQLException;
    Payment addWithdraw(Payment payment, UserTariff userTariff) throws NotEnoughBalanceException, SQLException;
    List<Payment> getPaymentsListByUser(int userId, PaymentType type, Map<String,String> parameters) throws SQLException;
    List<Payment> getPaymentsListAllUsers(PaymentType type, Map<String,String> parameters) throws SQLException;
    Integer getPaymentsCountByUserId(int userId, PaymentType type) throws DbConnectionException, SQLException;
    Integer getPaymentsCountAllUsers(PaymentType type) throws DbConnectionException, SQLException;
}
