package com.rarya.lmp.http;

import android.os.AsyncTask;
import android.util.Log;



/**
 * Created by rarya on 9/28/14.
 */
public class DoPost extends AsyncTask<String, Void, String> {

    private AsyncResponse responseListener;
    private static final String TAG = DoPost.class.getSimpleName();

    public DoPost(AsyncResponse responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected String doInBackground(String... params) {
        return doPost(HttpCalls.base_url + HttpCalls.save, params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        responseListener.processFinish(result);
    }

    private String doPost(String url, String payload) {
        try {
            Log.i(TAG, "payload " + payload + " url is " + url);
            HttpCalls calls = new HttpCalls();
            return calls.doPost(url, payload);
        } catch (Exception e) {
            Log.e(TAG, "Error in POST", e);
        }
        return null;
    }
}
