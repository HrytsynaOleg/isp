package dependecies;

import dao.IDao;
import dao.impl.ServiceDaoImpl;
import dao.impl.TariffDaoImpl;
import dao.impl.UserDaoImpl;
import dao.impl.UserTariffDaoImpl;
import repository.IServicesRepository;
import repository.ITariffRepository;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import repository.impl.ServicesRepositoryImpl;
import repository.impl.TariffRepositoryImpl;
import repository.impl.UserRepositoryImpl;
import repository.impl.UserTariffRepositoryImpl;
import service.IServicesService;
import service.ITariffsService;
import service.IUserService;
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
    public static final IServicesService serviceService;
    public static final IUserService userService;
    public static final ITariffsService tariffService;

    static {
        daoList.add(new ServiceDaoImpl());
        daoList.add(new UserDaoImpl());
        daoList.add(new TariffDaoImpl());
        daoList.add(new UserTariffDaoImpl());
    }

    static {
        serviceRepo = new ServicesRepositoryImpl(getDao(ServiceDaoImpl.class));
        userRepo = new UserRepositoryImpl(getDao(UserDaoImpl.class));
        tariffRepo = new TariffRepositoryImpl(getDao(TariffDaoImpl.class));
        userTariffRepo = new UserTariffRepositoryImpl(tariffRepo,userRepo,getDao(UserTariffDaoImpl.class));
    }

    static {
        serviceService = new ServicesService(serviceRepo);
        userService = new UserService(userRepo);
        tariffService = new TariffsService(tariffRepo, userTariffRepo, userRepo);
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

