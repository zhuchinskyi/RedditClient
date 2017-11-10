package sample.reddit.base;

import android.support.annotation.NonNull;


public interface BasePresenter<V extends BaseView> {
    void subscribe(@NonNull V view);

    void unsubscribe();
}
