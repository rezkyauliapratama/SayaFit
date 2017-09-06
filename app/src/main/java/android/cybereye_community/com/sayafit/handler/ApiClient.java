package android.cybereye_community.com.sayafit.handler;

import android.cybereye_community.com.sayafit.handler.api.FeedApi;
import android.cybereye_community.com.sayafit.handler.api.GuideApi;
import android.cybereye_community.com.sayafit.handler.api.UserApi;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 7/1/2017.
 */

public class ApiClient {
    private static ApiClient mInstance;
    // Step 1: private static variable of INSTANCE variable
    private static volatile ApiClient INSTANCE;

    // Step 2: private constructor
    private ApiClient() {
        user = new UserApi(this);
        guide = new GuideApi(this);
        feed = new FeedApi(this);

    }

    // Step 3: Provide public static getInstance() method returning INSTANCE after checking
    public static ApiClient getInstance() {

        // double-checking lock
        if(null == INSTANCE){

            // synchronized block
            synchronized (ApiClient.class) {
                if(null == INSTANCE){
                    INSTANCE = new ApiClient();
                }
            }
        }
        return INSTANCE;
    }
    //b77a9c9af1b4434dcbbacdde72879e7c

    //api list
    private final UserApi user;
    public UserApi user() {
        return user;
    }

    private final FeedApi feed;
    public FeedApi feed() {
        return feed;
    }

    private final GuideApi guide;
    public GuideApi guide() {
        return guide;
    }



}
