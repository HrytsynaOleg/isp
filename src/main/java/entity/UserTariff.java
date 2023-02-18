package entity;

import enums.SubscribeStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class UserTariff {
    int id;
    User user;
    Tariff tariff;
    SubscribeStatus subscribeStatus;
    LocalDate dateBegin;
    LocalDate dateEnd;

    public UserTariff(int id, User user, Tariff tariff, SubscribeStatus subscribeStatus, LocalDate dateBegin, LocalDate dateEnd) {
        this.id = id;
        this.user = user;
        this.tariff = tariff;
        this.subscribeStatus = subscribeStatus;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
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

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public SubscribeStatus getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(SubscribeStatus subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    public LocalDate getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(LocalDate dateBegin) {
        this.dateBegin = dateBegin;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public BigDecimal calcMoneyBackValue() {
        Tariff tariff = this.getTariff();
        long duration = Duration.between(LocalDate.now().atStartOfDay(), this.getDateEnd().atStartOfDay()).toDays() - 1;
        BigDecimal moneyBackPeriod = BigDecimal.valueOf(duration);
        BigDecimal priceForDay = tariff.getPrice().divide(BigDecimal.valueOf(tariff.getPeriod().getDivider()), RoundingMode.HALF_UP);
        return moneyBackPeriod.compareTo(BigDecimal.ZERO) > 0 ? priceForDay.multiply(moneyBackPeriod) : BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTariff that = (UserTariff) o;
        return id == that.id && Objects.equals(user, that.user) &&
                Objects.equals(tariff, that.tariff) && subscribeStatus == that.subscribeStatus &&
                Objects.equals(dateBegin, that.dateBegin) &&
                Objects.equals(dateEnd, that.dateEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, tariff, subscribeStatus, dateBegin, dateEnd);
    }
}
