package by.deniskruglik.queuemessagingsender.utils;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override
    public String render(Object o) {
        return gson.toJson(o);
    }
}
