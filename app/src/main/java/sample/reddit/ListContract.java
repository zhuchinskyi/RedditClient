package sample.reddit;

import android.support.annotation.NonNull;

import java.util.List;

import sample.reddit.base.BaseState;
import sample.reddit.base.BaseStatefulPresenter;
import sample.reddit.base.BaseView;
import sample.reddit.model.ChildItem;
import sample.reddit.model.Preview;

interface ListContract {
    interface View extends BaseView {
        void addItems(@NonNull List<ChildItem> items);

        void showPreviewActivity(@NonNull String fileName, @NonNull String url);

        void setListPosition(int position);

        void showProgress(boolean isVisible);

        void showMessage(String msg);

        void showMessage(int resId);

    }

    interface State extends BaseState {
        List<ChildItem> getListItems();

        int getFirstVisibleItemPosition();

        String getTagLastSlice();
    }

    interface Presenter extends BaseStatefulPresenter<View, State> {
        void onListPositionChanged(int position);

        void onThumbnailClicked(Preview preview);

        void onLoad();
    }
}
