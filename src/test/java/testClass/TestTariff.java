package testClass;

import entity.Tariff;
import enums.BillingPeriod;
import enums.TariffStatus;

import java.math.BigDecimal;

public class TestTariff {

    public static Tariff getTestTariff() {

        return new Tariff(5,TestService.getTestService(),"TestTariff","TestTariffDescription",
                new BigDecimal(100), BillingPeriod.MONTH, TariffStatus.ACTIVE);
    }

}

