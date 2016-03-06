package pibes.yallegue.preference;

import android.content.Context;

/**
 * Created by Edgar Salvador Maurilio on 01/12/2015.
 */
public class AppPreferences {

    private static AppPreferences INSTANCE;
    private PreferencesHelper preferencesHelper;

    private AppPreferences(Context context) {
        preferencesHelper = new PreferencesHelper(context, PrefsConstants.PREFS_NAME);
    }

    public static synchronized AppPreferences getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPreferences(context);
        }
        return INSTANCE;
    }

    public void saveLogin(boolean isLogin) {
        preferencesHelper.applyPreference(PrefsConstants.IS_LOGIN, isLogin);

    }

    public boolean isLongin() {
        return preferencesHelper.getBoolean(PrefsConstants.IS_LOGIN, false);
    }

    public void removePreference() {
        preferencesHelper.clearPreferences();
    }


}
