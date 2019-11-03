package by.deniskruglik.socialnetwork.controller;

import spark.Route;

public class APIRoute {
    private RouteMethod method;

    private String path;

    private Route route;

    public APIRoute(RouteMethod method, String path, Route route) {
        this.method = method;
        this.path = path;
        this.route = route;
    }

    public APIRoute() {
    }

    public RouteMethod getMethod() {
        return method;
    }

    public void setMethod(RouteMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
