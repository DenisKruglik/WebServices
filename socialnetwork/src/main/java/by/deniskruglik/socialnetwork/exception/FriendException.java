package by.deniskruglik.socialnetwork.exception;

public class FriendException extends Exception {
    public FriendException() {
    }

    public FriendException(String message) {
        super(message);
    }

    public FriendException(String message, Throwable cause) {
        super(message, cause);
    }

    public FriendException(Throwable cause) {
        super(cause);
    }

    public FriendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
