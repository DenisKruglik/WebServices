package by.deniskruglik.socialnetwork.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "likes")
public class Like {
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "owner_id")
    private User owner;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "post_id")
    private Post post;

    @DatabaseField(dataType = DataType.DATE_STRING, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL",
            readOnly = true, canBeNull = false, columnName = "created_at")
    private Date createdAt;

    public Like() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
