package by.deniskruglik.socialnetwork.controller;

import by.deniskruglik.socialnetwork.utils.StatusCode;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class Filters {
    public final static Filter addTrailingSlashes = (Request request, Response response) -> {
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };

    public final static Filter addGzipHeader = (Request request, Response response) -> {
        response.header("Content-Encoding", "gzip");
    };

    public final static Filter checkAuthorization = (Request request, Response response) -> {
        String path = request.pathInfo();
        if (!path.startsWith("/auth/") && request.session().attribute("user") == null) {
            halt(StatusCode.FORBIDDEN);
        }
    };
}
