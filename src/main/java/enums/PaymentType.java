package enums;

import java.math.BigDecimal;

public enum PaymentType {
    IN, OUT;

    public BigDecimal calculateBalance(BigDecimal balance, BigDecimal value) {
        return switch (this.toString()) {
            case "IN" -> balance.add(value);
            case "OUT" -> balance.subtract(value);
            default -> null;
        };
    }
}
