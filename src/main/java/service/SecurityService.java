package service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class SecurityService {

    private static final Argon2 argon2=Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
    private static SecurityService instance;

    private SecurityService() {}

    public static SecurityService getInstance() {
        if (instance==null) {
            instance=new SecurityService();
        }
        return instance;
    }

    public boolean isPasswordVerify(String password, String hash) {
        return argon2.verify(hash, password.toCharArray());
    }

    public String getPasswordHash(String password) {
        return argon2.hash(2,15*1024,1, password.toCharArray());
    }
}
