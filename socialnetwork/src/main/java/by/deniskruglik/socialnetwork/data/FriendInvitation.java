package by.deniskruglik.socialnetwork.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "friend_invitations")
public class FriendInvitation {
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "sender_id")
    private User sender;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "receiver_id")
    private User receiver;

    @DatabaseField(canBeNull = false, columnName = "status")
    private FriendInvitationStatus status;

    public FriendInvitation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public FriendInvitationStatus getStatus() {
        return status;
    }

    public void setStatus(FriendInvitationStatus status) {
        this.status = status;
    }
}
