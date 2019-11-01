package by.deniskruglik.socialnetwork.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonBodyConverter {
    private static Gson gson = new Gson();

    public static Map<String, String> convertJsonBodyToMap(Request request) {
        String body = request.body();
        Type bodyParamsMapType = new TypeToken<Map<String, String>>() {}.getType();
        return gson.fromJson(body, bodyParamsMapType);
    }
}
