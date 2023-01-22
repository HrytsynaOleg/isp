package entity;

import enums.SubscribeStatus;

import java.time.LocalDate;
import java.util.Date;

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

}
