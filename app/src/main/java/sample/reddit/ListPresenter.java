package sample.reddit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sample.reddit.model.ChildItem;
import sample.reddit.model.Image;
import sample.reddit.model.Preview;

import static sample.reddit.util.Constants.ITEMS_TOTAL;


public class ListPresenter implements ListContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    @Nullable
    private ListContract.View view;
    private List<ChildItem> listItems;
    private RedditInteractor redditInteractor;


    private int firstPosition;
    //    private int total;
    private String tagLastSlice;

    ListPresenter(RedditInteractor redditInteractor) {
        this.redditInteractor = redditInteractor;
        listItems = new ArrayList<>();
    }

    @Override
    public void subscribe(@NonNull ListContract.View view) {
        subscribe(view, null);
    }

    @Override
    public void subscribe(@NonNull ListContract.View view, @Nullable ListContract.State state) {
        this.view = view;
        if (state != null) {
            this.listItems = state.getListItems();
            this.tagLastSlice = state.getTagLastSlice();

            this.view.addItems(listItems);
            this.view.setListPosition(state.getFirstVisibleItemPosition());
        } else {
            onLoad();
        }
        Log.i(TAG, "subscribe: " + listItems.size());
    }

    @Override
    public ListState getState() {
        return new ListState(listItems, firstPosition, tagLastSlice);
    }

    @Override
    public void unsubscribe() {
        Log.i(TAG, "unsubscribe: " + listItems.size());
        listItems = new ArrayList<>();
        view = null;
        tagLastSlice = null;
        firstPosition = 0;
    }


    @Override
    public void onListPositionChanged(int position) {
        firstPosition = position;
    }

    @Override
    public void onThumbnailClicked(@Nullable Preview preview) {
        if (preview == null) {
            view.showMessage(R.string.nothing_show);
        } else {
            ArrayList<Image> images = preview.getImages();
            Image image = images.get(0);
            String url = image.getSource().getUrl();
            String id = image.getId();
            view.showPreviewActivity(id, url);
        }
    }


    public void onLoad() {
        if (tagLastSlice == null && !listItems.isEmpty()) {
            Log.i(TAG, "Nothing to load more, tag slice is null");
            return;
        }
        if (listItems.size() >= ITEMS_TOTAL) {
            Log.i(TAG, "The maximum number of elements has been uploaded");
            return;
        }
        view.showProgress(true);

        redditInteractor.loadItems(tagLastSlice, new RedditInteractor.Callback() {
            @Override
            public void onSuccess(String tagLastSlice, List<ChildItem> childrenItems) {
                Log.i(TAG, "onSuccess(), tagLastSlice: " + tagLastSlice);
                view.showProgress(false);

                listItems.addAll(childrenItems);
                int total = listItems.size();
                if (total >= ITEMS_TOTAL) {
                    view.showMessage(String.format("End is reached. Total: %s item(s)", total));
                }

                view.addItems(childrenItems);
                ListPresenter.this.tagLastSlice = tagLastSlice;

                Log.i(TAG, "Size: " + childrenItems.size() + ", Total: " + total);
            }

            @Override
            public void onFailure(String errorMsg) {
                Log.e(TAG, errorMsg);
                view.showProgress(false);
                view.showMessage(errorMsg);
            }
        });

    }
}
