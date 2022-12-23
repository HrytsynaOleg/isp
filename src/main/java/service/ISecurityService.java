package service;

public interface ISecurityService {
    boolean isPasswordVerify(String password, String hash);
}
