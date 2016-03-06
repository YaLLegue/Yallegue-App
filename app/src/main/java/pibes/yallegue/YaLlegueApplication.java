package pibes.yallegue;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Edgar Salvador Maurilio on 05/03/2016.
 */
public class YaLlegueApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFacebookSDK();
    }


    private void initializeFacebookSDK() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
