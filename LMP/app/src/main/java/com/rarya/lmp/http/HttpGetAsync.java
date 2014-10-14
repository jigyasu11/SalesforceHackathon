package com.rarya.lmp.http;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by rarya on 10/12/14.
 */
public class HttpGetAsync extends AsyncTask<String, Void, String> {

    private AsyncResponse responseListener;
    private static final String TAG = DoPost.class.getSimpleName();

    public HttpGetAsync(AsyncResponse responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected String doInBackground(String... params) {
        return doGet(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        responseListener.processFinish(result);
    }

    private String doGet(String url) {
        try {
            Log.i(TAG, "GET CALL url is " + url);
            HttpCalls calls = new HttpCalls();
            return calls.doGet(url);
        } catch (Exception e) {
            Log.e(TAG, "Error in GET", e);
        }
        return null;
    }
}
