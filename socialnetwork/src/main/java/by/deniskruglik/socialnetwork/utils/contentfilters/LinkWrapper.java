package by.deniskruglik.socialnetwork.utils.contentfilters;

public class LinkWrapper implements ContentFilter {
    private final static String LINK_REGEX = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    @Override
    public String filter(String content) {
        return content.replaceAll(LINK_REGEX, "<a href=\"$0\">$0</a>");
    }
}
