package by.deniskruglik.socialnetwork.controller;

public class Path {
    public final static String LOGIN_URL = "/auth/login/";
    public final static String REGISTER_URL = "/auth/register/";
    public final static String LOGOUT_URL = "/auth/logout/";
    public final static String CURRENT_USER_URL = "/auth/current-user/";

    public final static String FRIENDS_URL = "/friends/";
    public final static String FRIEND_BY_ID_URL = "/friend/:friendId/";
    public final static String FRIEND_INVITATIONS_URL = "/friends/invitations/";
    public final static String FRIEND_INVITATIONS_FROM_URL = "/friends/invitations/from/";
    public final static String FRIEND_INVITATIONS_TO_URL = "/friends/invitations/to/";
    public final static String ACCEPT_FRIEND_INVITATION_URL = "/friends/invitations/accept/";
    public final static String REJECT_FRIEND_INVITATION_URL = "/friends/invitations/reject/";

    public final static String GET_USER_BY_ID_URL = "/user/:id/";
    public final static String SEARCH_USERS_URL = "/users/:query/";

    public final static String USER_POSTS_URL = "/user/:userId/posts/";
    public final static String POSTS_URL = "/posts/";
    public final static String POST_BY_ID_URL = "/post/:postId/";
    public final static String POSTS_FEED_URL = "/posts/feed/";
    public final static String POST_LIKES_URL = "/post/:postId/likes/";

}
