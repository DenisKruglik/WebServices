package by.deniskruglik.socialnetwork.utils.contentfilters;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LinkWrapperTest {
    private LinkWrapper linkWrapper = new LinkWrapper();

    @Test(dataProvider = "links")
    public void shouldReturnContentWithWrapperLinks(String input, String expected) {
        String result = linkWrapper.filter(input);
        Assert.assertEquals(result, expected);
    }

    @DataProvider(name = "links")
    public Object[][] provideCases() {
        return new Object[][] {
                new Object[] {"Please visit www.google.com", "Please visit <a href=\"www.google.com\">www.google.com</a>"},
                new Object[] {
                        "Here's my catalog page http://ali.com/catalog/?price_from=100&price_to=400",
                        "Here's my catalog page <a href=\"http://ali.com/catalog/?price_from=100&price_to=400\">http://ali.com/catalog/?price_from=100&price_to=400</a>"
                },
                new Object[] {
                        "Read this section github.io/#requirements",
                        "Read this section <a href=\"github.io/#requirements\">github.io/#requirements</a>"
                }
        };
    }
}
