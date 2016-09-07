package com.selfcompany.tuturutest.subjects;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sergey on 04.09.2016.
 */
public class Stations extends RealmObject {
    public static final String COUNTRY_TITLE = "countryTitle";
    public static final String POINT = "point";
    public static final String DISTRICT_TITLE = "districtTitle";
    public static final String CITY_ID = "cityId";
    public static final String CITY_TITLE = "cityTitle";
    public static final String REGION_TITLE = "regionTitle";
    public static final String STATION_ID = "stationId";
    public static final String STATION_TITLE = "stationTitle";

    @SerializedName("countryTitle")
    private String countryTitle;
    @SerializedName("point")
    private Point point;
    @SerializedName("districtTitle")
    private String districtTitle;
    @SerializedName("cityId")
    private int cityId;
    @SerializedName("cityTitle")
    private String cityTitle;
    @SerializedName("regionTitle")
    private String regionTitle;
    @SerializedName("stationId")
    private int stationId;
    @SerializedName("stationTitle")
    private String stationTitle;

    public Stations(){ }

    public void setCountryTitle(String countryTitle){
        this.countryTitle = countryTitle;
    }
    public String getCountryTitle(){
        return this.countryTitle;
    }
    public void setPoint(Point point){
        this.point = point;
    }
    public Point getPoint(){
        return this.point;
    }
    public void setDistrictTitle(String districtTitle){
        this.districtTitle = districtTitle;
    }
    public String getDistrictTitle(){
        return this.districtTitle;
    }
    public void setCityId(int cityId){
        this.cityId = cityId;
    }
    public int getCityId(){
        return this.cityId;
    }
    public void setCityTitle(String cityTitle){
        this.cityTitle = cityTitle;
    }
    public String getCityTitle(){
        return this.cityTitle;
    }
    public void setRegionTitle(String regionTitle){
        this.regionTitle = regionTitle;
    }
    public String getRegionTitle(){
        return this.regionTitle;
    }
    public void setStationId(int stationId){
        this.stationId = stationId;
    }
    public int getStationId(){
        return this.stationId;
    }
    public void setStationTitle(String stationTitle){
        this.stationTitle = stationTitle;
    }
    public String getStationTitle(){
        return this.stationTitle;
    }

}
