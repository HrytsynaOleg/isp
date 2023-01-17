package enums;

import java.time.LocalDate;

public enum BillingPeriod {
    DAY, MONTH, YEAR;

    public LocalDate getNexDate(LocalDate begin) {

        return switch (this.toString()) {
            case "DAY" -> begin.plusDays(1);
            case "MONTH" -> begin.plusMonths(1);
            case "YEAR" -> begin.plusYears(1);
            default -> null;
        };
    }
}
