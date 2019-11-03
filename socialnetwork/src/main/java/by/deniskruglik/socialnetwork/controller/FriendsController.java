package by.deniskruglik.socialnetwork.controller;

import by.deniskruglik.socialnetwork.data.User;
import by.deniskruglik.socialnetwork.di.InjectLogger;
import by.deniskruglik.socialnetwork.exception.FriendException;
import by.deniskruglik.socialnetwork.exception.FriendInvitationException;
import by.deniskruglik.socialnetwork.service.UserService;
import by.deniskruglik.socialnetwork.utils.JsonBodyConverter;
import by.deniskruglik.socialnetwork.utils.ResponseUtils;
import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FriendsController {
    private final static String USER_KEY = "user";
    private final static String FRIEND_ID_KEY = "friendId";
    private final static String INVITATION_ID_KEY = "invitation_id";

    @InjectLogger
    private Logger logger;

    @Inject
    private UserService userService;

    public final APIRoute getUsersFriends = new APIRoute(RouteMethod.GET, Path.FRIENDS_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        Predicate<User> userPredicate = userFromList -> userFromList.getId() != user.getId();
        return userService.getFriendsForUser(user)
                .stream()
                .filter(userPredicate)
                .collect(Collectors.toList());
    });

    public final APIRoute sendFriendInvitation = new APIRoute(RouteMethod.POST, Path.FRIEND_INVITATIONS_URL, (request, response) -> {
        User sender = request.session().attribute(USER_KEY);
        Map<String, String> bodyParams = JsonBodyConverter.convertJsonBodyToMap(request);
        if (!bodyParams.containsKey(FRIEND_ID_KEY)) {
            return ResponseUtils.errorMessage(response, "Required parameters are missing");
        }
        String friendIdString = bodyParams.get(FRIEND_ID_KEY);
        int friendId;
        try {
            friendId = Integer.parseInt(friendIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request body");
        }
        try {
            return userService.sendFriendInvitation(friendId, sender);
        } catch (FriendInvitationException e) {
            return ResponseUtils.errorMessage(response, e.getMessage());
        }
    });

    public final APIRoute acceptFriendInvitation = new APIRoute(RouteMethod.POST, Path.ACCEPT_FRIEND_INVITATION_URL, (request, response) -> {
        User receiver = request.session().attribute(USER_KEY);
        Map<String, String> bodyParams = JsonBodyConverter.convertJsonBodyToMap(request);
        if (!bodyParams.containsKey(INVITATION_ID_KEY)) {
            return ResponseUtils.errorMessage(response, "Required parameters are missing");
        }
        String invitationIdString = bodyParams.get(INVITATION_ID_KEY);
        int invitationId;
        try {
            invitationId = Integer.parseInt(invitationIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request body");
        }
        try {
            userService.acceptFriendInvitation(invitationId, receiver);
            return ResponseUtils.successMessage(response, "Invitation successfully accepted");
        } catch (FriendInvitationException e) {
            return ResponseUtils.errorMessage(response, e.getMessage());
        }
    });

    public final APIRoute rejectFriendInvitation = new APIRoute(RouteMethod.POST, Path.REJECT_FRIEND_INVITATION_URL, (request, response) -> {
        User receiver = request.session().attribute(USER_KEY);
        Map<String, String> bodyParams = JsonBodyConverter.convertJsonBodyToMap(request);
        if (!bodyParams.containsKey(INVITATION_ID_KEY)) {
            return ResponseUtils.errorMessage(response, "Required parameters are missing");
        }
        String invitationIdString = bodyParams.get(INVITATION_ID_KEY);
        int invitationId;
        try {
            invitationId = Integer.parseInt(invitationIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request body");
        }
        try {
            userService.rejectFriendInvitation(invitationId, receiver);
            return ResponseUtils.successMessage(response, "Invitation successfully rejected");
        } catch (FriendInvitationException e) {
            return ResponseUtils.errorMessage(response, e.getMessage());
        }
    });

    public final APIRoute removeFriend = new APIRoute(RouteMethod.DELETE, Path.FRIEND_BY_ID_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        String friendIdString = request.params(FRIEND_ID_KEY);
        int friendId;
        try {
            friendId = Integer.parseInt(friendIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request query");
        }
        try {
            userService.removeFriend(friendId, user);
            return ResponseUtils.successMessage(response, "Friend successfully removed");
        } catch (FriendException e) {
            return ResponseUtils.errorMessage(response, e.getMessage());
        }
    });

    public final APIRoute getFriendInvitationsForCurrentUser = new APIRoute(RouteMethod.GET, Path.FRIEND_INVITATIONS_TO_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        return userService.getFriendInvitationsToUser(user);
    });

    public final APIRoute getFriendInvitationsFromCurrentUser = new APIRoute(RouteMethod.GET, Path.FRIEND_INVITATIONS_FROM_URL, (request, response) -> {
        User user = request.session().attribute(USER_KEY);
        return userService.getFriendInvitationsFromUser(user);
    });
}
