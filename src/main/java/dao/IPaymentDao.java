package dao;

import entity.Payment;
import entity.User;
import enums.SortOrder;
import exceptions.DbConnectionException;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentDao {
    void addIncomingPayment(int userId, BigDecimal value) throws DbConnectionException;
    List<Payment> getIncomingPaymentsListByUser(Integer limit, Integer total, Integer sortColumn, String sortOrder, int userId) throws DbConnectionException;
    Integer getIncomingPaymentsCountByUserId(int userId) throws DbConnectionException;
    boolean addWithdrawPayment(int userId, BigDecimal value, String description) throws DbConnectionException;
}
