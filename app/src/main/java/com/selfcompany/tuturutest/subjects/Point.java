package com.selfcompany.tuturutest.subjects;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Sergey on 04.09.2016.
 */
public class Point extends RealmObject {
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    @SerializedName("longitude")
    private float longitude;
    @SerializedName("latitude")
    private float latitude;

    public Point(){ }

    public void setLongitude(float longitude){
        this.longitude = longitude;
    }
    public float getLongitude(){
        return this.longitude;
    }
    public void setLatitude(float latitude){
        this.latitude = latitude;
    }
    public float getLatitude(){
        return this.latitude;
    }
}
