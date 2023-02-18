package testClass;

import entity.Service;

public class TestService {

    public static Service getTestService() {

        return new Service(2,"TestServiceName","TestServicedDescription");
    }

}

