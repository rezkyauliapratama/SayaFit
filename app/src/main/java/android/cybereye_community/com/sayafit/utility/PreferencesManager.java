package android.cybereye_community.com.sayafit.utility;

import android.content.Context;

import com.securepreferences.SecurePreferences;

/**
 * Created by Shiburagi on 09/07/2016.
 */
public class PreferencesManager {

    private static final String CURRENT_VERSION = "CURRENT_VERSION";
    private static final String FONT_SIZE_KEY = "FONT_SIZE_KEY";


    private static PreferencesManager instance;
    private final Context context;
    private final SecurePreferences preference;

    public static PreferencesManager init(Context context) {
        instance = new PreferencesManager(context);

        return instance;
    }

    public static PreferencesManager getInstance() {
        return instance;
    }

    PreferencesManager(Context context) {
        this.context = context;
        preference = new SecurePreferences(context);

    }


    public void setCurrentVersion(int version) {
        SecurePreferences.Editor editor = preference.edit();
        editor.putInt(CURRENT_VERSION, version);
        editor.apply();
    }

    public int getCurrentVersion() {
        return preference.getInt(CURRENT_VERSION, 1);
    }


    public void setFontSize(float scale) {
        SecurePreferences.Editor editor = preference.edit();
        editor.putFloat(FONT_SIZE_KEY, scale);
        editor.apply();
    }

    public float getFontSize() {
        return preference.getFloat(FONT_SIZE_KEY, 1.0f);
    }


}
