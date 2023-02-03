package resolver;

import dao.IDao;
import dao.impl.ServiceDao;
import entity.Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DependencyManagerTest {
    IDao<Service> dao = new ServiceDao();

    @Test
    void getDao() {

    }
}