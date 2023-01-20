package controller.listeners;

import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IPaymentService;
import service.impl.PaymentService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Timer;
import java.util.TimerTask;

import static settings.AppProperties.getProperty;

@WebListener
public class ServletListenerTimer implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(ServletListenerTimer.class);
    Timer timer = null;
    IPaymentService paymentService=new PaymentService();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.info("Timer task started");
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    paymentService.extendExpiredUserTariffs();
                } catch (DbConnectionException | NotEnoughBalanceException e) {
                    logger.error("timer task error " + e);
                }
            }
        },0, Long.parseLong(getProperty("timer.updateTariffPeriod")));

    }

}
