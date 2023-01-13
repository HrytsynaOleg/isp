package exceptions;

public class RelatedRecordsExistException extends Exception{
    public RelatedRecordsExistException() {
    }

    public RelatedRecordsExistException(String message) {
        super(message);
    }

    public RelatedRecordsExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelatedRecordsExistException(Throwable cause) {
        super(cause);
    }

    public RelatedRecordsExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
