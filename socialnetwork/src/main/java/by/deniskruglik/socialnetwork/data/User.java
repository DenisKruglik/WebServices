package by.deniskruglik.socialnetwork.data;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "users")
public class User {
    public final static String NICKNAME_FIELD = "nickname";
    public final static String PASSWORD_HASH_FIELD = "password_hash";

    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(unique = true, canBeNull = false, columnName = NICKNAME_FIELD)
    private String nickname;

    @DatabaseField(columnName = PASSWORD_HASH_FIELD)
    private transient String passwordHash;

    @DatabaseField(dataType = DataType.DATE_STRING, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL",
            readOnly = true, canBeNull = false, columnName = "created_at")
    private Date createdAt;

    @ForeignCollectionField(foreignFieldName = "author")
    private transient ForeignCollection<Post> posts;

    public ForeignCollection<Post> getPosts() {
        return posts;
    }

    public void setPosts(ForeignCollection<Post> posts) {
        this.posts = posts;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
