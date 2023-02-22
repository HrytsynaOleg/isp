package testClass;

import entity.Payment;
import entity.User;
import enums.IncomingPaymentType;
import enums.PaymentType;

import java.math.BigDecimal;
import java.util.Date;

public class TestPayment {

    public static Payment getTestInPayment(User user) {

        return new Payment(2, user, new BigDecimal(100), new Date(), PaymentType.IN, IncomingPaymentType.PAYMENT.getName());
    }

    public static Payment getTestOutPayment(User user, String description) {

        return new Payment(2, user, new BigDecimal(100), new Date(), PaymentType.OUT, description);
    }

}

