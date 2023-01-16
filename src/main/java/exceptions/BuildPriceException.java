package exceptions;

public class BuildPriceException extends Exception{
    public BuildPriceException() {
    }

    public BuildPriceException(String message) {
        super(message);
    }

    public BuildPriceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildPriceException(Throwable cause) {
        super(cause);
    }

    public BuildPriceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
