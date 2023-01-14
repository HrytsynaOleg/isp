package entity;

import enums.BillingPeriod;
import enums.SubscribeStatus;
import enums.TariffStatus;
import java.math.BigDecimal;

public class Tariff {
    private int id;
    private Service service;
    private String name;
    private String description;
    private BigDecimal price;
    private BillingPeriod period;
    private TariffStatus status;
    private SubscribeStatus subscribe;

    public Tariff(int id, Service service, String name, String description, BigDecimal price, BillingPeriod period, TariffStatus status) {
        this.id = id;
        this.service = service;
        this.name = name;
        this.description = description;
        this.price = price;
        this.period = period;
        this.status=status;
        this.subscribe=SubscribeStatus.UNSUBSCRIBE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BillingPeriod getPeriod() {
        return period;
    }

    public void setPeriod(BillingPeriod period) {
        this.period = period;
    }

    public TariffStatus getStatus() {
        return status;
    }

    public void setStatus(TariffStatus status) {
        this.status = status;
    }

    public SubscribeStatus getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(SubscribeStatus subscribe) {
        this.subscribe = subscribe;
    }
}
