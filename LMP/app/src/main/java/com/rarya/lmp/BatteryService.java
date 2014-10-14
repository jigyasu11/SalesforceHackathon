package com.rarya.lmp;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.rarya.lmp.http.AsyncResponse;
import com.rarya.lmp.http.DoPost;
import com.rarya.lmp.models.User;

/**
 * Created by rarya on 10/10/14.
 */
public class BatteryService extends Service implements AsyncResponse {

    private static final String ACTION_STRING_SERVICE = "BatteryService";
    private static final String ACTION_STRING_ACTIVITY = "MapsActivity";
    private Intent batteryStatus;

    private String androidId;
    private static final String model = Build.MODEL;
    private static final String brand = Build.BRAND;


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(ACTION_STRING_SERVICE, "ON CREATE OF BATTERY");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null, ifilter);
        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()

        // call after every 10 min to get battery statistics

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        Double batteryPct = (level*1.0 / scale)*100.0;
        sendBroadcast(batteryPct.longValue());

        //stopSelf();

        return Service.START_STICKY;
    }

    //send broadcast from activity to all receivers listening to the action "ACTION_STRING_ACTIVITY"
    private void sendBroadcast(Long batterPct) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("LAT_LONG", MODE_PRIVATE);



        float latitude = sharedPref.getFloat(getString(R.string.saved_latitude), (float) 0.0);
        float longitude = sharedPref.getFloat(getString(R.string.saved_longitude), (float) 0.0);
        User location = new User(androidId, latitude, longitude, androidId, model, batterPct, System.currentTimeMillis());
        DoPost post = new DoPost(this);
        post.execute(location.toJsonString());

        Toast.makeText(getApplicationContext(), "Posting data to server", Toast.LENGTH_SHORT).show();
        Log.i(ACTION_STRING_SERVICE, "Sending message BATTERY  " + latitude + " " + longitude);


        Intent new_intent = new Intent();
        new_intent.putExtra("battery", batterPct);
        new_intent.setAction(ACTION_STRING_ACTIVITY);


        sendBroadcast(new_intent);
    }

    @Override
    public void onDestroy() {
        // I want to restart this service again in one hour
        super.onDestroy();
//        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarm.set(
//                alarm.RTC_WAKEUP,
//                System.currentTimeMillis() + (1000 * 1 * 60),
//                PendingIntent.getService(this, 0, new Intent(this, BatteryService.class), 0)
//        );
    }

    @Override
    public void processFinish(String output) {
        Log.i(ACTION_STRING_ACTIVITY, "Successfully posted data " + output);
    }

}
