package sample.reddit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sample.reddit.model.ChildItem;
import sample.reddit.model.Preview;
import sample.reddit.util.EndlessScrollListener;

import static sample.reddit.util.Constants.FILE_NAME_KEY;
import static sample.reddit.util.Constants.SAVE_STATE_KEY;
import static sample.reddit.util.Constants.URL_KEY;

public class MainActivity extends AppCompatActivity implements ListContract.View, RedditAdapter.EventListener {
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    RedditAdapter redditAdapter;
    LinearLayoutManager linearLayoutManager;

    ListPresenter listPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        redditAdapter = new RedditAdapter(this, this);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(redditAdapter);
        EndlessScrollListener scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore");
                listPresenter.onLoad();
            }

            @Override
            public void onLastVisibleItemPosition(int position) {
                listPresenter.onListPositionChanged(position);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        listPresenter = new ListPresenter(new RedditInteractorImpl());
        listPresenter.subscribe(this, savedInstanceState != null ? readFromBundle(savedInstanceState) : null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        writeToBundle(outState, listPresenter.getState());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listPresenter.unsubscribe();
    }

    @Override
    public void addItems(@NonNull List<ChildItem> items) {
        redditAdapter.addItems(items);
    }

    @Override
    public void showPreviewActivity(@NonNull String fileName, @NonNull String url) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra(FILE_NAME_KEY, fileName);
        intent.putExtra(URL_KEY, url);
        startActivity(intent);
    }

    @Override
    public void setListPosition(int position) {
        Log.i(TAG, "setListPosition: " + position);
        linearLayoutManager.scrollToPosition(position);
    }

    @Override
    public void showProgress(boolean isVisible) {
        progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        showMsg(msg);
    }

    @Override
    public void showMessage(int strResId) {
        showMsg(getString(strResId));
    }

    @Override
    public void onThumbnailClicked(Preview preview) {
        listPresenter.onThumbnailClicked(preview);
    }

    private void writeToBundle(Bundle bundle, ListState state) {
        bundle.putParcelable(SAVE_STATE_KEY, state);
    }

    private ListState readFromBundle(Bundle bundle) {
        return bundle.getParcelable(SAVE_STATE_KEY);
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
