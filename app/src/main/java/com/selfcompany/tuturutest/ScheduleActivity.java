package com.selfcompany.tuturutest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.selfcompany.tuturutest.subjects.Stations;
import com.selfcompany.tuturutest.support.DataUpdater;
import com.selfcompany.tuturutest.support.MyOnNavigationItemSelectedListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Sergey on 04.09.2016.
 */
public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener{

    Stations fromStation, toStation;
    Calendar departureDate;
    EditText from, to, datePicker;
    FloatingActionButton fab;
    CaldroidFragment caldroidFragment;

    Realm realm;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //конфигурация БД
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplication())
                .name("tuturu.realm")
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        realm = Realm.getDefaultInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.schedule_name));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener(this, drawerLayout));

        //проверка необходимости обновления БД (раз в 3 месяца)
        isNeedToUpdate();

        //поля редактирования без возможности ввода текста, клик вызывает операцию выбора соответсвующего типа
        from = (EditText) findViewById(R.id.from_station_edittext);
        from.setOnClickListener(this);
        to = (EditText) findViewById(R.id.to_station_edittext);
        to.setOnClickListener(this);
        datePicker = (EditText) findViewById(R.id.date_picker_edittext);
        datePicker.setOnClickListener(this);

        setEditTexts();

        //ФАБ - отслеживает корректность данных полей ввода, вызывает дальнейшие действия обработки выбранных данных
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!from.getText().toString().isEmpty() && !to.getText().toString().isEmpty() && !datePicker.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.main_content), "Ищем расписание, ожидайте...", Snackbar.LENGTH_LONG).show();
                }
                else {
                    if (from.getText().toString().isEmpty())
                        ((TextInputLayout) findViewById(R.id.from_station_edittext_wrapper)).setError(getString(R.string.from_error));
                    if (to.getText().toString().isEmpty())
                        ((TextInputLayout) findViewById(R.id.to_station_edittext_wrapper)).setError(getString(R.string.to_error));
                    if (datePicker.getText().toString().isEmpty())
                        ((TextInputLayout) findViewById(R.id.date_picker_wrapper)).setError(getString(R.string.date_picker_error));
                }
            }
        });
        // кнопка замены местами станций отбытия - прибытия
        ImageButton swithcer = (ImageButton) findViewById(R.id.station_switcher);
        swithcer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stations temp = fromStation;
                fromStation = toStation;
                toStation = temp;
                setEditTexts();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fromStation != null) outState.putInt("from", fromStation.getStationId());
        if (toStation != null) outState.putInt("to", toStation.getStationId());
        if (departureDate != null) outState.putLong("date", departureDate.getTimeInMillis());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        realm = Realm.getDefaultInstance();
        fromStation = realm.where(Stations.class).equalTo(Stations.STATION_ID, savedInstanceState.getInt("from")).findFirst();
        toStation = realm.where(Stations.class).equalTo(Stations.STATION_ID, savedInstanceState.getInt("to")).findFirst();
        departureDate = Calendar.getInstance();
        departureDate.setTimeInMillis(savedInstanceState.getLong("date"));
    }

    @Override
    public void onClick(View view) {
        //клики по полям ввода
        switch (view.getId()) {
            // клик по полю ввода станции отправления
            case R.id.from_station_edittext:
                stationRequest(StationSelectorActivity.PICK_FROM_STATION_REQUEST);
                break;
            // клик по полю ввода станции прибытия
            case R.id.to_station_edittext:
                stationRequest(StationSelectorActivity.PICK_TO_STATION_REQUEST);
                break;
            // клик по полю ввода даты
            case R.id.date_picker_edittext:
                caldroidFragment = new CaldroidFragment();
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
                caldroidFragment.setMinDate(cal.getTime());
                caldroidFragment.setArguments(args);
                caldroidFragment.setCaldroidListener(listener);
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.add(R.id.date_picker_wrapper, caldroidFragment);
                t.commit();
                fab.setVisibility(View.GONE);
                break;
        }
    }
    //вызов активити выбора станции
    private void stationRequest(int mode) {
        Intent station_request = new Intent(this, StationSelectorActivity.class);
        station_request.putExtra("mode", mode);
        startActivityForResult(station_request, mode);
    }

    //привязка выбранной станции к соответсвующему полю ввода
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //активити выбора станции возвращает ID выбранной станции, восстанавливаем станцию из БД
            switch (requestCode) {
                case StationSelectorActivity.PICK_FROM_STATION_REQUEST:
                    fromStation = realm.where(Stations.class).equalTo(Stations.STATION_ID, data.getIntExtra("id", 0)).findFirst();
                    if (fromStation != null) from.setText(fromStation.getStationTitle());
                    ((TextInputLayout) findViewById(R.id.from_station_edittext_wrapper)).setError(null);
                    break;
                case StationSelectorActivity.PICK_TO_STATION_REQUEST:
                    toStation = realm.where(Stations.class).equalTo(Stations.STATION_ID, data.getIntExtra("id", 0)).findFirst();
                    if (toStation != null) to.setText(toStation.getStationTitle());
                    ((TextInputLayout) findViewById(R.id.to_station_edittext_wrapper)).setError(null);
                    break;
            }
        }
    }

    private void isNeedToUpdate() {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        Calendar refreshTime = Calendar.getInstance();
        // ключ-значение - численное представление даты следующего обновления
        refreshTime.setTime(new Date(sharedPreferences.getLong(getString(R.string.preference_update_date), 0)));
        //время пришло - устанавливаем ключ-значение новой даты обновления, запускаем обновление БД в бэкграунде
        //альтернативно: обновление БД через сервис... не представляется необходимым, ввиду редкой частоты обновлений
        if (refreshTime.getTime().before(new Date())) {
            Snackbar.make(findViewById(R.id.main_content), "Обновление БД запущено", Snackbar.LENGTH_LONG).show();
            refreshTime.setTime(new Date());
            refreshTime.add(Calendar.MONTH, 3);
            sharedPreferences.edit().putLong(getString(R.string.preference_update_date), refreshTime.getTimeInMillis()).apply();
            new DataUpdater(this).execute();
        }
    }

    //слушатель событий фрагмента выбора даты;
    //отслеживаем только клики по датам - прячем фрагмент, выбранную дату привызываем к полю ввода даты
    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            datePicker.setText(DateFormat.getDateInstance(DateFormat.LONG).format(date));
            ((TextInputLayout) findViewById(R.id.date_picker_wrapper)).setError(null);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.remove(caldroidFragment);
            t.commit();
            fab.setVisibility(View.VISIBLE);
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setEditTexts() {
        if (fromStation != null) from.setText(fromStation.getStationTitle()); else from.setText("");
        if (toStation != null) to.setText(toStation.getStationTitle()); else to.setText("");
        if (departureDate != null) datePicker.setText(DateFormat.getDateInstance(DateFormat.LONG).format(departureDate));
        else datePicker.setText("");
    }
}