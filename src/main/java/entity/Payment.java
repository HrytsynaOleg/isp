package entity;

import enums.PaymentType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Payment {
    int id;
    User user;
    BigDecimal value;
    Date data;
    PaymentType type;
    String description;

    public Payment(int id, User user, BigDecimal value, Date data, PaymentType type, String description) {
        this.id = id;
        this.user = user;
        this.value = value;
        this.data = data;
        this.type = type;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id == payment.id && Objects.equals(user, payment.user) &&
                Objects.equals(value, payment.value) &&
                type == payment.type &&
                Objects.equals(description, payment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, value, data, type, description);
    }
}
