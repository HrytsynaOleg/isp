package dependecies;

import dao.IDao;
import dao.impl.ServiceDao;
import entity.Service;
import org.junit.jupiter.api.Test;

class DependencyManagerTest {
    IDao<Service> dao = new ServiceDao();

    @Test
    void getDao() {

    }
}