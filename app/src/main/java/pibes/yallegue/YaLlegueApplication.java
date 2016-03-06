package pibes.yallegue;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;

import pibes.yallegue.data.DataFactory;
import pibes.yallegue.data.DataService;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Edgar Salvador Maurilio on 05/03/2016.
 */
public class YaLlegueApplication extends Application {

    private DataService mDataService;
    private Scheduler mScheduler;

    private static YaLlegueApplication get(Context context) {
        return (YaLlegueApplication) context.getApplicationContext();
    }

    public static YaLlegueApplication create(Context context) {
        return YaLlegueApplication.get(context);
    }

    public DataService getDataService() {
        if (mDataService == null)
            mDataService = DataFactory.create();

        return mDataService;
    }

    public Scheduler subscribeScheduler() {
        if (mScheduler == null)
            mScheduler = Schedulers.io();

        return mScheduler;
    }


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
