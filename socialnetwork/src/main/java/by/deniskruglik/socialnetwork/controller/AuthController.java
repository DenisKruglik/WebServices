package by.deniskruglik.socialnetwork.controller;

import by.deniskruglik.socialnetwork.data.User;
import by.deniskruglik.socialnetwork.di.InjectLogger;
import by.deniskruglik.socialnetwork.exception.LoginException;
import by.deniskruglik.socialnetwork.exception.RegistrationException;
import by.deniskruglik.socialnetwork.service.UserService;
import by.deniskruglik.socialnetwork.utils.JsonBodyConverter;
import by.deniskruglik.socialnetwork.utils.StatusCode;
import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import spark.Session;

import java.util.HashMap;
import java.util.Map;

public class AuthController {
    private final static String LOGIN_PARAMETER = "login";
    private final static String PASSWORD_PARAMETER = "password";
    private final static String REPEATED_PASSWORD_PARAMETER = "repeatedPassword";

    @Inject
    private UserService userService;

    @InjectLogger
    private Logger logger;

    public final APIRoute login = new APIRoute(RouteMethod.POST, Path.LOGIN_URL, (request, response) -> {
        Map<String, String> bodyParams = JsonBodyConverter.convertJsonBodyToMap(request);
        if (!bodyParams.containsKey(LOGIN_PARAMETER) || !bodyParams.containsKey(PASSWORD_PARAMETER)) {
            response.status(StatusCode.BAD_REQUEST);
            Map<String, String> result = new HashMap<>();
            result.put("message", "No required parameters provided");
            return result;
        }
        try {
            User user = userService.login(bodyParams.get(LOGIN_PARAMETER), bodyParams.get(PASSWORD_PARAMETER));
            Session session = request.session();
            session.attribute("user", user);
            response.status(StatusCode.OK);
            return user;
        } catch (LoginException e) {
            logger.catching(e);
            response.status(StatusCode.BAD_REQUEST);
            Map<String, String> result = new HashMap<>();
            result.put("message", e.getMessage());
            return result;
        }
    });

    public final APIRoute register = new APIRoute(RouteMethod.POST, Path.REGISTER_URL, (request, response) -> {
        Map<String, String> bodyParams = JsonBodyConverter.convertJsonBodyToMap(request);
        if (!bodyParams.containsKey(LOGIN_PARAMETER) || !bodyParams.containsKey(PASSWORD_PARAMETER)
                || !bodyParams.containsKey(REPEATED_PASSWORD_PARAMETER)) {
            response.status(StatusCode.BAD_REQUEST);
            Map<String, String> result = new HashMap<>();
            result.put("message", "No required parameters provided");
            return result;
        }
        try {
            User user = userService.register(bodyParams.get(LOGIN_PARAMETER), bodyParams.get(PASSWORD_PARAMETER),
                    bodyParams.get(REPEATED_PASSWORD_PARAMETER));
            Session session = request.session();
            session.attribute("user", user);
            response.status(StatusCode.CREATED);
            return user;
        } catch (RegistrationException e) {
            response.status(StatusCode.BAD_REQUEST);
            Map<String, String> result = new HashMap<>();
            result.put("message", e.getMessage());
            return result;
        }
    });

    public final APIRoute logout = new APIRoute(RouteMethod.GET, Path.LOGOUT_URL, (request, response) -> {
        request.session().invalidate();
        response.status(StatusCode.OK);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Logged out successfully");
        return result;
    });

    public final APIRoute getCurrentUser = new APIRoute(RouteMethod.GET, Path.CURRENT_USER_URL,
            (request, response) -> request.session().attribute("user"));
}
