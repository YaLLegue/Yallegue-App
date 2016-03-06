package pibes.yallegue;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Edgar Salvador Maurilio on 05/03/2016.
 */
public class YaLlegueApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFacebookSDK();

        initParse();
    }

    private void initParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }


    private void initializeFacebookSDK() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
