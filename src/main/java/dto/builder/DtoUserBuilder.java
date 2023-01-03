package dto.builder;

import dto.DtoUser;
import entity.User;
import enums.UserRole;
import enums.UserStatus;

public class DtoUserBuilder {

    private final DtoUser user;

    public DtoUserBuilder() {
        this.user = new DtoUser();
    }

    public DtoUserBuilder setUserId(String id) {
        this.user.setId(id);
        return this;
    }

    public DtoUserBuilder setUserEmail(String email) {
        this.user.setEmail(email);
        return this;
    }

    public DtoUserBuilder setUserPassword(String password) {
        this.user.setPassword(password);
        return this;
    }

    public DtoUserBuilder setUserConfirmPassword(String confirmPassword) {
        this.user.setPassword(confirmPassword);
        return this;
    }

    public DtoUserBuilder setUserRole(String role) {
        this.user.setRole(role);
        return this;
    }

    public DtoUserBuilder setUserStatus(String status) {
        this.user.setStatus(status);
        return this;
    }

    public DtoUserBuilder setUserName(String name) {
        this.user.setName(name);
        return this;
    }

    public DtoUserBuilder setUserLastName(String name) {
        this.user.setLastName(name);
        return this;
    }

    public DtoUserBuilder setUserPhone(String phone) {
        this.user.setPhone(phone);
        return this;
    }

    public DtoUserBuilder setUserAdress(String adress) {
        this.user.setAddress(adress);
        return this;
    }

    public DtoUser build() {
        return user;
    }

}
