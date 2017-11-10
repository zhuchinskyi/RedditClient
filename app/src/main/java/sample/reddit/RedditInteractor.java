package sample.reddit;

import java.util.List;

import sample.reddit.model.ChildItem;

public interface RedditInteractor {

    void loadItems(String tagLoadAfter, final Callback callback);

    interface Callback {
        void onSuccess(String tagAfter, List<ChildItem> items);

        void onFailure(String errorMsg);
    }
}
