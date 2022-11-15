package exeptions;

public class DbConnectionExeption extends Exception{

    public DbConnectionExeption() {
    }

    public DbConnectionExeption(String message) {
        super(message);
    }

    public DbConnectionExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public DbConnectionExeption(Throwable cause) {
        super(cause);
    }

    public DbConnectionExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
