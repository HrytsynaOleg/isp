package dto;

public class DtoUserTariff extends DtoTariff {
    private String subscribe;

    public DtoUserTariff(String id, String name, String description, String service, String status, String price, String period, String subscribe) {
        super(id, name, description, service, status, price, period);
        this.subscribe = subscribe;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }
}
