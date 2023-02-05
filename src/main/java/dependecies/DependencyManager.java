package dependecies;

import dao.IDao;
import dao.impl.*;
import repository.*;
import repository.impl.*;
import service.IPaymentService;
import service.IServicesService;
import service.ITariffsService;
import service.IUserService;
import service.impl.PaymentService;
import service.impl.ServicesService;
import service.impl.TariffsService;
import service.impl.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DependencyManager {
    private static final List<IDao> daoList = new ArrayList<>();
    public static final IServicesRepository serviceRepo;
    public static final IUserRepository userRepo;
    public static final ITariffRepository tariffRepo;
    public static final IUserTariffRepository userTariffRepo;
    public static final IPaymentRepository paymentRepo;
    public static final IServicesService serviceService;
    public static final IUserService userService;
    public static final ITariffsService tariffService;
    public static final IPaymentService paymentService;

    static {
        daoList.add(new ServiceDaoImpl());
        daoList.add(new UserDaoImpl());
        daoList.add(new TariffDaoImpl());
        daoList.add(new UserTariffDaoImpl());
        daoList.add(new PaymentDaoImpl());
    }

    static {
        serviceRepo = new ServicesRepositoryImpl(getDao(ServiceDaoImpl.class));
        userRepo = new UserRepositoryImpl(getDao(UserDaoImpl.class));
        tariffRepo = new TariffRepositoryImpl(getDao(TariffDaoImpl.class));
        userTariffRepo = new UserTariffRepositoryImpl(getDao(UserTariffDaoImpl.class));
        paymentRepo=new PaymentRepository(getDao(PaymentDaoImpl.class), getDao(UserDaoImpl.class), getDao(UserTariffDaoImpl.class));
    }

    static {
        serviceService = new ServicesService(serviceRepo, tariffRepo);
        userService = new UserService(userRepo, userTariffRepo, paymentRepo);
        tariffService = new TariffsService(tariffRepo, userTariffRepo, userRepo, paymentRepo);
        paymentService=new PaymentService(paymentRepo,userTariffRepo,userRepo);
    }

    private DependencyManager() {
    }

    private static <T> IDao<T> getDao(Class<T> className) {
        Optional<IDao> dao = daoList.stream()
                .filter(e -> e.getClass().equals(className))
                .findFirst();
        return dao.get();
    }

}

