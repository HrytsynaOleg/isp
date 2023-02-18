package testClass;

import entity.User;
import entity.builder.UserBuilder;
import enums.UserRole;

public class TestUser {

    public static User getCustomer() {

        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUserName("User");
        userBuilder.setUserLastName("Test");
        userBuilder.setUserEmail("test@mail.com");
        userBuilder.setUserPassword("password");
        userBuilder.setUserAdress("User address");
        userBuilder.setUserPhone("+380666666666");
        userBuilder.setUserId(25);
        return userBuilder.build();

    }
    public static User getAdmin() {

        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUserEmail("test@mail.com");
        userBuilder.setUserPassword("password");
        userBuilder.setUserId(25);
        userBuilder.setUserRole(UserRole.ADMIN);
        return userBuilder.build();

    }
}

