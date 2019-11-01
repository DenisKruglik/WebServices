package by.deniskruglik.socialnetwork.utils;

import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {
    @Inject
    private Gson gson;

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
