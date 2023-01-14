package exceptions;

public class TariffAlreadySubscribedException extends Exception{
    public TariffAlreadySubscribedException() {
    }

    public TariffAlreadySubscribedException(String message) {
        super(message);
    }

    public TariffAlreadySubscribedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TariffAlreadySubscribedException(Throwable cause) {
        super(cause);
    }

    public TariffAlreadySubscribedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
