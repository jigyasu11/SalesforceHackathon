package com.rarya.lmp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.rarya.lmp.http.AsyncResponse;
import com.rarya.lmp.http.HttpCalls;
import com.rarya.lmp.models.User;
import com.rarya.lmp.models.UserList;

import org.json.JSONObject;

import utils.LocationUtils;

public class MapsActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, AsyncResponse {

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    private LatLng userClickedLocation;
    private GoogleMap map;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final String ACTION_STRING_ACTIVITY = "MapsActivity";
    private String androidId;
    private static final String model = Build.MODEL;
    private static final String brand = Build.BRAND;


    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private ProgressDialog pDialog;

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Long battery = intent.getLongExtra("battery", 0);
            //brand = verizon

            //SharedPreferences sharedPref = MapsActivity.this.getPreferences(Context.MODE_PRIVATE);

            //float latitude = sharedPref.getFloat(getString(R.string.saved_latitude), (float) 0.0);
            //float longitude = sharedPref.getFloat(getString(R.string.saved_longitude), (float) 0.0);

            //User location = new User(androidId, latitude, longitude, androidId, model, battery, System.currentTimeMillis());
            //DoPost post = new DoPost(MapsActivity.this);
            //post.execute(location.toJsonString());
            Log.i(ACTION_STRING_ACTIVITY, "GOT THE MESSAGE");
        }
    };


    /*
     * Initialize the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pDialog = new ProgressDialog(this);

         androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (model != null) {
            pDialog.setMessage("Checking how many users use your phone " + model);
            pDialog.show();
            final Gson gson = new Gson();
            JsonObjectRequest userFeedReq = new JsonObjectRequest(HttpCalls.base_url+ HttpCalls.location + "?device=" + model + "&user=" + androidId,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Parsing json
                            final UserList userFeedList = new Gson().fromJson(response.toString(), UserList.class);
                            for (User user: userFeedList.getUsers()) {
                                //Log.i(TAG, "SHLD NOT BE NULL " + user.getLatitude() + " "  + user.getLongitude());
                                LatLng userLoc = new LatLng(user.getLatitude(), user.getLongitude());
                                map.addMarker(new MarkerOptions()
                                        .title("***")
                                        .snippet(user.getDeviceName())
                                        .position(userLoc));

                            }
                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            pDialog.hide();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error occoured: " + error.getMessage());
                    Log.e(TAG, "Error occoured", error.getCause());
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(userFeedReq);
        }

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);

        Intent alarmIntent = new Intent(this, BatteryService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        startAlarm();

        if (activityReceiver != null) {
            //Create an intent filter to listen to the broadcast sent with the action "ACTION_STRING_ACTIVITY"
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_ACTIVITY);
            //Map the intent filter to the receiver
            registerReceiver(activityReceiver, intentFilter);
        }
        Intent intent = new Intent(this, BatteryService.class);
        startService(intent);

    }

    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();

        super.onStop();
    }
    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {

        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();

    }
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.resolved));
                        break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));

                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                Log.d(LocationUtils.APPTAG,
                        getString(R.string.unknown_activity_request_code, requestCode));

                break;
        }
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
            }
            return false;
        }
    }

    /**
     * Invoked by the "Get Location" button
     * Calls getLastLocation() to get the current location
     */
    public Location getLocation() {

        // If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();
            return currentLocation;
        }
        return null;
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {

        // Get a handle to the Map Fragment

        Location loc = getLocation();

        if (loc != null) {
            // Get all users with around current location
            LatLng currLoc = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());


           // SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("LAT_LONG", MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat(getString(R.string.saved_latitude), (float) loc.getLatitude());
            editor.putFloat(getString(R.string.saved_longitude), (float) loc.getLongitude());
            editor.commit();

            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 13));

            map.addMarker(new MarkerOptions()
                    .title("****")
                    .snippet("My phone!")
                    .position(currLoc));
        } else {
            Log.i(TAG, "Null location received");
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }

    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    @Override
    public void processFinish(String output) {
        Log.i(ACTION_STRING_ACTIVITY, "Successfully posted data " + output);
    }

    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 900000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm() {
        if (manager != null) {
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTION_STRING_ACTIVITY, "ON DESTROY CALLED");
        //STEP3: Unregister the receiver
        unregisterReceiver(activityReceiver);
        activityReceiver = null;
        //cancelAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_home:
                return true;
            case R.id.menu_graph:
                Intent intent = new Intent(getBaseContext(), GraphActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
