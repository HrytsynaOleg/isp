package enums;

public enum IncomingPaymentType {
    PAYMENT("customer payment"),
    MONEYBACK("money back after unsubscribe tariff");

    private final String name;

    IncomingPaymentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
