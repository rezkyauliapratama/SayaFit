package android.cybereye_community.com.sayafit.utility;

import android.text.Spannable;

/**
 * Created by Shiburagi on 21/11/2016.
 */
public interface OnFoundListener {
    void onFound(Spannable spannable, int start, int end, int openTag, int closeTag);
}
