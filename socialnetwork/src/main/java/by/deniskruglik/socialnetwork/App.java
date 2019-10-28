package by.deniskruglik.socialnetwork;

import by.deniskruglik.socialnetwork.controller.*;
import by.deniskruglik.socialnetwork.di.InjectLogger;
import by.deniskruglik.socialnetwork.utils.CorsEnabler;
import by.deniskruglik.socialnetwork.utils.JsonTransformer;
import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class App {
    @InjectLogger
    private Logger logger;

    @Inject
    private JsonTransformer jsonTransformer;

    @Inject
    private CorsEnabler corsEnabler;

    @Inject
    private AuthController authController;

    @Inject
    private FriendsController friendsController;

    @Inject
    private UserController userController;

    @Inject
    private PostsController postsController;

    private List<APIRoute> getRoutes() {
        return Arrays.asList(
                authController.login,
                authController.logout,
                authController.register,
                authController.getCurrentUser,
                friendsController.getUsersFriends,
                friendsController.sendFriendInvitation,
                friendsController.acceptFriendInvitation,
                friendsController.rejectFriendInvitation,
                friendsController.removeFriend,
                friendsController.getFriendInvitationsForCurrentUser,
                friendsController.getFriendInvitationsFromCurrentUser,
                userController.getUserById,
                userController.searchUsers,
                postsController.getUserPosts,
                postsController.getFeed,
                postsController.createPost,
                postsController.deletePost,
                postsController.putLike,
                postsController.deleteLike
        );
    }

    public void run() {
        logger.info("Application started!");
        port(9000);
        staticFiles.location("/static");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        corsEnabler.enableCORS("*");
        before("*", Filters.addTrailingSlashes);
        before(Filters.checkAuthorization);

        for (APIRoute route: getRoutes()) {
            switch (route.getMethod()) {
                case GET:
                    get(route.getPath(), route.getRoute(), jsonTransformer);
                    break;
                case POST:
                    post(route.getPath(), route.getRoute(), jsonTransformer);
                    break;
                case PUT:
                    put(route.getPath(), route.getRoute(), jsonTransformer);
                    break;
                case DELETE:
                    delete(route.getPath(), route.getRoute(), jsonTransformer);
                    break;
            }
        }

        after("*", Filters.addGzipHeader);
        logger.info("Routes and filters defined");
    }
}
