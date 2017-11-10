package sample.reddit.model;

import com.google.gson.annotations.SerializedName;

public class RedditResponse {
    @SerializedName("data")
    private Content content;

    public Content getData() {
        return content;
    }

    public void setData(Content content) {
        this.content = content;
    }


}
