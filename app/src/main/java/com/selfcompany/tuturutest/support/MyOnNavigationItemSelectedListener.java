package com.selfcompany.tuturutest.support;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.selfcompany.tuturutest.AboutActivity;
import com.selfcompany.tuturutest.R;
import com.selfcompany.tuturutest.ScheduleActivity;

/**
 * Created by Sergey on 05.09.2016.
 */
public class MyOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private DrawerLayout drawerLayout;

    public MyOnNavigationItemSelectedListener(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_schedule:
                drawerLayout.closeDrawers();
                if (context.getClass() != ScheduleActivity.class) {
                    Intent toSchedule = new Intent(context, ScheduleActivity.class);
                    context.startActivity(toSchedule);
                }
                return true;
            case R.id.menu_about:
                drawerLayout.closeDrawers();
                if (context.getClass() != AboutActivity.class) {
                    Intent toInfo = new Intent(context, AboutActivity.class);
                    context.startActivity(toInfo);
                }
                return true;
            default:
                return false;
        }
    }
}
