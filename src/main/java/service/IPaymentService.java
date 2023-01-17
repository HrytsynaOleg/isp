package service;

import entity.Payment;
import enums.SortOrder;
import exceptions.DbConnectionException;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentService {
    void addIncomingPayment(int userId, BigDecimal value) throws DbConnectionException;
    List<Payment> getIncomingPaymentsListByUserId (Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int userId) throws DbConnectionException;
    Integer getIncomingPaymentsCountByUserId (int userId) throws DbConnectionException;
}
