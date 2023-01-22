package controller.testClass;

import entity.User;
import entity.builder.UserBuilder;
import enums.UserRole;

public class TestUser {

    public static User getCustomer() {

        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUserEmail("test@mail.com");
        userBuilder.setUserPassword("password");
        return userBuilder.build();

    }
    public static User getAdmin() {

        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUserEmail("test@mail.com");
        userBuilder.setUserPassword("password");
        userBuilder.setUserRole(UserRole.CUSTOMER);
        return userBuilder.build();

    }
}

