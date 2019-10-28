package by.deniskruglik.socialnetwork.data;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "posts")
public class Post {
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "author_id")
    private User author;

    @DatabaseField(canBeNull = false, columnName = "content")
    private String content;

    @DatabaseField(dataType = DataType.DATE_STRING, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL",
            readOnly = true, canBeNull = false, columnName = "created_at")
    private Date createdAt;

    @ForeignCollectionField(foreignFieldName = "post")
    private ForeignCollection<Like> likes;

    public ForeignCollection<Like> getLikes() {
        return likes;
    }

    public void setLikes(ForeignCollection<Like> likes) {
        this.likes = likes;
    }

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
