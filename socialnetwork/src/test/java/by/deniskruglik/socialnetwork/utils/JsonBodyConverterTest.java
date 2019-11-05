package by.deniskruglik.socialnetwork.utils;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

public class JsonBodyConverterTest {
    @Test
    public void shouldReturnCorrectMapAfterConverting() {
        String input = "{\"key\": \"value\"}";
        Map<String, String> expected = new HashMap<>();
        expected.put("key", "value");
        Request request = Mockito.mock(Request.class);
        Mockito.when(request.body()).thenReturn(input);
        Map<String, String> result = JsonBodyConverter.convertJsonBodyToMap(request);
        Assert.assertEquals(result, expected);
    }
}
