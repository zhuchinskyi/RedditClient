package sample.reddit.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sample.reddit.model.RedditResponse;

public interface RedditService {
    @GET("/top.json")
    Call<RedditResponse> loadItems(@Query("limit") int limit, @Query("after") String after);
}
