package com.selfcompany.tuturutest.subjects;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sergey on 04.09.2016.
 */
public class CitiesTo extends RealmObject implements CityDataInterface{

    @SerializedName("countryTitle")
    private String countryTitle;
    @SerializedName("point")
    private Point point;
    @SerializedName("districtTitle")
    private String districtTitle;
    @SerializedName("cityId")
    @PrimaryKey
    private int cityId;
    @SerializedName("cityTitle")
    private String cityTitle;
    @SerializedName("regionTitle")
    private String regionTitle;
    @SerializedName("stations")
    private RealmList<Stations> stations;

    public CitiesTo(){ }
    @Override
    public void setCountryTitle(String countryTitle){
        this.countryTitle = countryTitle;
    }
    @Override
    public String getCountryTitle(){
        return this.countryTitle;
    }
    @Override
    public void setPoint(Point point){
        this.point = point;
    }
    @Override
    public Point getPoint(){
        return this.point;
    }
    @Override
    public void setDistrictTitle(String districtTitle){
        this.districtTitle = districtTitle;
    }
    @Override
    public String getDistrictTitle(){
        return this.districtTitle;
    }
    @Override
    public void setCityId(int cityId){
        this.cityId = cityId;
    }
    @Override
    public int getCityId(){
        return this.cityId;
    }
    @Override
    public void setCityTitle(String cityTitle){
        this.cityTitle = cityTitle;
    }
    @Override
    public String getCityTitle(){
        return this.cityTitle;
    }
    @Override
    public void setRegionTitle(String regionTitle){
        this.regionTitle = regionTitle;
    }
    @Override
    public String getRegionTitle(){
        return this.regionTitle;
    }
    @Override
    public void setStations(RealmList<Stations> stations){
        this.stations = stations;
    }
    @Override
    public RealmList<Stations> getStations(){
        return this.stations;
    }

}
