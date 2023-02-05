package service;

import dto.DtoTable;
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
    List<Payment> getPaymentsListByUserId (DtoTable dtoTable, int userId, PaymentType type) throws DbConnectionException;
    List<Payment> getPaymentsListAllUsers(DtoTable dtoTable, PaymentType type) throws DbConnectionException;
    Integer getPaymentsCountByUserId (int userId, PaymentType type) throws DbConnectionException;
    Integer getPaymentsCountAllUsers (PaymentType type) throws DbConnectionException;

}
