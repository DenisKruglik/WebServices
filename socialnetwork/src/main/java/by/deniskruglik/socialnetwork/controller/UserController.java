package by.deniskruglik.socialnetwork.controller;

import by.deniskruglik.socialnetwork.data.User;
import by.deniskruglik.socialnetwork.exception.UserServiceException;
import by.deniskruglik.socialnetwork.service.UserService;
import by.deniskruglik.socialnetwork.utils.ResponseUtils;
import com.google.inject.Inject;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserController {
    private final static String ID_PARAM = ":id";
    private final static String QUERY_PARAM = ":query";
    private final static String PAGE_PARAM = "page";
    private final static String USER_KEY = "user";

    @Inject
    private UserService userService;

    public final APIRoute getUserById = new APIRoute(RouteMethod.GET, Path.GET_USER_BY_ID_URL, (request, response) -> {
        String userIdString = request.params(ID_PARAM);
        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            return ResponseUtils.errorMessage(response, "Invalid request query");
        }
        try {
            return userService.getUserById(userId);
        } catch (UserServiceException e) {
            return ResponseUtils.errorMessage(response,"No user with such id");
        }
    });

    public final APIRoute searchUsers = new APIRoute(RouteMethod.GET, Path.SEARCH_USERS_URL, (request, response) -> {
        String query = request.params(QUERY_PARAM);
        String pageString = request.queryParams(PAGE_PARAM);
        int page = 0;
        if (pageString != null) {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException e) {
                return ResponseUtils.errorMessage(response, "Invalid request query");
            }
        }
        User user = request.session().attribute(USER_KEY);
        Predicate<User> userPredicate = userFromList -> userFromList.getId() != user.getId();
        return userService.searchUsers(query, page)
                .stream()
                .filter(userPredicate)
                .collect(Collectors.toList());
    });
}
