package com.selfcompany.tuturutest.subjects;

/**
 * Created by Sergey on 05.09.2016.
 */
public class Station {

    private String countryTitle;
    private Point point;
    private String districtTitle;
    private int cityId;
    private String cityTitle;
    private String regionTitle;
    private int stationId;
    private String stationTitle;
    private String searchableText;

    public Station(String countryTitle, Point point, String districtTitle, int cityId,
                   String cityTitle, String regionTitle, int stationId, String stationTitle){
        this.countryTitle = countryTitle;
        this.point = point;
        this.districtTitle = districtTitle;
        this.cityId = cityId;
        this.cityTitle = cityTitle;
        this.regionTitle = regionTitle;
        this.stationId = stationId;
        this.stationTitle = stationTitle;
        setSearchableText();
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

    public String getSearchableText() {
        return searchableText;
    }

    public void setSearchableText() {
        this.searchableText = (new StringBuilder().append(getCountryTitle().toLowerCase()).append(" ")
                .append(getCityTitle().toLowerCase()).append(" ")
                .append(getStationTitle().toLowerCase())).toString();
    }
}
