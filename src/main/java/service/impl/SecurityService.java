package service.impl;

import service.ISecurityService;

public class SecurityService implements ISecurityService {
    @Override
    public boolean isPasswordVerify(String password, String hash) {
        return password.equals(hash);
    }
}
