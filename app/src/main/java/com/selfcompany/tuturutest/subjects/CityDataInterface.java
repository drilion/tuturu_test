package com.selfcompany.tuturutest.subjects;

import io.realm.RealmList;

/**
 * Created by Sergey on 05.09.2016.
 */
public interface CityDataInterface {

    String COUNTRY_TITLE = "countryTitle";
    String POINT = "point";
    String DISTRICT_TITLE = "districtTitle";
    String CITY_ID = "cityId";
    String CITY_TITLE = "cityTitle";
    String REGION_TITLE = "regionTitle";
    String STATIONS = "stations";

    void setCountryTitle(String countryTitle);
    String getCountryTitle();
    void setPoint(Point point);
    Point getPoint();
    void setDistrictTitle(String districtTitle);
    String getDistrictTitle();
    void setCityId(int cityId);
    int getCityId();
    void setCityTitle(String cityTitle);
    String getCityTitle();
    void setRegionTitle(String regionTitle);
    String getRegionTitle();
    void setStations(RealmList<Stations> stations);
    RealmList<Stations> getStations();


}
