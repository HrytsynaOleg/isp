package service.impl;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import service.ISecurityService;

public class SecurityService implements ISecurityService {

    private final Argon2 argon2;

    public SecurityService() {
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
    }

    @Override
    public boolean isPasswordVerify(String password, String hash) {
        return argon2.verify(hash, password.toCharArray());
    }

    @Override
    public String getPasswordHash(String password) {
        return argon2.hash(2,15*1024,1, password.toCharArray());
    }
}
