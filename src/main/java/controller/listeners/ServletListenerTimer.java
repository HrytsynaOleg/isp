package controller.listeners;

import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import service.IPaymentService;
import service.impl.PaymentService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class ServletListenerTimer implements ServletContextListener {
    Timer timer = null;
    IPaymentService paymentService=new PaymentService();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    paymentService.extendExpiredUserTariffs();
//                    System.out.println("timer task run ok");
                } catch (DbConnectionException | NotEnoughBalanceException e) {
                    System.out.println("timer task error" + e);
                }
            }
        },0,60*1000);

    }

}
