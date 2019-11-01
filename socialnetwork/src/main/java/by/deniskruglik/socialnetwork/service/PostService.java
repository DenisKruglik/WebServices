package by.deniskruglik.socialnetwork.service;

import by.deniskruglik.socialnetwork.data.FriendRelation;
import by.deniskruglik.socialnetwork.data.Like;
import by.deniskruglik.socialnetwork.data.Post;
import by.deniskruglik.socialnetwork.data.User;
import by.deniskruglik.socialnetwork.exception.PostForbiddenActionException;
import by.deniskruglik.socialnetwork.exception.PostServiceException;
import by.deniskruglik.socialnetwork.utils.contentfilters.FilterChain;
import by.deniskruglik.socialnetwork.utils.contentfilters.LinkWrapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import org.apache.commons.lang3.StringEscapeUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class PostService {
    private final static long POSTS_PAGE_SIZE = 10;

    @Inject
    private Dao<Post, Integer> postDao;

    @Inject
    private Dao<Like, Integer> likeDao;

    @Inject
    private Dao<User, Integer> userDao;

    @Inject
    private Dao<FriendRelation, Integer> friendRelationDao;

    public List<Post> getUsersPosts(int userId, int page) {
        QueryBuilder<Post, Integer> queryBuilder = postDao.queryBuilder();
        try {
            queryBuilder
                    .limit(POSTS_PAGE_SIZE)
                    .offset(POSTS_PAGE_SIZE * page)
                    .where()
                    .eq("author_id", userId);
            List<Post> posts = queryBuilder.query();
            return filterPostsContent(posts);
        } catch (SQLException e) {
            throw new PostServiceException("Failed to fetch posts", e);
        }
    }

    public List<Post> getPostsForUserFeed(User user, int page) {
        QueryBuilder<User, Integer> userQueryBuilder = userDao.queryBuilder();
        QueryBuilder<User, Integer> friendQueryBuilder = userDao.queryBuilder();
        QueryBuilder<FriendRelation, Integer> friendRelationQueryBuilder = friendRelationDao.queryBuilder();
        QueryBuilder<Post, Integer> postQueryBuilder = postDao.queryBuilder();
        try {
            userQueryBuilder.join("id", "user_id", friendRelationQueryBuilder);
            friendQueryBuilder.join("id", "friend_id", friendRelationQueryBuilder);
            postQueryBuilder.join("author_id", "id", friendQueryBuilder);
            userQueryBuilder.where().idEq(user.getId());
            postQueryBuilder
                    .limit(POSTS_PAGE_SIZE)
                    .offset(POSTS_PAGE_SIZE * page);
            List<Post> posts = postQueryBuilder.query();
            return filterPostsContent(posts);
        } catch (SQLException e) {
            throw new PostServiceException("Failed to fetch posts", e);
        }
    }

    private List<Post> filterPostsContent(List<Post> posts) {
        FilterChain filterChain = new FilterChain();
        filterChain.addFilter(new LinkWrapper());
        for (Post post: posts) {
            String filteredContent = filterChain.filter(post.getContent());
            post.setContent(filteredContent);
        }
        return posts;
    }

    public Post createPost(String content, User user) {
        content = StringEscapeUtils.escapeHtml4(content);
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(user);
        try {
            postDao.create(post);
            postDao.refresh(post);
            return post;
        } catch (SQLException e) {
            throw new PostServiceException("Failed to create post", e);
        }
    }

    public void deletePost(int id, User user) throws PostForbiddenActionException {
        try {
            Post post = postDao.queryForId(id);
            if (post.getAuthor().getId() != user.getId()) {
                throw new PostForbiddenActionException("Post doesn't belong to user");
            }
            postDao.delete(post);
        } catch (SQLException e) {
            throw new PostServiceException("Failed to delete post", e);
        }
    }

    public Like putLike(int postId, User user) throws PostForbiddenActionException {
        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("owner_id", user.getId());
        fieldValues.put("post_id", postId);
        try {
            List<Like> likes = likeDao.queryForFieldValues(fieldValues);
            if (!likes.isEmpty()) {
                throw new PostForbiddenActionException("There's already a like from user for this post");
            }
            Post post = postDao.queryForId(postId);
            Like like = new Like();
            like.setOwner(user);
            like.setPost(post);
            likeDao.create(like);
            likeDao.refresh(like);
            return like;
        } catch (SQLException e) {
            throw new PostServiceException("Failed to put like", e);
        }
    }

    public void deleteLike(int postId, User user) throws PostForbiddenActionException {
        try {
            Map<String, Object> fieldValues = new HashMap<>();
            fieldValues.put("owner_id", user.getId());
            fieldValues.put("post_id", postId);
            List<Like> likes = likeDao.queryForFieldValues(fieldValues);
            if (likes.isEmpty()) {
                throw new PostForbiddenActionException("There's no like for this post from current user");
            }
            if (likes.size() > 1) {
                throw new IllegalStateException("More than 1 instance of like found");
            }
            likeDao.delete(likes);
        } catch (SQLException e) {
            throw new PostServiceException("Failed to delete like", e);
        }
    }
}
