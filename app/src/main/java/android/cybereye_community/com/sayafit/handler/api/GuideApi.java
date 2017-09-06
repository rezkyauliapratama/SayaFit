package android.cybereye_community.com.sayafit.handler.api;

import android.cybereye_community.com.sayafit.controller.database.entity.FeedTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.GuideTbl;
import android.cybereye_community.com.sayafit.handler.ApiClient;
import android.cybereye_community.com.sayafit.model.response.ApiResponse;
import android.cybereye_community.com.sayafit.utility.Constant;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 9/7/2017.
 */

public class GuideApi  {
    private final ApiClient api;
    final  String path = Constant.getInstance().BASE_URL.concat("feed/");
    final String getPath = path.concat("feed_item_v289967.php");

    public GuideApi(ApiClient api) {
        this.api = api;
    }

    public ANRequest get(final int page){
        Timber.e("PATH : "+getPath+"/"+page);
        Timber.e("Page : "+page);
        return AndroidNetworking.get(getPath+"/"+page)
                .addQueryParameter("page",page+"")
                .setPriority(Priority.HIGH)
                .build();
    }

    public class GetResponse extends ApiResponse<GuideTbl> {

    }

}
