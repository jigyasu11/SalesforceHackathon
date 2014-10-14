package com.rarya.lmp.models;

import com.google.gson.Gson;

/**
 * Created by rarya on 10/11/14.
 */
public class User {

    String userId;

    Float latitude;

    Float longitude;

    String deviceId;

    String deviceName;

    Long battery;

    Long pingTime;

    private static final Gson gson = new Gson();
    public User(String userId, Float latitude, Float longitude, String deviceId, String deviceName, Long battery, Long pingTime) {

        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.battery = battery;
        this.pingTime = pingTime;

    }

    public String toJsonString() {
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Long getBattery() {
        return battery;
    }
}
