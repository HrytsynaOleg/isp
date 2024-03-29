package controller.listeners;

import dependecies.DependencyManager;
import exceptions.DbConnectionException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IPaymentService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Timer;
import java.util.TimerTask;

import static settings.properties.AppPropertiesManager.getProperty;

@WebListener
public class ServletListenerTimer implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(ServletListenerTimer.class);
    Timer timer = null;
    IPaymentService paymentService= DependencyManager.paymentService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.info("Timer task started");
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    paymentService.extendExpiredUserTariffs();
                } catch (DbConnectionException  e) {
                    logger.error("timer task error " + e);
                }
            }
        },0, Long.parseLong(getProperty("timer.updateTariffPeriod")));

    }

}
