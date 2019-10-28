package by.deniskruglik.socialnetwork.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "chats")
public class Chat {
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false, columnName = "type")
    private ChatType type;

    @DatabaseField(dataType = DataType.DATE_STRING, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL",
            readOnly = true, canBeNull = false, columnName = "created_at")
    private Date createdAt;

    public Chat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
