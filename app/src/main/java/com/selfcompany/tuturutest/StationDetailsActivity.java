package com.selfcompany.tuturutest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.selfcompany.tuturutest.subjects.Stations;
import com.selfcompany.tuturutest.support.MyOnNavigationItemSelectedListener;

import java.util.Locale;

import io.realm.Realm;

/**
 * Created by Sergey on 05.09.2016.
 */
public class StationDetailsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView stationName, districtName, cityName, regionName, countryName, location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station_details);

        drawerLayout = (DrawerLayout) findViewById(R.id.details_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.details_nav_view);
        navigationView.setNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener(this, drawerLayout));

        Realm realm = Realm.getDefaultInstance();
        //восстанавливаем станцию для отображения из БД по ID
        final Stations station = realm.where(Stations.class).equalTo(Stations.STATION_ID, getIntent().getIntExtra("id", -1)).findFirst();

        stationName = (TextView) findViewById(R.id.station_name);
        districtName = (TextView) findViewById(R.id.district_name);
        cityName = (TextView) findViewById(R.id.city_name);
        regionName = (TextView) findViewById(R.id.region_name);
        countryName = (TextView) findViewById(R.id.country_name);
        location = (TextView) findViewById(R.id.location);
        TextView link = (TextView) findViewById(R.id.loc_link);

        if (station != null) {

            if (station.getStationTitle().isEmpty())
                stationName.setText(getString(R.string.station_title, "Не указано"));
            else
                stationName.setText(getString(R.string.station_title, station.getStationTitle()));

            if (station.getDistrictTitle().isEmpty())
                districtName.setText(getString(R.string.district_title, "Не указано"));
            else
                districtName.setText(getString(R.string.district_title, station.getDistrictTitle()));

            if (station.getCityTitle().isEmpty())
                cityName.setText(getString(R.string.city_title, "Не указано"));
            else
                cityName.setText(getString(R.string.city_title, station.getCityTitle()));

            if (station.getRegionTitle().isEmpty())
                regionName.setText(getString(R.string.region_title, "Не указано"));
            else
                regionName.setText(getString(R.string.region_title, station.getRegionTitle()));

            if (station.getCountryTitle().isEmpty())
                countryName.setText(getString(R.string.country_title, "Не указано"));
            else
                countryName.setText(getString(R.string.country_title, station.getCountryTitle()));

            if (station.getPoint() == null) {
                location.setText(getString(R.string.location, "Не указано"));
                link.setVisibility(View.GONE);
            }
            else {
                //неявный интент типа ВЬЮ с геоданными открывает приложение просмотра карты
                location.setText(getString(R.string.location, ""));
                link.setVisibility(View.VISIBLE);
                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri geoLocation = Uri.parse(getString(R.string.geo_uri,
                                String.format(Locale.ROOT, "%f", station.getPoint().getLatitude()),
                                String.format(Locale.ROOT, "%f", station.getPoint().getLongitude())));
                        Intent intent = new Intent(Intent.ACTION_VIEW, geoLocation);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }

                    }
                });
            }
        }
        else Snackbar.make(drawerLayout, "some troubles", Snackbar.LENGTH_LONG).show();
    }

}
