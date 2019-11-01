package by.deniskruglik.socialnetwork.exception;

public class FriendInvitationException extends Exception {
    public FriendInvitationException() {
    }

    public FriendInvitationException(String message) {
        super(message);
    }

    public FriendInvitationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FriendInvitationException(Throwable cause) {
        super(cause);
    }

    public FriendInvitationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
