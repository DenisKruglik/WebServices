package by.deniskruglik.socialnetwork.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "friend_relations")
public class FriendRelation {
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "user_id")
    private User user;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "friend_id")
    private User friend;

    public FriendRelation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}
