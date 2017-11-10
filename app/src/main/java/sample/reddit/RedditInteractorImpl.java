package sample.reddit;

import android.util.Log;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sample.reddit.model.ChildItem;
import sample.reddit.model.RedditResponse;
import sample.reddit.network.RedditService;
import sample.reddit.util.Constants;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class RedditInteractorImpl implements RedditInteractor {
    private RedditService service;

    public RedditInteractorImpl() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RedditService.class);
    }

    public void loadItems(String tagLoadAfter, final RedditInteractor.Callback callback) {
        Call<RedditResponse> callCredentials = service.loadItems(Constants.ITEMS_PER_REQUEST, tagLoadAfter);
        callCredentials.enqueue(new retrofit2.Callback<RedditResponse>() {
            @Override
            public void onResponse(Call<RedditResponse> call, Response<RedditResponse> response) {
                Log.i(TAG, response.body().toString());

                List<ChildItem> childrenItems = response.body().getData().getChildren();
                String tagAfter = response.body().getData().getAfter();
                callback.onSuccess(tagAfter, childrenItems);
            }

            @Override
            public void onFailure(Call<RedditResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());

                callback.onFailure(t.getMessage());
            }
        });
    }


}
