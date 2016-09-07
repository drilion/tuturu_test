package com.selfcompany.tuturutest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.selfcompany.tuturutest.subjects.CitiesFrom;
import com.selfcompany.tuturutest.subjects.CitiesTo;
import com.selfcompany.tuturutest.subjects.City;
import com.selfcompany.tuturutest.subjects.CityDataInterface;
import com.selfcompany.tuturutest.subjects.Station;
import com.selfcompany.tuturutest.subjects.Stations;
import com.selfcompany.tuturutest.support.MyExpandableRecyclerViewAdapter;
import com.selfcompany.tuturutest.support.MyOnNavigationItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Sergey on 05.09.2016.
 */
public class StationSelectorActivity extends AppCompatActivity {

    public static final int PICK_FROM_STATION_REQUEST = 1;
    public static final int PICK_TO_STATION_REQUEST = 2;

    Context context;

    DrawerLayout drawerLayout;
    RecyclerView recyclerView;

    Realm realm = Realm.getDefaultInstance();
    RealmResults<? extends RealmObject> cities;
    List<City> adapterCities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station_selector);

        context = this;

        drawerLayout = (DrawerLayout) findViewById(R.id.selector_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.selector_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.selector_nav_view);
        navigationView.setNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener(this, drawerLayout));

        //формат сортировки
        String [] sortType = {CitiesFrom.COUNTRY_TITLE, CitiesFrom.CITY_TITLE};
        Sort [] sortOrder = {Sort.ASCENDING, Sort.ASCENDING};
        //cities - запись обьектов из БД, соответсвующие необходимому списку выбора;
        //обьекты cities - "живые" (изменение обьектов этого списка влечет соответсвующие изменения в БД)
        switch (getIntent().getIntExtra("mode", 0)) {
            case PICK_FROM_STATION_REQUEST:
                cities = realm.where(CitiesFrom.class).findAllSorted(sortType, sortOrder);
                break;
            case PICK_TO_STATION_REQUEST:
                cities = realm.where(CitiesTo.class).findAllSorted(sortType, sortOrder);
                break;
        }

        //копия "живых" обьектов, их редактирование не влияет на БД
        adapterCities = prepareAdapterData();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyExpandableRecyclerViewAdapter(this,
                adapterCities, itemClickListener));

        final SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(onQueryTextListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //слушатель кликов и долгих нажатий по элментам развернутого списка (станции)
    //клик - возвращает ID кликнутой станции в активити заполнения полей
    //долгое нажатие - вызов активити с детальной информацией по станции, несет инфо о ID станции
    private MyExpandableRecyclerViewAdapter.ItemClickListener itemClickListener = new MyExpandableRecyclerViewAdapter.ItemClickListener() {
        @Override
        public void onClickItem(int id) {
            Intent result = new Intent();
            result.putExtra("id", id);
            StationSelectorActivity.this.setResult(Activity.RESULT_OK, result);
            StationSelectorActivity.this.finish();
        }

        @Override
        public void onLongClickItem(int id) {
            Intent details = new Intent(StationSelectorActivity.this, StationDetailsActivity.class);
            details.putExtra("id", id);
            StationSelectorActivity.this.startActivity(details);
        }
    };
    //слушатель поиска
    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            List<City> filteredList = new ArrayList<>();
            if (newText == null || newText.isEmpty()) {
                filteredList.addAll(adapterCities);
            } else {
                Set<String> searchingWordsSet = new HashSet<String>();
                searchingWordsSet.addAll(Arrays.asList(newText.toLowerCase().trim().split("\\s+")));
                List<City> copy = prepareAdapterData();

                for (int i = 0; i < copy.size(); i++) {
                    for (int j = 0; j < copy.get(i).getStations().size(); j++) {
                        boolean contains = true;
                        for (String searchingWord : searchingWordsSet) {
                            if (!(copy.get(i).getStations().get(j).getSearchableText()).contains(searchingWord)) {
                                contains = false;
                                break;
                            }
                        }
                        if (!contains) { copy.get(i).getStations().remove(j); j--; }
                    }
                    if (copy.get(i).getStations().size() > 0) filteredList.add(copy.get(i));
                }
            }

            recyclerView.setAdapter(new MyExpandableRecyclerViewAdapter(StationSelectorActivity.this,
                    filteredList, itemClickListener));
            return false;
        }
    };

    //копия "живых" обьектов
    private List<City> prepareAdapterData() {

        List<City> result = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            CityDataInterface city = (CityDataInterface) cities.get(i);
            List<Station> stations = new ArrayList<>();
            for (int j = 0; j < city.getStations().size(); j++) {
                Stations station = city.getStations().get(j);
                stations.add(new Station(
                        station.getCountryTitle(),
                        station.getPoint(),
                        station.getDistrictTitle(),
                        station.getCityId(),
                        station.getCityTitle(),
                        station.getRegionTitle(),
                        station.getStationId(),
                        station.getStationTitle()
                ));
            }
            result.add(new City(
                    city.getCountryTitle(),
                    city.getPoint(),
                    city.getDistrictTitle(),
                    city.getCityId(),
                    city.getCityTitle(),
                    city.getRegionTitle(),
                    stations
            ));
        }
        return result;
    }
}
