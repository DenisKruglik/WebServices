package by.deniskruglik.socialnetwork.controller;

import by.deniskruglik.socialnetwork.data.User;
import by.deniskruglik.socialnetwork.exception.PostForbiddenActionException;
import by.deniskruglik.socialnetwork.service.PostService;
import by.deniskruglik.socialnetwork.utils.JsonBodyConverter;
import by.deniskruglik.socialnetwork.utils.ResponseUtils;
import by.deniskruglik.socialnetwork.utils.StatusCode;
import com.google.inject.Inject;

import java.util.Map;

public class PostsController {
    private final static String USER_ID_PARAM = ":userId";
    private final static String POST_ID_PARAM = ":postId";
    private final static String PAGE_PARAM = "page";
    private final static String USER_KEY = "user";
    private final static String CONTENT_KEY = "content";

    @Inject
    private PostService postService;

    public final APIRoute getUserPosts = new APIRoute(RouteMethod.GET, Path.USER_POSTS_URL, (request, response) -> {
        String userIdString = request.params(USER_ID_PARAM);
        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request query");
        }
        String pageString = request.queryParams(PAGE_PARAM);
        int page = 0;
        if (pageString != null) {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException e) {
                return ResponseUtils.errorMessage(response, "Invalid request query");
            }
        }
        return postService.getUsersPosts(userId, page);
    });

    public final APIRoute getFeed = new APIRoute(RouteMethod.GET, Path.POSTS_FEED_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        String pageString = request.queryParams(PAGE_PARAM);
        int page = 0;
        if (pageString != null) {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException e) {
                return ResponseUtils.errorMessage(response, "Invalid request query");
            }
        }
        return postService.getPostsForUserFeed(user, page);
    });

    public final APIRoute createPost = new APIRoute(RouteMethod.POST, Path.POSTS_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        Map<String, String> bodyParams = JsonBodyConverter.convertJsonBodyToMap(request);
        if (!bodyParams.containsKey(CONTENT_KEY)) {
            return ResponseUtils.errorMessage(response, "Required parameters are missing");
        }
        String content = bodyParams.get(CONTENT_KEY);
        return postService.createPost(content, user);
    });

    public final APIRoute deletePost = new APIRoute(RouteMethod.DELETE, Path.POST_BY_ID_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        String postIdString = request.params(POST_ID_PARAM);
        int postId;
        try {
            postId = Integer.parseInt(postIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request query");
        }
        try {
            postService.deletePost(postId, user);
            return ResponseUtils.successMessage(response, "Post successfully deleted");
        } catch (PostForbiddenActionException e) {
            return ResponseUtils.message(response, e.getMessage(), StatusCode.FORBIDDEN);
        }
    });

    public final APIRoute putLike = new APIRoute(RouteMethod.POST, Path.POST_LIKES_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        String postIdString = request.params(POST_ID_PARAM);
        int postId;
        try {
            postId = Integer.parseInt(postIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request query");
        }
        try {
            return postService.putLike(postId, user);
        } catch (PostForbiddenActionException e) {
            return ResponseUtils.errorMessage(response, e.getMessage());
        }
    });

    public final APIRoute deleteLike = new APIRoute(RouteMethod.DELETE, Path.POST_LIKES_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        String postIdString = request.params(POST_ID_PARAM);
        int postId;
        try {
            postId = Integer.parseInt(postIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request query");
        }
        try {
            postService.deleteLike(postId, user);
            return ResponseUtils.successMessage(response, "Like deleted successfully");
        } catch (PostForbiddenActionException e) {
            return ResponseUtils.errorMessage(response, e.getMessage());
        }
    });
}
