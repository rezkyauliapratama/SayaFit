package android.cybereye_community.com.sayafit.handler.api;

import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.model.request.FeedPost;
import android.cybereye_community.com.sayafit.model.response.ApiResponse;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.google.gson.Gson;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class FeedApi {
    private final ApiClient api;
    final static String path = ApiClient.getInstance().BASE_URL.concat("feed/");
    final static String postPath = path.concat("post_feed_item_v29876.php");

    public FeedApi(ApiClient api) {
        this.api = api;
    }

    public ANRequest post(final FeedPost feed){
        Timber.e("PATH : "+postPath);
        Timber.e("FEED : "+new Gson().toJson(feed));
        return AndroidNetworking.post(postPath)
                .addApplicationJsonBody(feed) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build();
    }

    public class PostResponse extends ApiResponse<FeedPost> {

    }

}
