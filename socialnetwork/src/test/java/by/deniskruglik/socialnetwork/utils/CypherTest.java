package by.deniskruglik.socialnetwork.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CypherTest {
    @Test
    public void shouldReturnValidMD5Hash() {
//        given
        String input = "Hello world";
        String expectedHash = "3E25960A79DBC69B674CD4EC67A72C62";
//        when
        String result = Cypher.encode(input);
//        then
        Assert.assertEquals(result, expectedHash);
    }
}
