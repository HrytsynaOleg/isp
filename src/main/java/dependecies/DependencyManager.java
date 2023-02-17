package dependecies;

import dao.IDao;
import dao.impl.*;
import entity.*;
import repository.*;
import repository.impl.*;
import service.*;
import service.impl.*;


public class DependencyManager {

    public static final IDao<Tariff> tariffDao;
    public static final IDao<UserTariff> userTariffDao;
    public static final IDao<User> userDao;
    public static final IDao<Service> serviceDao;
    public static final IDao<Payment> paymentDao;
    public static final IServicesRepository serviceRepo;
    public static final IUserRepository userRepo;
    public static final ITariffRepository tariffRepo;
    public static final IUserTariffRepository userTariffRepo;
    public static final IPaymentRepository paymentRepo;
    public static final IServicesService serviceService;
    public static final IUserService userService;
    public static final ITariffsService tariffService;
    public static final IPaymentService paymentService;
    public static final IEmailService emailService;

    static {
        tariffDao = new TariffDaoImpl();
        userTariffDao = new UserTariffDaoImpl();
        userDao = new UserDaoImpl();
        serviceDao= new ServiceDaoImpl();
        paymentDao= new PaymentDaoImpl();

    }

    static {
        serviceRepo = new ServicesRepositoryImpl(serviceDao);
        userRepo = new UserRepositoryImpl(userDao, userTariffDao, paymentDao);
        tariffRepo = new TariffRepositoryImpl(tariffDao, userTariffDao, paymentDao, userDao);
        userTariffRepo = new UserTariffRepositoryImpl(userTariffDao);
        paymentRepo = new PaymentRepository(paymentDao, userDao, userTariffDao);
    }

    static {
        serviceService = new ServicesService(serviceRepo, tariffRepo);
        emailService = new EmailService();
        userService = new UserService(userRepo, userTariffRepo, SecurityService.getInstance(), emailService);
        tariffService = new TariffsService(tariffRepo, userTariffRepo, userRepo);
        paymentService = new PaymentService(paymentRepo, userTariffRepo, userRepo);

    }

    private DependencyManager() {
    }

}

