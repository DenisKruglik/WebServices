package by.deniskruglik.socialnetwork.exception;

public class ConnectionSourceGettingException extends Exception {
    public ConnectionSourceGettingException() {
    }

    public ConnectionSourceGettingException(String message) {
        super(message);
    }

    public ConnectionSourceGettingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionSourceGettingException(Throwable cause) {
        super(cause);
    }

    public ConnectionSourceGettingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
