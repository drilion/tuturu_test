package com.selfcompany.tuturutest.subjects;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Sergey on 04.09.2016.
 */
public class DataRoot extends RealmObject {
    public static final String CITIES_FROM = "citiesFrom";
    public static final String CITIES_TO = "citiesTo";

    @SerializedName("citiesFrom")
    private RealmList<CitiesFrom> citiesFrom;
    @SerializedName("citiesTo")
    private RealmList<CitiesTo> citiesTo;

    public DataRoot(){ }

    public void setCitiesFrom(RealmList<CitiesFrom> citiesFrom){
        this.citiesFrom = citiesFrom;
    }
    public RealmList<CitiesFrom> getCitiesFrom(){
        return this.citiesFrom;
    }
    public void setCitiesTo(RealmList<CitiesTo> citiesTo){
        this.citiesTo = citiesTo;
    }
    public RealmList<CitiesTo> getCitiesTo(){
        return this.citiesTo;
    }
}
