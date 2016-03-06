package pibes.yallegue.receive;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

import pibes.yallegue.home.HomeActivity;

/**
 * Created by Edgar Salvador Maurilio on 06/03/2016.
 */
public class PushNotificationApp extends ParsePushBroadcastReceiver {

    public static final String EXTRA_START = "start";
    public static final String START_GAME = "start_game";

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);

        Intent myIntent = new Intent(context, HomeActivity.class);
        myIntent.putExtra(EXTRA_START, START_GAME);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(myIntent);

    }
}
