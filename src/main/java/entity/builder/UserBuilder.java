package entity.builder;

import entity.User;

public class UserBuilder {

    private final User user;

    public UserBuilder(User user) {
        this.user = user;
    }

    UserBuilder setUserName(String name) {
        this.user.setName(name);
        return this;
    }

    UserBuilder setUserLastName(String name) {
        this.user.setLastName(name);
        return this;
    }

}
