package android.cybereye_community.com.sayafit.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rezky Aulia Pratama on 9/6/2017.
 */

public class ApiResponse<T> implements Serializable {

    @SerializedName(value = "value")
    @Expose
    public int value;
    @SerializedName(value = "message")
    @Expose
    public String message;

    @SerializedName("ApiElapsed")
    @Expose
    public String ApiElapsed;
    @SerializedName("result")
    @Expose
    public List<T> ApiList=new ArrayList<>();

}
