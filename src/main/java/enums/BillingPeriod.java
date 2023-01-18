package enums;

import java.time.LocalDate;

public enum BillingPeriod {
    DAY(1),
    MONTH(30),
    YEAR(365);

    private final int divider;

    BillingPeriod(int divider) {
        this.divider = divider;
    }

    public LocalDate getNexDate(LocalDate begin) {

        return switch (this.toString()) {
            case "DAY" -> begin.plusDays(1);
            case "MONTH" -> begin.plusMonths(1);
            case "YEAR" -> begin.plusYears(1);
            default -> null;
        };
    }

    public int getDivider() {
        return divider;
    }
}
