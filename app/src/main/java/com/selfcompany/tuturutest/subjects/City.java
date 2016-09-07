package com.selfcompany.tuturutest.subjects;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by Sergey on 05.09.2016.
 */
public class City implements ParentListItem {

    private String countryTitle;
    private Point point;
    private String districtTitle;
    private int cityId;
    private String cityTitle;
    private String regionTitle;
    private List<Station> stations;

    public City(String countryTitle, Point point, String districtTitle, int cityId,
                String cityTitle, String regionTitle, List<Station> stations){
        this.countryTitle = countryTitle;
        this.point = point;
        this.districtTitle = districtTitle;
        this.cityId = cityId;
        this.cityTitle = cityTitle;
        this.regionTitle = regionTitle;
        this.stations = stations;
    }

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
    public void setStations(List<Station> stations){
        this.stations = stations;
    }
    public List<Station> getStations(){
        return this.stations;
    }

    @Override
    public List<Station> getChildItemList() {
        return getStations();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
