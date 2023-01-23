package dto;

import java.util.Objects;

public class DtoUser {
    private String id;
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String lastName;
    private String phone;
    private String address;
    private String role;
    private String status;

    public DtoUser() {
        this.id = "";
        this.email = "";
        this.password = "";
        this.confirmPassword = "";
        this.name = "";
        this.lastName = "";
        this.phone = "";
        this.address = "";
        this.role = "";
        this.status = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DtoUser dtoUser = (DtoUser) o;
        return Objects.equals(id, dtoUser.id) &&
                Objects.equals(email, dtoUser.email) &&
                Objects.equals(password, dtoUser.password) &&
                Objects.equals(confirmPassword, dtoUser.confirmPassword) &&
                Objects.equals(name, dtoUser.name) &&
                Objects.equals(lastName, dtoUser.lastName) &&
                Objects.equals(phone, dtoUser.phone) &&
                Objects.equals(address, dtoUser.address) &&
                Objects.equals(role, dtoUser.role) &&
                Objects.equals(status, dtoUser.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, confirmPassword, name, lastName, phone, address, role, status);
    }
}
