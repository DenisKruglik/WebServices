package by.deniskruglik.socialnetwork.exception;

public class PostForbiddenActionException extends Exception {
    public PostForbiddenActionException() {
    }

    public PostForbiddenActionException(String message) {
        super(message);
    }

    public PostForbiddenActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostForbiddenActionException(Throwable cause) {
        super(cause);
    }

    public PostForbiddenActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
