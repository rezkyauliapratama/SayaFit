package android.cybereye_community.com.sayafit.handler.api;

import android.cybereye_community.com.sayafit.controller.database.entity.UserTbl;
import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.model.response.ApiResponse;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.google.gson.Gson;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class UserApi {
    private final ApiClient api;
    final static String path = ApiClient.getInstance().BASE_URL.concat("register_google_sign_in_api.php");

    public UserApi(ApiClient api) {
        this.api = api;
    }

    public ANRequest post(final UserTbl user){
        Timber.e("PATH : "+path);
        Timber.e("USER : "+new Gson().toJson(user));
        return AndroidNetworking.post(path)
                .addBodyParameter(user) // posting java object
//                .setTag("test")
                .setPriority(Priority.HIGH)
                .build();
    }

    public class Response extends ApiResponse<UserTbl> {


    }

}
