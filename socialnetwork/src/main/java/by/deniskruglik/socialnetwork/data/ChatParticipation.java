package by.deniskruglik.socialnetwork.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "chat_participations")
public class ChatParticipation {
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "chat_id")
    private Chat chat;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "user_id")
    private User user;

    public ChatParticipation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
