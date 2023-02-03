package resolver;

import dao.IDao;
import dao.impl.ServiceDao;
import entity.Service;
import repository.IServicesRepository;
import repository.impl.ServicesRepositoryImpl;
import service.IServicesService;
import service.impl.ServicesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DependencyManager {
    private static final List<IDao> daoList = new ArrayList<>();
    public static final IServicesRepository serviceRepo;
    public static final IServicesService serviceService;

    static {
        daoList.add(new ServiceDao());
    }

    static {
        serviceRepo= new ServicesRepositoryImpl(getDao(ServiceDao.class));
    }

    static{
        serviceService = new ServicesService(serviceRepo);
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

