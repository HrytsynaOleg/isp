package controller.testClass;

import entity.Tariff;
import entity.User;
import entity.builder.UserBuilder;
import enums.BillingPeriod;
import enums.TariffStatus;
import enums.UserRole;

import java.math.BigDecimal;

public class TestTariff {

    public static Tariff getTestTariff() {

        return new Tariff(5,TestService.getTestService(),"TestTariff","TestTariffDescription",
                new BigDecimal(100), BillingPeriod.MONTH, TariffStatus.ACTIVE);
    }

}

