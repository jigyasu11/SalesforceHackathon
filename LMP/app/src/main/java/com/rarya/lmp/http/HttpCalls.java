package com.rarya.lmp.http;

/**
 * Created by rarya on 10/11/14.
 */

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import java.io.ByteArrayOutputStream;

/**
 * Created by rarya on 9/27/14.
 */
public class HttpCalls {

    public static final String base_url = "http://secret-temple-2008.herokuapp.com";

    public static final String otherUsers = "/otherUsers";

    public static final String userInfo = "/userInfo";

    public static final String location = "/location";

    public static final String save = "/save";

    private static final String TAG = HttpCalls.class.getSimpleName();


    public String doPost(String url, String payload) throws Exception {
        Log.i(TAG, "making a post call");
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(payload, HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        return executeRequest(httpPost);
    }


    public String doGet(String end_point) {
        try {
            return executeRequest(new HttpGet(this.base_url + end_point));
        } catch (Exception e) {
            Log.e(TAG, "Error in GET", e);
        }
        return null;
    }

    private String executeRequest(HttpRequestBase request) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(request);
        StatusLine statusLine = response.getStatusLine();
        Log.i(TAG, "RESPONSE IS " + statusLine);
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String responseString = out.toString();
            return responseString;
        } else {
            response.getEntity().getContent().close();
        }
        return null;
    }
}
