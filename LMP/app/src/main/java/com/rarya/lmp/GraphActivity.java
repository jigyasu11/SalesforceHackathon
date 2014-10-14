package com.rarya.lmp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.gson.Gson;
import com.rarya.lmp.http.AsyncResponse;
import com.rarya.lmp.http.HttpCalls;
import com.rarya.lmp.http.HttpGetAsync;
import com.rarya.lmp.models.User;
import com.rarya.lmp.models.UserList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rarya on 10/11/14.
 */
/**
 * A straightforward example of using AndroidPlot to plot some data.
 */
public class GraphActivity extends Activity implements AsyncResponse
{

    private XYPlot plot;
    private ProgressDialog pDialog;
    private static final String TAG = GraphActivity.class.getSimpleName();
    private static Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // fun little snippet that prevents users from taking screenshots
        // on ICS+ devices :-)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.graph);

        // make two http call to get batter stats
        pDialog.setMessage("Comparing battery ");
        pDialog.show();



        List<Long> userBattery = new ArrayList<Long>();
        List<Long> juntaBattery = new ArrayList<Long>();

        try {
            HttpGetAsync getAsync1 = new HttpGetAsync(this);
            HttpGetAsync getAsync2 = new HttpGetAsync(this);

            String userData = getAsync1.execute(HttpCalls.userInfo + "?user=" + androidId + "&device=" + androidId).get();
            Log.i(TAG, "USER DATA " + userData);

            String juntaData = getAsync2.execute(HttpCalls.otherUsers + "?user=" + androidId).get();
            Log.i(TAG, "JUNTA DATA " + juntaData);
            UserList users = gson.fromJson(userData, UserList.class);
            UserList junta = gson.fromJson(juntaData, UserList.class);
            for (User user: users.getUsers()) {
              userBattery.add(user.getBattery());
            }
            for (User user: junta.getUsers()) {
                juntaBattery.add(user.getBattery());
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception in get data " + e);
        }

        pDialog.hide();


//        final List<Long> userBattery = new ArrayList<Long>();
//        JsonObjectRequest userFeedReq = new JsonObjectRequest(HttpCalls.base_url+ HttpCalls.userInfo + "?user=" + androidId +
//                "&device="+ androidId,null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "userfeedactivity other got back data " + response.toString());
//                        // Parsing json
//                        final UserList userFeedList = new Gson().fromJson(response.toString(), UserList.class);
//                        for (User user: userFeedList.getUsers()) {
//                            userBattery.add(user.getBattery());
//                        }
//                  }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error occoured: " + error.getMessage());
//                Log.e(TAG, "Error occoured", error.getCause());
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(userFeedReq);
//
//
//        final List<Long> juntaBattery = new ArrayList<Long>();
//        JsonObjectRequest juntaFeedReq = new JsonObjectRequest(HttpCalls.base_url+ HttpCalls.otherUsers + "?user=" + androidId,null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "juntafeedactivity got back data " + response.toString());
//                        // Parsing json
//                        final UserList userFeedList = new Gson().fromJson(response.toString(), UserList.class);
//                        for (User user: userFeedList.getUsers()) {
//                            juntaBattery.add(user.getBattery());
//                        }
//                        pDialog.hide();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error occoured: " + error.getMessage());
//                Log.e(TAG, "Error occoured", error.getCause());
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(juntaFeedReq);

        // do the plotting
        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

        // Create a couple arrays of y-values to plot:
        //Number[] series1Numbers = userBattery.toArray();
        //Number[] series2Numbers = {4, 6, 3, 8, 2, 10};

        // Turn the above arrays into XYSeries':
        Log.i(TAG, "SERIES 1 " + userBattery.size() + userBattery.toString());
        Log.i(TAG, "SERIES 2 " + juntaBattery.size() + " " + juntaBattery.toString());

        int size = (userBattery.size()>juntaBattery.size()) ? juntaBattery.size() : userBattery.size();

        XYSeries series1 = new SimpleXYSeries(
                userBattery.subList(0, size),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "UserConsumption");                             // Set the display title of the series

        // same as above
        XYSeries series2 = new SimpleXYSeries(
                juntaBattery.subList(0, size),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "AvgConsumption");

        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf1);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        // same as above:
        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf2);
        plot.addSeries(series2, series2Format);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);

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
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_graph:
                return true;
            case R.id.logout:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void processFinish(String output) {
        // Do something here

    }
}
