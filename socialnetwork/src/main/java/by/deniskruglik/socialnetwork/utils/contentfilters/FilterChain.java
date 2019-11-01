package by.deniskruglik.socialnetwork.utils.contentfilters;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    private List<ContentFilter> filters = new ArrayList<>();

    public void setFilters(List<ContentFilter> filters) {
        this.filters = filters;
    }

    public List<ContentFilter> getFilters() {
        return filters;
    }

    public void addFilter(ContentFilter filter) {
        filters.add(filter);
    }

    public void removeFilter(ContentFilter filter) {
        filters.remove(filter);
    }

    public void clearFilters() {
        filters.clear();
    }

    public String filter(String content) {
        for (ContentFilter filter: filters) {
            content = filter.filter(content);
        }
        return content;
    }
}
