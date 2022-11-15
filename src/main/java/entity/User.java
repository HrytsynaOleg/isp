package entity;

import enums.UserRole;
import enums.UserStatus;

public class User {

    private Integer id;
    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;
    private String name;
    private String lastName;
    private String phone;
    private String adress;

    public User() {
        this.id=0;
        this.email="";
        this.password="";
        this.role=UserRole.USER;
        this.status=UserStatus.ACTIVE;
        this.name="";
        this.lastName="";
        this.phone="";
        this.adress="";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
