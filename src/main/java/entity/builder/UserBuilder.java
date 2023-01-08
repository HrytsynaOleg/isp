package entity.builder;

import entity.User;
import enums.UserRole;
import enums.UserStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class UserBuilder {

    private final User user;

    public UserBuilder() {
        this.user = new User();
    }

    public UserBuilder setUserId(int id) {
        this.user.setId(id);
        return this;
    }

    public UserBuilder setUserEmail(String email) {
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder setUserPassword(String pass) {
        this.user.setPassword(pass);
        return this;
    }

    public UserBuilder setUserRole(UserRole role) {
        this.user.setRole(role);
        return this;
    }

    public UserBuilder setUserStatus(UserStatus status) {
        this.user.setStatus(status);
        return this;
    }

    public UserBuilder setUserName(String name) {
        this.user.setName(name);
        return this;
    }

    public UserBuilder setUserLastName(String name) {
        this.user.setLastName(name);
        return this;
    }

    public UserBuilder setUserPhone(String phone) {
        this.user.setPhone(phone);
        return this;
    }

    public UserBuilder setUserAdress(String adress) {
        this.user.setAdress(adress);
        return this;
    }
    public UserBuilder setUserBalance(String balance) {
        this.user.setBalance(new BigDecimal(balance));
        return this;
    }
    public UserBuilder setUserRegistration(Date date) {
        this.user.setRegistration(date);
        return this;
    }

    public User build() {
        return user;
    }

}
