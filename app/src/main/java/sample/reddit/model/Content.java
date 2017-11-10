package sample.reddit.model;

import java.util.List;

public class Content {
    private List<ChildItem> children;
    private String after;

    public String getAfter() {
        return after;
    }

    public List<ChildItem> getChildren() {
        return children;
    }
}
