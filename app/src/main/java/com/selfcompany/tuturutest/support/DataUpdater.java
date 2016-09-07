package com.selfcompany.tuturutest.support;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import com.google.gson.GsonBuilder;
import com.selfcompany.tuturutest.R;
import com.selfcompany.tuturutest.ScheduleActivity;
import com.selfcompany.tuturutest.subjects.DataRoot;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import io.realm.Realm;

/**
 * Created by Sergey on 05.09.2016.
 */
public class DataUpdater extends AsyncTask<Void, Void, Void> {

    Realm realm;
    Context context;

    public DataUpdater(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //открываем файл... в последующем заменяем на запрос к веб серверу
            Reader reader = new InputStreamReader(context.getAssets().open("allStations.json"));

            //парс JSONа
            final DataRoot dataRoot = new GsonBuilder().create().fromJson(reader, DataRoot.class);

            //зачищаем БД, формируем по новой
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                    realm.copyToRealm(dataRoot);
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Snackbar.make(((ScheduleActivity) context).findViewById(R.id.main_content), "БД обновлена", Snackbar.LENGTH_LONG).show();
    }
}
