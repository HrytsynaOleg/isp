package dto;

public class DtoTariff {
    private String id;
    private String name;
    private String description;
    private String service;
    private String status;
    private String price;
    private String period;

    public DtoTariff(String id, String name, String description, String service, String status, String price, String period) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.service = service;
        this.status = status;
        this.price = price;
        this.period = period;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
